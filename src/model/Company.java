package model;

import java.util.List;

import model.RoomModel.Elevator;
import model.RoomModel.MeetingRoom;
import model.RoomModel.RestRoom;
import model.RoomModel.Toilet;

public class Company {
    private static Company _instance = new Company();

    public static Company getInstance() {
        return _instance;
    }

    static List<Floor> floorList;
    static List<MeetingRoom> meetingroomPool;
    static List<Toilet> toiletPool;
    static List<RestRoom> restroomPool;
    static List<Employee> employeePool;
    static List<Elevator> elevatorList;

    private Company() {
    }
}
