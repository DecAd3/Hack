package service;

import model.Employee;
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

}
