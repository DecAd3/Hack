package service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.Employee.WORK_STATUS;
import model.Floor;
import model.RoomModel.Elevator;
import model.RoomModel.MeetingRoom;
import model.RoomModel.Office;
import model.RoomModel.RestRoom;
import model.RoomModel.Room;
import model.RoomModel.Toilet;
import util.Constants;

public class WorkTask {
    private Employee employee;
    private Room actionRoom;

    public WorkTask(Employee employee, Room actionRoom) {
        this.employee = employee;
        this.actionRoom = actionRoom;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Room getActionRoom() {
        return actionRoom;
    }

    public void setActionRoom(Room actionRoom) {
        this.actionRoom = actionRoom;
    }

    public void doTask() throws IOException {
        ReworkService rework = ReworkService.getInstance();
        Company company = Company.getInstance();
        LinkedBlockingQueue<WorkTask> queue = rework.getQueue();
        if (employee.getHealthyStatus() == HEALTHY_STATUS.ISOLATED) {
            queue.add(new WorkTask(employee, company.getFloorList().get(employee.getFloor()).getOffice()));
            return;
        }

        Room room = null;
        final double rand_double = Math.random();// 0-1

        AtomicBoolean isExposed = new AtomicBoolean(false);
        List<Employee> employeeList = new ArrayList<>();
        if (actionRoom instanceof MeetingRoom) {
            employee.setWorkStatus(WORK_STATUS.MEETING);
            MeetingRoom meetingRoom = (MeetingRoom) actionRoom;
            employeeList = meetingRoom.getEmployeeList();
        } else if (actionRoom instanceof Toilet) {
            employee.setWorkStatus(WORK_STATUS.PEEING);
            Toilet toilet = (Toilet) actionRoom;
            employeeList = toilet.getEmployeeList();
        } else if (actionRoom instanceof RestRoom) {
            employee.setWorkStatus(WORK_STATUS.RESTING);
            RestRoom restroom = (RestRoom) actionRoom;
            employeeList = restroom.getEmployeeList();
        } else if (actionRoom instanceof Elevator) {
            employee.setWorkStatus(WORK_STATUS.LIFTING);
            Elevator elevator = (Elevator) actionRoom;
            employeeList = elevator.getEmployeeList();
        } else if (actionRoom instanceof Office) {
            // Assume officers are safe at their workplace
            employee.setWorkStatus(WORK_STATUS.WORKING);
        }

        BufferedWriter logFile = rework.getBufferWritter();
        if (!(actionRoom instanceof Office)) {
            logFile.write(employee.toString() + " is in " + actionRoom.toString());
            logFile.write("\n");
            logFile.flush();
        }
        // Begin infection
        String infectedEmployeeNum = "";
        if (employeeList != null) {
            if (employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED) {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() != HEALTHY_STATUS.INFECTED) {
                        if (rand_double <= Constants.P_INFECT) {
                            employeeList.get(i).setHealthyStatus(HEALTHY_STATUS.INFECTED);
                            logFile.write(employeeList.get(i).toString() + " is infected, source of infection: "
                                    + employee.toString() + ", location: " + actionRoom.toString());
                            logFile.write("\n");
                            logFile.flush();
                        } else {
                            employeeList.get(i).setHealthyStatus(HEALTHY_STATUS.RISKY);
                            logFile.write(employeeList.get(i).toString()
                                    + "has close contact with infected persons thus become risky, the source of infection is"
                                    + employee.toString() + ", location " + actionRoom.toString());
                            logFile.write("\n");
                            logFile.flush();
                        }
                    }
                }
            } else {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() == HEALTHY_STATUS.INFECTED) {
                        infectedEmployeeNum = employeeList.get(i).toString();
                        isExposed.set(true);
                        break;
                    }
                }
            }
        }
        if (isExposed.get()) {
            if (rand_double <= Constants.P_INFECT) {
                if (employee.getHealthyStatus() != HEALTHY_STATUS.INFECTED) {
                    employee.setHealthyStatus(HEALTHY_STATUS.INFECTED);
                    logFile.write(employee.toString() + " is infected, source of infection: " + infectedEmployeeNum
                            + ", location: " + actionRoom.toString());
                    logFile.write("\n");
                    logFile.flush();
                }
            } else {
                if (employee.getHealthyStatus() == HEALTHY_STATUS.HEALTHY) {
                    employee.setHealthyStatus(HEALTHY_STATUS.RISKY);
                    logFile.write(employee.toString()
                            + "has close contact with infected persons thus become risky, the source of infection is"
                            + infectedEmployeeNum + ", location:" + actionRoom.toString());
                    logFile.write("\n");
                    logFile.flush();
                }
            }
        }
        // Switch room status
        room = switchStatus(employee, actionRoom);
        queue.add(new WorkTask(employee, room));
    }

    private WORK_STATUS getNextStatus() {
        WORK_STATUS nextStatus = WORK_STATUS.WORKING;
        double rand_double = Math.random();// 0-1
        if (rand_double <= Constants.P_MEETING) {
            nextStatus = WORK_STATUS.MEETING;
        } else if (rand_double > Constants.P_MEETING && rand_double <= (Constants.P_MEETING + Constants.P_RESTING)) {
            nextStatus = WORK_STATUS.RESTING;
        } else if (rand_double > (Constants.P_MEETING + Constants.P_RESTING)
                && rand_double <= (Constants.P_MEETING + Constants.P_RESTING + Constants.P_PEEING)) {
            nextStatus = WORK_STATUS.PEEING;
        } else if (rand_double > (Constants.P_MEETING + Constants.P_RESTING + Constants.P_PEEING)
                && rand_double <= (Constants.P_MEETING + Constants.P_RESTING + Constants.P_PEEING
                        + Constants.P_LIFTING)) {
            nextStatus = WORK_STATUS.LIFTING;
        }
        return nextStatus;
    }

    private Room switchStatus(Employee e, Room room) {

        Company company = Company.getInstance();

        if (e.getWorkStatus() == WORK_STATUS.WORKING) {

            // can switch room, calculate the possibility
            WORK_STATUS nextStatus = getNextStatus();

            Floor floor = company.getFloorList().get(e.getFloor());

            // use room in the same floor first (meeting room except)
            switch (nextStatus) {
                case MEETING:
                    MeetingRoom freeMeetingRoom = (MeetingRoom) floor.getFreeMeetingRoom();
                    if (freeMeetingRoom == null) {
                        freeMeetingRoom = (MeetingRoom) company.getFreeMeetingRoomPool();
                        if (freeMeetingRoom == null) {
                            // back to office
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeMeetingRoom.join(e);
                    return freeMeetingRoom;
                case RESTING:
                    RestRoom freeRestroom = (RestRoom) floor.getFreeRestRoom();
                    if (freeRestroom == null) {
                        freeRestroom = (RestRoom) company.getFreeRestroomPool();
                        if (freeRestroom == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeRestroom.join(e);
                    return freeRestroom;
                case PEEING:
                    Toilet freeToilet = (Toilet) floor.getFreeToilet();
                    if (freeToilet == null) {
                        freeToilet = (Toilet) company.getFreeToiletPool();
                        if (freeToilet == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeToilet.join(e);
                    return freeToilet;
                case LIFTING:
                    Elevator freeElevator = (Elevator) company.getFreeElevator();
                    if (freeElevator == null) {
                        return company.getFloorList().get(e.getFloor()).getOffice();
                    }
                    freeElevator.join(e);
                    return freeElevator;
                default:
                    return company.getFloorList().get(e.getFloor()).getOffice();
            }
        } else if (e.getWorkStatus() == WORK_STATUS.HOMING) {

            e.setWorkStatus(WORK_STATUS.WORKING);
            return company.getFloorList().get(e.getFloor()).getOffice();
        } else {

            return room;
        }
    }

}
