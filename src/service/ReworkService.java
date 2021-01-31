package service;

import static util.Constants.D_LIFT;
import static util.Constants.D_MEETING;
import static util.Constants.D_PEE;
import static util.Constants.D_REST;
import static util.Constants.LOG_PATH;
import static util.Constants.P_ISOLATED;

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
import model.RoomModel.Room.STATUS;

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
        logFile.write("World Time :" + WORLD_TIME);
        logFile.write("\n");
        logFile.write("DAY " + (WORLD_TIME / 48));
        logFile.write("\n");
        logFile.write("Office :" + OFFICE_AMOUNT);
        logFile.write("\n");
        logFile.write("Meeting Room:" + MEETING_AMOUNT);
        logFile.write("\n");
        logFile.write("Rest Room" + RESTING_AMOUNT);
        logFile.write("\n");
        logFile.write("Toilet :" + TOILETING_AMOUNT);
        logFile.write("\n");
        logFile.write("INFECTED :" + INFECTED_AMOUNT);
        logFile.write("\n");
        logFile.write("RISKY :" + RISKY_AMOUNT);
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
            if (employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED && d < P_ISOLATED) {
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
                if (task.getActionRoom().getUseDuration() > D_MEETING) {
                    needRelease = true;
                }
                break;
            case RESTING:
                if (task.getActionRoom().getUseDuration() > D_REST) {
                    needRelease = true;
                }
                break;
            case PEEING:
                if (task.getActionRoom().getUseDuration() > D_PEE) {
                    needRelease = true;
                }
                break;
            case LIFTING:
                if (task.getActionRoom().getUseDuration() > D_LIFT) {
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
            logFile.write("The root is" + root.toString() + " Employee");
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

                    // int healthyNum = checkAll();
                    try {
                        Thread.sleep(1000);
                        if ((WORLD_TIME / 6 / 8) == 30) {

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
