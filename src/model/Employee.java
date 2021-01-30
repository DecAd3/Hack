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
        HOMING(0), WORKING(1), RESTING(2), MEETING(3), PEEING(4), LIFTING(5);

        private final int value;

        WORK_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum HEALTHY_STATUS {
        HEALTHY(0), RISKY(1), INFECTED(2), ISOLATED(3);

        private final int value;

        HEALTHY_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
