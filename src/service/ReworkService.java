package service;

import static util.Constants.FIND_OUT_POSSIBILITY;
import static util.Constants.LIFTING_POSSIBILITY;
import static util.Constants.LIFT_DURATION;
import static util.Constants.LOG_PATH;
import static util.Constants.MEETING_DURATION;
import static util.Constants.MEETING_POSSIBILITY;
import static util.Constants.PEEING_POSSIBILITY;
import static util.Constants.PEE_DURATION;
import static util.Constants.RESTING_POSSIBILITY;
import static util.Constants.REST_DURATION;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.Employee.WORK_STATUS;
import model.Floor;
import model.RoomModel.Elevator;
import model.RoomModel.MeetingRoom;
import model.RoomModel.RestRoom;
import model.RoomModel.Room;
import model.RoomModel.Room.STATUS;
import model.RoomModel.Toilet;

public class ReworkService {
    private static ReworkService _instance = new ReworkService();

    public static ReworkService getInstance() {
        return _instance;
    }

    private BufferedWriter logFile;
    private Company company;
    private long WORLD_TIME = 0;
    private LinkedBlockingQueue<WorkTask> queue = new LinkedBlockingQueue<WorkTask>(30000);

    private ReworkService() {
        File file = new File(LOG_PATH);
        company = Company.getInstance();

        try {
            file.createNewFile();
            logFile = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedBlockingQueue<WorkTask> getQueue() {
        return queue;
    }

    public long getWorldTime() {
        return WORLD_TIME;
    }

    public BufferedWriter getBufferWritter() {
        return logFile;
    }

    public void info() throws IOException {
        int OFFICE_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.WORKING).collect(Collectors.toList())
                .size();
        int MEETING_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.MEETING).collect(Collectors.toList())
                .size();
        int RESTING_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.RESTING).collect(Collectors.toList())
                .size();
        int TOILETING_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.PEEING).collect(Collectors.toList()).size();
        int INFECTED_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED).collect(Collectors.toList())
                .size();
        int RISKY_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.RISKY).collect(Collectors.toList())
                .size();
        logFile.write("---------");
        logFile.write("\n");
        logFile.write("World Timeï¼š" + WORLD_TIME);
        logFile.write("\n");
        logFile.write("DAY " + (WORLD_TIME / 48));
        logFile.write("\n");
        logFile.write("Officeï¼š" + OFFICE_AMOUNT);
        logFile.write("\n");
        logFile.write("Meeting Roomï¼š" + MEETING_AMOUNT);
        logFile.write("\n");
        logFile.write("Rest Roomï¼š" + RESTING_AMOUNT);
        logFile.write("\n");
        logFile.write("Toiletï¼š" + TOILETING_AMOUNT);
        logFile.write("\n");
        logFile.write("INFECTEDï¼š" + INFECTED_AMOUNT);
        logFile.write("\n");
        logFile.write("RISKYï¼š" + RISKY_AMOUNT);
        logFile.write("\n");
        logFile.write("---------");
        logFile.flush();
    }

    private int checkAll() throws IOException {
        logFile.write("---Begin Daily Check---");
        logFile.write("\n");
        logFile.flush();
        AtomicInteger ret = new AtomicInteger();
        company.getEmployeePool().forEach(employee -> {
            if (employee.getHealthyStatus() == HEALTHY_STATUS.ISOLATED)
                return;
            double d = Math.random();
            if (employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED && d < FIND_OUT_POSSIBILITY) {
                try {
                    logFile.write(employee.toString() + " Employee is infected");
                    logFile.write("\n");
                    logFile.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                employee.setHealthyStatus(HEALTHY_STATUS.ISOLATED);
            }
            if (employee.getHealthyStatus() == HEALTHY_STATUS.HEALTHY) {
                ret.getAndIncrement();
            }
        });
        logFile.write("---End Check---");
        logFile.write("\n");
        logFile.flush();
        return ret.get();
    }

    synchronized private void tryReleaseRoom(WorkTask task) throws IOException {
        boolean needRelease = false;
        switch (task.getEmployee().getWorkStatus()) {
            case WORKING:
                break;
            case MEETING:
                if (task.getActionRoom().getUseDuration() > MEETING_DURATION) {
                    needRelease = true;
                }
                break;
            case RESTING:
                if (task.getActionRoom().getUseDuration() > REST_DURATION) {
                    needRelease = true;
                }
                break;
            case PEEING:
                if (task.getActionRoom().getUseDuration() > PEE_DURATION) {
                    needRelease = true;
                }
                break;
            case LIFTING:
                if (task.getActionRoom().getUseDuration() > LIFT_DURATION) {
                    needRelease = true;
                }
                break;
            default:
                break;
        }
        if (needRelease) {

            task.getActionRoom().setUseDuration(0);
            task.getActionRoom().setUseStatus(STATUS.FREE);

            String employeeNum = "";
            List<Employee> list = task.getActionRoom().getEmployeeList();
            try {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setWorkStatus(WORK_STATUS.WORKING);
                    employeeNum = employeeNum + list.get(i).toString() + ",";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!"".equals(employeeNum)) {
                logFile.write(employeeNum + "Exit room " + task.getActionRoom().toString() + " back to office.");
                logFile.write("\n");
                logFile.flush();
            }
            task.getActionRoom().getEmployeeList().clear();

            task.setActionRoom(company.getFloorList().get(task.getEmployee().getFloor()).getOffice());
            task.getEmployee().setWorkStatus(WORK_STATUS.WORKING);
        }
        task.getActionRoom().setUseDuration(task.getActionRoom().getUseDuration() + 1);
    }

    private Room switchStatus(Employee e, Room room) {
        if (e.getWorkStatus() == WORK_STATUS.WORKING) {

            WORK_STATUS nextStatus = WORK_STATUS.WORKING;
            double d = Math.random();
            if (d <= MEETING_POSSIBILITY) {
                nextStatus = WORK_STATUS.MEETING;
            } else if (d > MEETING_POSSIBILITY && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.RESTING;
            } else if (d > (MEETING_POSSIBILITY + RESTING_POSSIBILITY)
                    && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.PEEING;
            } else if (d > (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY)
                    && d <= (MEETING_POSSIBILITY + RESTING_POSSIBILITY + PEEING_POSSIBILITY + LIFTING_POSSIBILITY)) {
                nextStatus = WORK_STATUS.LIFTING;
            }
            Floor floor = company.getFloorList().get(e.getFloor());

            switch (nextStatus) {
                case MEETING:
                    MeetingRoom freeMeetingRoom = floor.getFreeMeetingRoom();
                    if (freeMeetingRoom == null) {
                        freeMeetingRoom = company.getFreeMeetingRoomPool();
                        if (freeMeetingRoom == null) {

                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeMeetingRoom.join(e);
                    return freeMeetingRoom;
                case RESTING:
                    RestRoom freeRestroom = floor.getFreeRestRoom();
                    if (freeRestroom == null) {
                        freeRestroom = company.getFreeRestroomPool();
                        if (freeRestroom == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeRestroom.join(e);
                    return freeRestroom;
                case PEEING:
                    Toilet freeToilet = floor.getFreeToilet();
                    if (freeToilet == null) {
                        freeToilet = company.getFreeToiletPool();
                        if (freeToilet == null) {
                            return company.getFloorList().get(e.getFloor()).getOffice();
                        }
                    }
                    freeToilet.join(e);
                    return freeToilet;
                case LIFTING:
                    Elevator freeElevator = company.getFreeElevator();
                    if (freeElevator == null) {
                        return company.getFloorList().get(e.getFloor()).getOffice();
                    }
                    freeElevator.join(e);
                    return freeElevator;
                default:
                    return company.getFloorList().get(e.getFloor()).getOffice();
            }
        } else if (e.getWorkStatus() == WORK_STATUS.HOMING) {

            e.setWorkStatus(WORK_STATUS.WORKING);
            return company.getFloorList().get(e.getFloor()).getOffice();
        } else {

            return room;
        }
    }

    public void rework(int begin) throws IOException {
        // randomly pick some employees to be the roots
        Random random = new Random();
        for (int i = 0; i < begin; i++) {
            int index = random.nextInt(company.getEmployeePool().size());
            Employee root = company.getEmployeePool().get(index);
            while (root.getHealthyStatus() == HEALTHY_STATUS.INFECTED) {
                index = random.nextInt(company.getEmployeePool().size());
                root = company.getEmployeePool().get(index);
            }
            root.setHealthyStatus(HEALTHY_STATUS.INFECTED);
            logFile.write("The root isï¼š" + root.toString() + " Employee");
            logFile.write("\n");
            logFile.flush();
        }

        company.getFloorList().forEach(f -> {
            f.getEmployeeList().forEach(e -> {
                e.setWorkStatus(WORK_STATUS.WORKING);
                queue.add(new WorkTask(e, f.getOffice()));
            });
        });
        logFile.write("Begin to rework");
        logFile.write("\n");
        logFile.flush();

        long taskAmount = 0;
        while (queue.size() > 0) {
            WorkTask task = null;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryReleaseRoom(task);
            task.doTask();
            taskAmount++;
            if (taskAmount % 1000 == 0) {
                WORLD_TIME++;
                if (WORLD_TIME % 48 == 0) {
                    info();

                    int healthyNum = checkAll();
                    try {
                        Thread.sleep(1000);
                        if (healthyNum == 0) {
                            logFile.write("ALL INFECTED, TIME: " + WORLD_TIME % 48);
                            logFile.flush();

                            Thread.sleep(10000000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }   

        }
    }
}
