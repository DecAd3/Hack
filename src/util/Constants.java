package util;

public class Constants {
    // 感染风险
    public final static double INFECT_POSSIBILITY = 0.1;
    // 循环1次为1个单位时间，假设一个单位时间等于现实世界的10m
    public final static double MEETING_POSSIBILITY = 0.006; // 一周两次
    public final static double RESTING_POSSIBILITY = 0.03125; // 一天三次
    public final static double PEEING_POSSIBILITY = 0.03125; // 一天三次
    public final static double LIFTING_POSSIBILITY = 0.02083; // 一天两次
    // 不同状态持续的单位时间
    public final static int MEETING_DURATION = 6;
    public final static int REST_DURATION = 3;
    public final static int PEE_DURATION = 1;
    public final static int LIFT_DURATION = 1;
    // 基本常量
    public final static int ROOM_MAX_CAPACITY = 10;
    public final static int ROOM_MIN_CAPACITY = 3;
    public final static int ELEVATOR_NUM = 4;
    public final static int ELEVATOR_CAPACITY = 10;
    public final static int FLOOR_AMOUNT = 10;
    public final static int EMPLOYEE_NUM_EACH_FLOOR = 200;
    public final static int MEETINGROOM_EACH_FLOOR = 6;
    public final static int TOILET_EACH_FLOOR = 2;
    public final static int RESTROOM_EACH_FLOOR = 3;
    public final static double FIND_OUT_POSSIBILITY = 0.4;
    // 日志路径
    public final static String LOG_PATH = "../log.txt";
}
