package model.RoomModel;

public class Office extends Room {
    public Office(int floor, int number, int capacity) {
        super(floor, number, capacity);
    }

    @Override
    public String toString() {
        return floor + "F" + "-Office";
    }
}
