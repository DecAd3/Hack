package model;

public class Employee {
    private int number;
    private int floor;
    private int workStatus;
    private int healthyStatus;

    public Employee(int floor, int number) {
        this.number = number;
        this.floor = floor;
        this.workStatus = STATUS_HOMING;
        this.healthyStatus = STATUS_HEALTHY;
    }

    public int getNumber() {
        return number;
    }

    public int gettFloor() {
        return floor;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public int getHealthyStatus() {
        return healthyStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public void setHealthyStatus(int healthyStatus) {
        this.healthyStatus = healthyStatus;
    }

    @Override
    public String toString() {
        return floor + " - " + number;
    }

    // work status
    public final static int STATUS_HOMING = 0;
    public final static int STATUS_WORKING = 1;
    public final static int STATUS_RESTING = 2;
    public final static int STATUS_MEETING = 3;
    public final static int STATUS_PEEING = 4;
    public final static int STATUS_LIFTING = 5;
    // healthy status
    public final static int STATUS_HEALTHY = 0;
    public final static int STATUS_RISKY = 1;
    public final static int STATUS_INFECTED = 2;
    public final static int STATUS_ISOLATED = 3;

}
