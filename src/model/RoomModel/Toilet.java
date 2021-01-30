package model.RoomModel;

public class Toilet extends Room {
    public Toilet(int floor, int number, int capacity) {
        super(floor, number, capacity);
    }

    @Override
    public String toString() {
        return floor + "F-" + number + "-Toilet";
    }
}
