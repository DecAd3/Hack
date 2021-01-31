package util;

public class Constants {
    // The risk of infection
    public final static double P_INFECT = 0.1;
    // The possibility of found out
    public final static double P_ISOLATED = 0.15;

    // one loop = 10 min
    public final static double P_MEETING = 0.006; // twice a week
    public final static double P_RESTING = 0.03125; // three times a day
    public final static double P_PEEING = 0.03125; // three times a day
    public final static double P_LIFTING = 0.02083; // twice a day

    // the durations
    public final static int D_MEETING = 6;
    public final static int D_REST = 3;
    public final static int D_PEE = 1;
    public final static int D_LIFT = 1;

    // Constants
    public final static int MEETING_ROOM_CAPACITY = 8;
    public final static int REST_ROOM_CAPACITY = 8;
    public final static int TOILET_CAPACITY = 4;
    public final static int ELEVATOR_AMOUNT = 4;
    public final static int ELEVATOR_CAPACITY = 10;
    public final static int FLOOR_AMOUNT = 10;

    // Room Constants
    public final static int EMPLOYEE_IN_EACH_FLOOR = 200;
    public final static int MEETING_ROOM_IN_EACH_FLOOR = 6;
    public final static int TOILET_IN_EACH_FLOOR = 2;
    public final static int RESTROOM_IN_EACH_FLOOR = 3;

    // log file
    public final static String LOG_PATH = "../log.txt";
}
