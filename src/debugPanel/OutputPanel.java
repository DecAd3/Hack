package debugPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.Employee.WORK_STATUS;
import service.ReworkService;

public class OutputPanel implements Runnable {
    private static Company company = Company.getInstance();
    private long WORLD_TIME = ReworkService.getInstance().getWorldTime();

    public void data_info() {
        WORLD_TIME = ReworkService.getInstance().getWorldTime();
        // Summary
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
        int ELEVATOR_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.LIFTING).collect(Collectors.toList())
                .size();
        int INFECTED_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED).collect(Collectors.toList())
                .size();
        int HEALTHY_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.HEALTHY).collect(Collectors.toList())
                .size();
        int RISKY_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.RISKY).collect(Collectors.toList())
                .size();
        int ISOLATED_AMOUNT = company.getEmployeePool().stream()
                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.ISOLATED).collect(Collectors.toList())
                .size();

        List<Employee> allEmployee = company.getEmployeePool();
        List<Integer> statusArray = new ArrayList<>();
        for (int i = 0; i < allEmployee.size(); i++) {
            int status = allEmployee.get(i).getHealthyStatus().getValue();
            statusArray.add(status);
        }

    }

    public java.util.Timer timer = new java.util.Timer();

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            data_info();
        }
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 0, 100);
    }
}
