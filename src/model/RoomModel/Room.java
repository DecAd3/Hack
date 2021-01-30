package model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import model.Employee;

public abstract class Room {
    protected int number;
    protected int floor;
    protected int capacity;
    protected int useStatus;
    protected int useDuration;
    protected List<Employee> employeeList;

    public Room() {
        this.capacity = Integer.MAX_VALUE;
        this.employeeList = new ArrayList<>();
        this.useStatus = STATUS_FREE;
        this.useDuration = 0;
    }

    public Room(int floor, int number, int capacity) {
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
        this.employeeList = new ArrayList<>();
        this.useStatus = STATUS_FREE;
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

    public int getUseStatus() {
        return useStatus;
    }

    public int getUseDuration() {
        return useDuration;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public void setUseDuration(int useDuration) {
        this.useDuration = useDuration;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    synchronized public void join(Employee e) {
        this.useStatus = STATUS_IN_USE;
        this.employeeList.add(e);
    }

    public final static int STATUS_IN_USE = 1;
    public final static int STATUS_FREE = 0;
}