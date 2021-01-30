package model.RoomModel;

public class MeetingRoom extends Room {
    public MeetingRoom(int floor, int number, int capacity) {
        super(floor, number, capacity);
    }

    @Override
    public String toString() {
        return floor + "F-" + number + "-MeetingRoom";
    }
}
