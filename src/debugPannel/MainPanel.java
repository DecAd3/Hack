package debugPannel;

import static util.GraphicsConstants.EACH_FLOOR_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import model.Company;
import model.Employee;
import model.Employee.HEALTHY_STATUS;
import model.Employee.WORK_STATUS;
import service.ReworkService;
import util.GraphicsConstants;

public class MainPanel extends JPanel implements Runnable {

        private static Company company = Company.getInstance();
        private long WORLD_TIME = ReworkService.getInstance().getWorldTime();

        public void info() {
                int OFFICEING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.WORKING)
                                .collect(Collectors.toList()).size();
                int MEETTING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.MEETING)
                                .collect(Collectors.toList()).size();
                int RESTING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.RESTING)
                                .collect(Collectors.toList()).size();
                int TOILETING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.PEEING)
                                .collect(Collectors.toList()).size();
                int INFECTED_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED)
                                .collect(Collectors.toList()).size();
                int RISKY_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.RISKY)
                                .collect(Collectors.toList()).size();
                System.out.println("---------");
                System.out.println("世界时间：" + WORLD_TIME);
                System.out.println("复工第：" + (WORLD_TIME / 6 / 8) + "天");
                System.out.println("在办公区的人数：" + OFFICEING_AMOUNT);
                System.out.println("在会议室的人数：" + MEETTING_AMOUNT);
                System.out.println("在茶水间的人数：" + RESTING_AMOUNT);
                System.out.println("在洗手间的人数：" + TOILETING_AMOUNT);
                System.out.println("被感染的人数：" + INFECTED_AMOUNT);
                System.out.println("存在风险的人数：" + RISKY_AMOUNT);
                System.out.println("---------");
        }

        public MainPanel() {
                super();
                this.setBackground(new Color(0xFFFAFA));
        }

        @Override
        public void paint(Graphics g) {
                super.paint(g);
                Map<Integer, Employee.HEALTHY_STATUS> employeeMap = company.getEmployeePool().stream()
                                .collect(Collectors.toMap(Employee::getNumber, Employee::getHealthyStatus));
                for (GraphicsConstants.Floor f : GraphicsConstants.floors) {
                        g.setColor(Color.black);
                        g.drawLine(f.getX1(), f.getY1(), f.getX2(), f.getY1());
                        for (GraphicsConstants.WorkBay bay : f.getWorkBayList()) {
                                if (employeeMap.get(bay.getNumber()) == HEALTHY_STATUS.HEALTHY) {
                                        g.setColor(new Color(0x00ff00));
                                } else if (employeeMap.get(bay.getNumber()) == HEALTHY_STATUS.RISKY) {
                                        g.setColor(new Color(0x0000ff));
                                } else if (employeeMap.get(bay.getNumber()) == HEALTHY_STATUS.INFECTED) {
                                        g.setColor(new Color(0xff0000));
                                } else if (employeeMap.get(bay.getNumber()) == HEALTHY_STATUS.ISOLATED) {
                                        g.setColor(new Color(0x000000));
                                }
                                g.fillRect(bay.getX1(), bay.getY1(), bay.getX2() - bay.getX1(),
                                                bay.getY2() - bay.getY1());
                        }
                }
                int OFFICEING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.WORKING)
                                .collect(Collectors.toList()).size();
                int MEETTING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.MEETING)
                                .collect(Collectors.toList()).size();
                int RESTING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.RESTING)
                                .collect(Collectors.toList()).size();
                int TOILETING_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.PEEING)
                                .collect(Collectors.toList()).size();
                int ELEVATOR_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getWorkStatus() == WORK_STATUS.LIFTING)
                                .collect(Collectors.toList()).size();
                int INFECTED_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.INFECTED)
                                .collect(Collectors.toList()).size();
                int HEALTHY_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.HEALTHY)
                                .collect(Collectors.toList()).size();
                int RISKY_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.RISKY)
                                .collect(Collectors.toList()).size();
                int ISOLATED_AMOUNT = company.getEmployeePool().stream()
                                .filter(employee -> employee.getHealthyStatus() == HEALTHY_STATUS.ISOLATED)
                                .collect(Collectors.toList()).size();
                Font font = new Font("黑体", Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(new Color(0xff0000));
                g.drawString("复工第" + (WORLD_TIME / 6 / 8) + "天", EACH_FLOOR_WIDTH + 30, 50);
                Font f3 = new Font("宋体", Font.PLAIN, 15);
                g.setFont(f3);
                g.setColor(new Color(0x99004c));
                g.drawString("在办公区的人数：" + OFFICEING_AMOUNT, EACH_FLOOR_WIDTH + 30, 100);
                g.drawString("在会议室的人数：" + MEETTING_AMOUNT, EACH_FLOOR_WIDTH + 30, 150);
                g.drawString("在茶水间的人数：" + RESTING_AMOUNT, EACH_FLOOR_WIDTH + 30, 200);
                g.drawString("在洗手间的人数：" + TOILETING_AMOUNT, EACH_FLOOR_WIDTH + 30, 250);
                g.drawString("在电梯的人数：" + ELEVATOR_AMOUNT, EACH_FLOOR_WIDTH + 30, 300);
                g.setColor(new Color(0x00ff00));
                g.drawString("健康的人数：" + HEALTHY_AMOUNT, EACH_FLOOR_WIDTH + 30, 350);
                g.setColor(new Color(0x0000ff));
                g.drawString("存在风险的人数：" + RISKY_AMOUNT, EACH_FLOOR_WIDTH + 30, 400);
                g.setColor(new Color(0xff0000));
                g.drawString("被感染的人数：" + INFECTED_AMOUNT, EACH_FLOOR_WIDTH + 30, 450);
                g.setColor(new Color(0x000000));
                g.drawString("被隔离的人数：" + ISOLATED_AMOUNT, EACH_FLOOR_WIDTH + 30, 500);
        }

        public java.util.Timer timer = new java.util.Timer();

        class MyTimerTask extends TimerTask {
                @Override
                public void run() {
                        MainPanel.this.repaint();
                }
        }

        @Override
        public void run() {
                timer.schedule(new MyTimerTask(), 0, 100);
        }
}
