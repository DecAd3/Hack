package service;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
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
        Company company = rework.getCompany();
        LinkedBlockingQueue<WorkTask> queue = rework.getQueue();
        if (employee.getHealthyStatus() == HEALTHY_STATUS.ISOLATED) {
            queue.add(new WorkTask(employee, company.getFloorList().get(employee.gettFloor()).getOffice()));
            return;
        }

    }
}
