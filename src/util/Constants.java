package util;

public class Constants {
    // The risk of infection
    public final static double INFECT_POSSIBILITY = 0.1;
    // One cycle equals one unit time, assuming that one unit time equals 10 minutes in the real world
    public final static double MEETING_POSSIBILITY = 0.006; // twice a week
    public final static double RESTING_POSSIBILITY = 0.03125; // three times a day
    public final static double PEEING_POSSIBILITY = 0.03125; // three times a day
    public final static double LIFTING_POSSIBILITY = 0.02083; // twice a day
    // The different unit of time period that different states lasts
    public final static int MEETING_DURATION = 6;
    public final static int REST_DURATION = 3;
    public final static int PEE_DURATION = 1;
    public final static int LIFT_DURATION = 1;
    // Basic constants
    public final static int ROOM_MAX_CAPACITY = 10;
    public final static int ROOM_MIN_CAPACITY = 3;
    public final static int ELEVATOR_NUM = 4;
    public final static int ELEVATOR_CAPACITY = 10;
    public final static int FLOOR_AMOUNT = 10;
    public final static int EMPLOYEE_NUM_EACH_FLOOR = 200;
    public final static int MEETINGROOM_EACH_FLOOR = 6;
    public final static int TOILET_EACH_FLOOR = 2;
    public final static int RESTROOM_EACH_FLOOR = 3;
    public final static double FIND_OUT_POSSIBILITY = 0.15;
    // Log path
    public final static String LOG_PATH = "../log.txt";
}
