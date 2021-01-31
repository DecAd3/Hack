import javax.swing.JFrame;

import debugPanel.MainPanel;
import service.ReworkService;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        initFrame();
        rework();
    }

    private static void initFrame() {
        JFrame frame = new JFrame();
        MainPanel p = new MainPanel();
        Thread panelThread = new Thread(p);

        frame.add(p);
        frame.setSize(1200, 1200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("COVID-19 Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
    }

    private static void rework() {
        ReworkService reworkService = ReworkService.getInstance();
        try {
            reworkService.rework(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
