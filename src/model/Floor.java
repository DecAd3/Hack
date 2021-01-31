package model;

import static util.Constants.MEETING_ROOM_CAPACITY;
import static util.Constants.REST_ROOM_CAPACITY;
import static util.Constants.TOILET_CAPACITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import helper.Helper;
import model.RoomModel.MeetingRoom;
import model.RoomModel.Office;
import model.RoomModel.RestRoom;
import model.RoomModel.Room;
import model.RoomModel.Room.ROOM_TYPE;
import model.RoomModel.Toilet;

public class Floor {
    Random random = new Random();
    private int floorNumber;
    private List<Employee> employeeList;

    private List<Room> meetingRoomList;
    private List<Room> toiletList;
    private List<Room> restroomList;
    private Office office;

    public int getNumber() {
        return floorNumber;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<Room> getMeetingRoomList() {
        return meetingRoomList;
    }

    public List<Room> getToiletList() {
        return toiletList;
    }

    public List<Room> getRestroomList() {
        return restroomList;
    }

    public Office getOffice() {
        return office;
    }

    private void initRooms(ROOM_TYPE type, int floor, int amount) {

        switch (type) {
            case MeetingRoom:
                this.meetingRoomList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    meetingRoomList.add(new MeetingRoom(floor, i, MEETING_ROOM_CAPACITY));
                }
                break;
            case Toilet:
                this.toiletList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    toiletList.add(new Toilet(floor, i, REST_ROOM_CAPACITY));
                }
                break;
            case RestRoom:
                this.restroomList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    restroomList.add(new RestRoom(floor, i, TOILET_CAPACITY));
                }
                break;
            default:// never use default
                break;
        }
    }

    public Floor(int number, int employeeEachFloor, int meetingEachFloor, int toiletEachFloor, int restEachFloor) {

        this.floorNumber = number;
        this.employeeList = new ArrayList<>();
        // init Employee
        for (int i = 0; i < employeeEachFloor; i++) {
            employeeList.add(new Employee(number, i));
        }
        // init meeting room
        initRooms(ROOM_TYPE.MeetingRoom, number, meetingEachFloor);
        // init toilet room
        initRooms(ROOM_TYPE.Toilet, number, toiletEachFloor);
        // init rest room
        initRooms(ROOM_TYPE.RestRoom, number, restEachFloor);

        // every employee has an office
        // we only use one office stand for it in the same floor
        this.office = new Office(number, 0, 1);

    }

    public Room getFreeMeetingRoom() {
        return Helper.getFreeRoom(meetingRoomList);

    }

    public Room getFreeRestRoom() {
        return Helper.getFreeRoom(restroomList);

    }

    public Room getFreeToilet() {
        return Helper.getFreeRoom(toiletList);

    }
}
