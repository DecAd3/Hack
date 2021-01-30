package model;

import static util.Constants.ROOM_MAX_CAPACITY;
import static util.Constants.ROOM_MIN_CAPACITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import model.RoomModel.MeetingRoom;
import model.RoomModel.Office;
import model.RoomModel.RestRoom;
import model.RoomModel.Room.ROOM_TYPE;
import model.RoomModel.Room.STATUS;
import model.RoomModel.Toilet;

public class Floor {
    Random random = new Random();
    private int floorNumber;
    private List<Employee> employeeList;

    private List<MeetingRoom> meetingroomList;
    private List<Toilet> toiletList;
    private List<RestRoom> restroomList;
    private Office office;

    public int getNumber() {
        return floorNumber;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<MeetingRoom> getMeetingroomList() {
        return meetingroomList;
    }

    public List<Toilet> getToiletList() {
        return toiletList;
    }

    public List<RestRoom> getRestroomList() {
        return restroomList;
    }

    public Office getOffice() {
        return office;
    }

    private void initRooms(ROOM_TYPE type, int floor, int amount) {
        // generate random capacity
        int capacity = random.nextInt(ROOM_MAX_CAPACITY) % (ROOM_MAX_CAPACITY - ROOM_MIN_CAPACITY + 1)
                + ROOM_MIN_CAPACITY;
        switch (type) {
            case MeetingRoom:
                this.meetingroomList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    meetingroomList.add(new MeetingRoom(floor, i, capacity));
                }
                break;
            case Toilet:
                this.toiletList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    toiletList.add(new Toilet(floor, i, capacity));
                }
                break;
            case RestRoom:
                this.restroomList = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    restroomList.add(new RestRoom(floor, i, capacity));
                }
                break;
            default:
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

        this.office = new Office(number, 0, 1);
    }

    public MeetingRoom getFreeMeetingRoom() {
        AtomicReference<MeetingRoom> rooms = new AtomicReference<MeetingRoom>();
        meetingroomList.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
                rooms.set(m);
            }
        });
        return rooms.get();
    }

    public RestRoom getFreeRestRoom() {
        AtomicReference<RestRoom> rooms = new AtomicReference<RestRoom>();
        restroomList.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
                rooms.set(m);
            }
        });
        return rooms.get();
    }

    public Toilet getFreeToilet() {
        AtomicReference<Toilet> rooms = new AtomicReference<Toilet>();
        toiletList.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
                rooms.set(m);
            }
        });
        return rooms.get();
    }
}
