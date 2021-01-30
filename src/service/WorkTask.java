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
        final double d = Math.random();

        AtomicBoolean isInfected = new AtomicBoolean(false);
        List<Employee> employeeList = new ArrayList<>();
        if (actionRoom instanceof MeetingRoom) {
            employee.setWorkStatus(WORK_STATUS.MEETING);
            MeetingRoom meetingroom = (MeetingRoom) actionRoom;
            employeeList = meetingroom.getEmployeeList();
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
            // 假设社畜在工位上是安全的
            employee.setWorkStatus(WORK_STATUS.WORKING);
        }
        BufferedWriter logFile = rework.getBufferWritter();
        if (!(actionRoom instanceof Office)) {
            logFile.write(employee.toString() + "号员工在" + actionRoom.toString());
            logFile.write("\n");
            logFile.flush();
        }
        // 开始感染
        String infectedEmployeeNum = "";
        if (employeeList != null) {
            if (employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED) {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() != HEALTHY_STATUS.INFECTED) {
                        if (d <= Constants.INFECT_POSSIBILITY) {
                            employeeList.get(i).setHealthyStatus(HEALTHY_STATUS.INFECTED);
                            logFile.write(employeeList.get(i).toString() + "号员工被感染,感染源：" + employee.toString() + ",地点："
                                    + actionRoom.toString());
                            logFile.write("\n");
                            logFile.flush();
                        } else {
                            employeeList.get(i).setHealthyStatus(HEALTHY_STATUS.RISKY);
                            logFile.write(employeeList.get(i).toString() + "号员工亲密接触过感染者，存在风险, 亲密接触感染者："
                                    + employee.toString() + ",地点：" + actionRoom.toString());
                            logFile.write("\n");
                            logFile.flush();
                        }
                    }
                }
            } else {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() == HEALTHY_STATUS.INFECTED) {
                        infectedEmployeeNum = employeeList.get(i).toString();
                        isInfected.set(true);
                        break;
                    }
                }
            }
        }
        if (isInfected.get()) {
            if (d <= Constants.INFECT_POSSIBILITY) {
                if (employee.getHealthyStatus() != HEALTHY_STATUS.INFECTED) {
                    employee.setHealthyStatus(HEALTHY_STATUS.INFECTED);
                    logFile.write(
                            employee.toString() + "号员工被感染,感染源：" + infectedEmployeeNum + ",地点：" + actionRoom.toString());
                    logFile.write("\n");
                    logFile.flush();
                }
            } else {
                if (employee.getHealthyStatus() == HEALTHY_STATUS.HEALTHY) {
                    employee.setHealthyStatus(HEALTHY_STATUS.RISKY);
                    logFile.write(employee.toString() + "号员工亲密接触过感染者，存在风险, 亲密接触感染者：" + infectedEmployeeNum + ",地点："
                            + actionRoom.toString());
                    logFile.write("\n");
                    logFile.flush();
                }
            }
        }
        // 切换状态
        room = switchStatus(employee, actionRoom);
        queue.add(new WorkTask(employee, room));
    }

    private Room switchStatus(Employee e, Room room) {
        Company company = Company.getInstance();
        if (e.getWorkStatus() == WORK_STATUS.WORKING) {
            // 可以更改状态,摇骰子
            WORK_STATUS nextStatus = WORK_STATUS.WORKING;
            double d = Math.random();
            if (d <= Constants.MEETING_POSSIBILITY) {
                nextStatus = WORK_STATUS.MEETING;
            } else if (d > Constants.MEETING_POSSIBILITY
                    && d <= (Constants.MEETING_POSSIBILITY + Constants.RESTING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.RESTING;
            } else if (d > (Constants.MEETING_POSSIBILITY + Constants.RESTING_POSSIBILITY)
                    && d <= (Constants.MEETING_POSSIBILITY + Constants.RESTING_POSSIBILITY
                            + Constants.PEEING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.PEEING;
            } else if (d > (Constants.MEETING_POSSIBILITY + Constants.RESTING_POSSIBILITY
                    + Constants.PEEING_POSSIBILITY)
                    && d <= (Constants.MEETING_POSSIBILITY + Constants.RESTING_POSSIBILITY
                            + Constants.PEEING_POSSIBILITY + Constants.LIFTING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.LIFTING;
            }
            Floor floor = company.getFloorList().get(e.getFloor());
            // 除了电梯类型外，其他房间都优先选择本层
            switch (nextStatus) {
                case MEETING:
                    MeetingRoom freeMeetingRoom = floor.getFreeMeetingRoom();
                    if (freeMeetingRoom == null) {
                        freeMeetingRoom = company.getFreeMeetingRoomPool();
                        if (freeMeetingRoom == null) {
                            // 无可用房间，返回办公区
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeMeetingRoom.join(e);
                    return freeMeetingRoom;
                case RESTING:
                    RestRoom freeRestroom = floor.getFreeRestRoom();
                    if (freeRestroom == null) {
                        freeRestroom = company.getFreeRestroomPool();
                        if (freeRestroom == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeRestroom.join(e);
                    return freeRestroom;
                case PEEING:
                    Toilet freeToilet = floor.getFreeToilet();
                    if (freeToilet == null) {
                        freeToilet = company.getFreeToiletPool();
                        if (freeToilet == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeToilet.join(e);
                    return freeToilet;
                case LIFTING:
                    Elevator freeElevator = company.getFreeElevator();
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
