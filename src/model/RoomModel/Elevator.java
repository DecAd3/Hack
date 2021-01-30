package model.RoomModel;

public class Elevator extends Room {
    public Elevator(int floor, int number, int capacity) {
        super(floor, number, capacity);
    }

    @Override
    public String toString() {
        return number + "-Elevator ";
    }
}
