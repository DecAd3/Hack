package model;

import static util.Constants.ELEVATOR_CAPACITY;
import static util.Constants.ELEVATOR_NUM;
import static util.Constants.EMPLOYEE_NUM_EACH_FLOOR;
import static util.Constants.FLOOR_AMOUNT;
import static util.Constants.MEETINGROOM_EACH_FLOOR;
import static util.Constants.RESTROOM_EACH_FLOOR;
import static util.Constants.TOILET_EACH_FLOOR;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import model.RoomModel.Elevator;
import model.RoomModel.MeetingRoom;
import model.RoomModel.RestRoom;
import model.RoomModel.Room.STATUS;
import model.RoomModel.Toilet;

public class Company {
    private static Company _instance = new Company();

    public static Company getInstance() {
        return _instance;
    }

    private List<Floor> floorList;
    private List<MeetingRoom> meetingRoomPool;
    private List<Toilet> toiletPool;
    private List<RestRoom> restroomPool;
    private List<Employee> employeePool;
    private List<Elevator> elevatorList;

    public List<Elevator> getElevatorList() {
        return elevatorList;
    }

    public List<Floor> getFloorList() {
        return floorList;
    }

    public MeetingRoom getFreeMeetingRoomPool() {
        AtomicReference<MeetingRoom> rooms = new AtomicReference<MeetingRoom>();
        meetingRoomPool.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
            	rooms.set(m);
            }
        });
        return rooms.get();
    }

    public RestRoom getFreeRestroomPool() {
        AtomicReference<RestRoom> rooms = new AtomicReference<RestRoom>();
        restroomPool.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
            	rooms.set(m);
            }
        });
        return rooms.get();
    }

    public Toilet getFreeToiletPool() {
        AtomicReference<Toilet> rooms = new AtomicReference<Toilet>();
        toiletPool.forEach(m -> {
            if (m.getUseStatus() == STATUS.FREE
                    || (m.getUseStatus() == STATUS.IN_USE && m.getEmployeeList().size() < m.getCapacity())) {
                rooms.set(m);
            }
        });
        return rooms.get();
    }

    public Elevator getFreeElevator() {
        AtomicReference<Elevator> elevators = new AtomicReference<Elevator>();
        elevatorList.forEach(e -> {
            if (e.getUseStatus() == STATUS.FREE
                    || (e.getUseStatus() == STATUS.IN_USE && e.getEmployeeList().size() < e.getCapacity())) {
                elevators.set(e);
            }
        });
        return elevators.get();
    }

    public List<MeetingRoom> getMeetingRoomPool() {
        return meetingRoomPool;
    }

    public List<Toilet> getToiletPool() {
        return toiletPool;
    }

    public List<RestRoom> getRestRoomPool() {
        return restroomPool;
    }

    public List<Employee> getEmployeePool() {
        return employeePool;
    }

    private Company() {
        floorList = new ArrayList<>();
        for (int i = 0; i < FLOOR_AMOUNT; i++) {
            floorList.add(new Floor(i, EMPLOYEE_NUM_EACH_FLOOR, MEETINGROOM_EACH_FLOOR, TOILET_EACH_FLOOR,
                    RESTROOM_EACH_FLOOR));
        }
        meetingRoomPool = new ArrayList<>();
        toiletPool = new ArrayList<>();
        restroomPool = new ArrayList<>();
        employeePool = new ArrayList<>();
        floorList.forEach(floor -> {
            meetingRoomPool.addAll(floor.getMeetingroomList());
            toiletPool.addAll(floor.getToiletList());
            restroomPool.addAll(floor.getRestroomList());
            employeePool.addAll(floor.getEmployeeList());
        });
        this.elevatorList = new ArrayList<>();
        for (int i = 0; i < ELEVATOR_NUM; i++) {
            elevatorList.add(new Elevator(0, i, ELEVATOR_CAPACITY));
        }
    }
}
