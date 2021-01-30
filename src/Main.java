import javax.swing.JFrame;

import debugPannel.MainPanel;
import service.ReworkService;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        initFrame();
        rework();
    }

    private static void initFrame() {
        MainPanel p = new MainPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(1200, 1200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("aaa");
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
