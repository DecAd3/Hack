package model;

import static util.Constants.ELEVATOR_AMOUNT;
import static util.Constants.ELEVATOR_CAPACITY;
import static util.Constants.EMPLOYEE_IN_EACH_FLOOR;
import static util.Constants.FLOOR_AMOUNT;
import static util.Constants.MEETING_ROOM_IN_EACH_FLOOR;
import static util.Constants.RESTROOM_IN_EACH_FLOOR;
import static util.Constants.TOILET_IN_EACH_FLOOR;

import java.util.ArrayList;
import java.util.List;

import helper.Helper;
import model.RoomModel.Elevator;
import model.RoomModel.Room;

public class Company {
    private static Company _instance = new Company();

    public static Company getInstance() {
        return _instance;
    }

    private List<Floor> floorList;
    private List<Room> meetingRoomPool;
    private List<Room> toiletPool;
    private List<Room> restroomPool;
    private List<Employee> employeePool;
    private List<Room> elevatorList;

    public List<Room> getElevatorList() {
        return elevatorList;
    }

    public List<Floor> getFloorList() {
        return floorList;
    }


    public Room getFreeMeetingRoomPool() {
        return Helper.getFreeRoom(meetingRoomPool);
    }

    public Room getFreeRestroomPool() {
        return Helper.getFreeRoom(restroomPool);
    }

    public Room getFreeToiletPool() {
        return Helper.getFreeRoom(toiletPool);

    }

    public Room getFreeElevator() {
        return Helper.getFreeRoom(elevatorList);

    }

    public List<Room> getMeetingRoomPool() {
        return meetingRoomPool;
    }

    public List<Room> getToiletPool() {
        return toiletPool;
    }

    public List<Room> getRestRoomPool() {
        return restroomPool;
    }

    public List<Employee> getEmployeePool() {
        return employeePool;
    }

    private Company() {
        floorList = new ArrayList<>();
        for (int i = 0; i < FLOOR_AMOUNT; i++) {
            floorList.add(new Floor(i, EMPLOYEE_IN_EACH_FLOOR, MEETING_ROOM_IN_EACH_FLOOR, TOILET_IN_EACH_FLOOR,
                    RESTROOM_IN_EACH_FLOOR));
        }
        meetingRoomPool = new ArrayList<>();
        toiletPool = new ArrayList<>();
        restroomPool = new ArrayList<>();
        employeePool = new ArrayList<>();
        floorList.forEach(floor -> {
            meetingRoomPool.addAll(floor.getMeetingRoomList());
            toiletPool.addAll(floor.getToiletList());
            restroomPool.addAll(floor.getRestroomList());
            employeePool.addAll(floor.getEmployeeList());
        });
        this.elevatorList = new ArrayList<>();
        for (int i = 0; i < ELEVATOR_AMOUNT; i++) {
            elevatorList.add(new Elevator(0, i, ELEVATOR_CAPACITY));
        }
    }
}
