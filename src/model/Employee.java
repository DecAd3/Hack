package model;

public class Employee {
    private int number;
    private int floor;
    private WORK_STATUS workStatus;
    private HEALTHY_STATUS healthyStatus;

    public Employee(int floor, int number) {
        this.number = number;
        this.floor = floor;
        this.workStatus = WORK_STATUS.HOMING;
        this.healthyStatus = HEALTHY_STATUS.HEALTHY;
    }

    public int getNumber() {
        return number;
    }

    public int gettFloor() {
        return floor;
    }

    public WORK_STATUS getWorkStatus() {
        return workStatus;
    }

    public HEALTHY_STATUS getHealthyStatus() {
        return healthyStatus;
    }

    public void setWorkStatus(WORK_STATUS workStatus) {
        this.workStatus = workStatus;
    }

    public void setHealthyStatus(HEALTHY_STATUS healthyStatus) {
        this.healthyStatus = healthyStatus;
    }

    @Override
    public String toString() {
        return floor + " - " + number;
    }

    public enum WORK_STATUS {
        HOMING, WORKING, RESTING, MEETING, PEEING, LIFTING
    }

    public enum HEALTHY_STATUS {
        HEALTHY, RISKY, INFECTED, ISOLATED
    }

}
