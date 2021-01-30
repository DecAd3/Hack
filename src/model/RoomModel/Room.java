package model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import model.Employee;

public abstract class Room {
    protected int number;
    protected int floor;
    protected int capacity;
    protected STATUS useStatus;
    protected int useDuration;
    protected List<Employee> employeeList;

    public Room() {
        this.capacity = Integer.MAX_VALUE;
        this.employeeList = new ArrayList<>();
        this.useStatus = STATUS.FREE;
        this.useDuration = 0;
    }

    public Room(int floor, int number, int capacity) {
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
        this.employeeList = new ArrayList<>();
        this.useStatus = STATUS.FREE;
        this.useDuration = 0;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public STATUS getUseStatus() {
        return useStatus;
    }

    public int getUseDuration() {
        return useDuration;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setUseStatus(STATUS useStatus) {
        this.useStatus = useStatus;
    }

    public void setUseDuration(int useDuration) {
        this.useDuration = useDuration;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    synchronized public void join(Employee e) {
        this.useStatus = STATUS.IN_USE;
        this.employeeList.add(e);
    }

    public enum STATUS {
        IN_USE, FREE
    }

    public enum ROOM_TYPE {
        Office, MeetingRoom, Elevator, RestRoom, Toilet,
    }

}
