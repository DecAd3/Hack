package model.RoomModel;

public class RestRoom extends Room {
    public RestRoom(int floor, int number, int capacity) {
        super(floor, number, capacity);
    }

    @Override
    public String toString() {
        return floor + "F-" + number + "-RestRoom";
    }
}
