package service;

import static util.Constants.LOG_PATH;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.Employee.WORK_STATUS;

public class ReworkService {
    private static ReworkService _instance = new ReworkService();

    public static ReworkService getInstance() {
        return _instance;
    }

    private BufferedWriter logFile;
    private Company company;
    public long WORLD_TIME = 0;
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

    public void info() throws IOException {
        int OFFICEING_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.WORKING).collect(Collectors.toList())
                .size();
        int MEETTING_AMOUNT = company.getEmployeePool().stream()
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
        logFile.write("世界时间：" + WORLD_TIME);
        logFile.write("\n");
        logFile.write("复工第" + (WORLD_TIME / 48) + "天");
        logFile.write("\n");
        logFile.write("在办公区的人数：" + OFFICEING_AMOUNT);
        logFile.write("\n");
        logFile.write("在会议室的人数：" + MEETTING_AMOUNT);
        logFile.write("\n");
        logFile.write("在茶水间的人数：" + RESTING_AMOUNT);
        logFile.write("\n");
        logFile.write("在洗手间的人数：" + TOILETING_AMOUNT);
        logFile.write("\n");
        logFile.write("被感染的人数：" + INFECTED_AMOUNT);
        logFile.write("\n");
        logFile.write("存在风险的人数：" + RISKY_AMOUNT);
        logFile.write("\n");
        logFile.write("---------");
        logFile.flush();
    }

    public void rework() throws IOException {
        // 随机指定某员工为感染者
        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            int index = random.nextInt(company.getEmployeePool().size());
            Employee root = company.getEmployeePool().get(index);
            root.setHealthyStatus(HEALTHY_STATUS.INFECTED);
            logFile.write("感染源为：" + root.getNumber() + "号员工");
            logFile.write("\n");
            logFile.flush();
        }
        // 全员入队
        company.getFloorList().forEach(f -> {
            f.getEmployeeList().forEach(e -> {
                e.setWorkStatus(WORK_STATUS.WORKING);
                queue.add(new WorkTask(e, f.getOffice()));
            });
        });
        logFile.write("开始复工");
        logFile.write("\n");
        logFile.flush();
        // 状态计算
        long taskAmount = 0;
        while (queue.size() > 0) {
            WorkTask task = null;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // tryReleaseRoom(task);
            // task.doTask();
            taskAmount++;
            if (taskAmount % 1000 == 0) {
                WORLD_TIME++;
                if (WORLD_TIME % 48 == 0) {
                    info();
                    // 加入检查机制
                    // int healthyNum = checkAll();
                    try {
                        Thread.sleep(1000);
                        // if (healthyNum == 0) {
                        // logFile.write("全军覆没，耗时：" + WORLD_TIME % 48);
                        // logFile.flush();
                        // logHistoryclose();
                        // Thread.sleep(10000000);
                        // }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
