package model;

import java.util.List;
import java.util.Random;

import model.RoomModel.MeetingRoom;
import model.RoomModel.Office;
import model.RoomModel.RestRoom;
import model.RoomModel.Toilet;

public class Floor {
    private int number;
    private List<Employee> employeeList;

    private List<MeetingRoom> meetingroomList;
    private List<Toilet> toiletList;
    private List<RestRoom> restroomList;
    private Office office;

    public int getNumber() {
        return number;
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

    public Floor(int number, int employeeEachFloor, int meetingEachFloor, int toiletEachFloor, int restEachFloor) {
        Random random = new Random();
    }
}
