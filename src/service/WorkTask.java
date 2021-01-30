package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.RoomModel.MeetingRoom;
import model.RoomModel.Room;

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
            queue.add(new WorkTask(employee, company.getFloorList().get(employee.gettFloor()).getOffice()));
            return;
        }

        Room room = null;
        final double d = Math.random();
        Room room = null;
        final double d = Math.random();
        AtomicBoolean isInfected = new AtomicBoolean(false);
        List<Employee> employeeList = new ArrayList<>();
        if (actionRoom instanceof MeetingRoom) {
            employee.setWorkStatus(Employee.STATUS_MEETING);
            Company.Meetingroom meetingroom = (Company.Meetingroom) actionRoom;
            employeeList = meetingroom.getEmployeeList();
        } else if (actionRoom instanceof Company.Toilet) {
            employee.setWorkStatus(Employee.STATUS_PEEING);
            Company.Toilet toilet = (Company.Toilet) actionRoom;
            employeeList = toilet.getEmployeeList();
        } else if (actionRoom instanceof Company.Restroom) {
            employee.setWorkStatus(Employee.STATUS_RESTING);
            Company.Restroom restroom = (Company.Restroom) actionRoom;
            employeeList = restroom.getEmployeeList();
        } else if (actionRoom instanceof Company.Elevator) {
            employee.setWorkStatus(STATUS_LIFTING);
            Company.Elevator elevator = (Company.Elevator) actionRoom;
            employeeList = elevator.getEmployeeList();
        } else if (actionRoom instanceof Company.Office) {
            // 假设社畜在工位上是安全的
            employee.setWorkStatus(STATUS_WORKING);
        }
        if (!(actionRoom instanceof Company.Office)) {
            out.write(employee.getNumber() + "号员工在" + actionRoom.getNumber());
            out.write("\n");
            out.flush();
        }
        // 开始感染
        String infectedEmployeeNum = "";
        if (employeeList != null) {
            if (employee.getHealthyStatus() == STATUS_INFECTED) {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() != STATUS_INFECTED) {
                        if (d <= INFECT_POSSIBILITY) {
                            employeeList.get(i).setHealthyStatus(STATUS_INFECTED);
                            out.write(employeeList.get(i).getNumber() + "号员工被感染,感染源：" + employee.getNumber() + ",地点："
                                    + actionRoom.getNumber());
                            out.write("\n");
                            out.flush();
                        } else {
                            employeeList.get(i).setHealthyStatus(STATUS_RISKY);
                            out.write(employeeList.get(i).getNumber() + "号员工亲密接触过感染者，存在风险, 亲密接触感染者："
                                    + employee.getNumber() + ",地点：" + actionRoom.getNumber());
                            out.write("\n");
                            out.flush();
                        }
                    }
                }
            } else {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (employeeList.get(i).getHealthyStatus() == STATUS_INFECTED) {
                        infectedEmployeeNum = employeeList.get(i).getNumber();
                        isInfected.set(true);
                        break;
                    }
                }
            }
        }
        if (isInfected.get()) {
            if (d <= INFECT_POSSIBILITY) {
                if (employee.getHealthyStatus() != STATUS_INFECTED) {
                    employee.setHealthyStatus(Employee.STATUS_INFECTED);
                    out.write(employee.getNumber() + "号员工被感染,感染源：" + infectedEmployeeNum + ",地点："
                            + actionRoom.getNumber());
                    out.write("\n");
                    out.flush();
                }
            } else {
                if (employee.getHealthyStatus() == STATUS_HEALTHY) {
                    employee.setHealthyStatus(STATUS_RISKY);
                    out.write(employee.getNumber() + "号员工亲密接触过感染者，存在风险, 亲密接触感染者：" + infectedEmployeeNum + ",地点："
                            + actionRoom.getNumber());
                    out.write("\n");
                    out.flush();
                }
            }
        }
        // 切换状态
        room = switchStatus(employee, actionRoom);
        queue.add(new WorkTask(employee, room));
    }

    private Room switchStatus(Employee e, Room room) {
        if (e.getWorkStatus() == STATUS_WORKING) {
            // 可以更改状态,摇骰子
            int nextStatus = STATUS_WORKING;
            double d = Math.random();
            if (d <= MEETING_POSSIBILITY) {
                nextStatus = STATUS_MEETING;
            } else if (d > MEETING_POSSIBILITY && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY)) {
                nextStatus = STATUS_RESTING;
            } else if (d > (MEETING_POSSIBILITY + RESTING_POSSIBILITY)
                    && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY)) {
                nextStatus = STATUS_PEEING;
            } else if (d > (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY)
                    && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY + LIFTING_POSSIBILITY)) {
                nextStatus = STATUS_LIFTING;
            }
            Company.Floor floor = company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0]));
            // 除了电梯类型外，其他房间都优先选择本层
            switch (nextStatus) {
                case STATUS_MEETING:
                    Company.Meetingroom freeMeetingRoom = floor.getFreeMeetingroom();
                    if (freeMeetingRoom == null) {
                        freeMeetingRoom = company.getFreeMeetingroomPool();
                        if (freeMeetingRoom == null) {
                            // 无可用房间，返回办公区
                            return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0]))
                                    .getOffice();
                        }
                    }
                    freeMeetingRoom.join(e);
                    return freeMeetingRoom;
                case STATUS_RESTING:
                    Company.Restroom freeRestroom = floor.getFreeRestroom();
                    if (freeRestroom == null) {
                        freeRestroom = company.getFreeRestroomPool();
                        if (freeRestroom == null) {
                            return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0]))
                                    .getOffice();
                        }
                    }
                    freeRestroom.join(e);
                    return freeRestroom;
                case STATUS_PEEING:
                    Company.Toilet freeToilet = floor.getFreeToilet();
                    if (freeToilet == null) {
                        freeToilet = company.getFreeToiletPool();
                        if (freeToilet == null) {
                            return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0]))
                                    .getOffice();
                        }
                    }
                    freeToilet.join(e);
                    return freeToilet;
                case STATUS_LIFTING:
                    Company.Elevator freeElevator = company.getFreeElevator();
                    if (freeElevator == null) {
                        return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0])).getOffice();
                    }
                    freeElevator.join(e);
                    return freeElevator;
                default:
                    return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0])).getOffice();
            }
        } else if (e.getWorkStatus() == STATUS_HOMING) {
            // 在家, 返回办公区复工
            e.setWorkStatus(STATUS_WORKING);
            return company.getFloorList().get(Integer.parseInt(e.getNumber().split("-")[0])).getOffice();
        } else {
            // 保持原状态，等待释放
            return room;
        }
    }

}
