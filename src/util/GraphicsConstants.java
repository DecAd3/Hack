package util;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttrDecls;

import java.util.ArrayList;
import java.util.List;

public class GraphicsConstants {

    public static int PANEL_WIDTH = 1200;
    public static int PANEL_HEIGHT = 1000;
    public static int EACH_FLOOR_HEIGHT = 100;
    public static int EACH_FLOOR_WIDTH = 1000;
    public static int EACH_BAY_HEIGHT = 10;
    public static int EACH_BAY_WIDTH = 10;
    public static int EACH_EMPTY_HEIGHT = 35;
    public static int EACH_EMPTY_WIDTH = 10;
    public static int FLOOR_NUM = 10;
    public static int BAY_EACH_FLOOR_NUM = 100;

    public static List<Floor> floors;

    static {
        floors = new ArrayList<>();
        for (int i = 0; i < FLOOR_NUM; i++) {
            Floor floor = new Floor(0, EACH_FLOOR_WIDTH, (i + 1) * EACH_FLOOR_HEIGHT);
            List<WorkBay> workBays = new ArrayList<>();
            // 行
            int num = 0;
            for (int j = 0; j < EACH_FLOOR_HEIGHT / (EACH_BAY_HEIGHT + EACH_EMPTY_HEIGHT); j++) {
                // 列
                for (int k = 0; k < EACH_FLOOR_WIDTH / (EACH_BAY_WIDTH + EACH_EMPTY_WIDTH); k++) {
                    int x1 = EACH_EMPTY_WIDTH * (k + 1) + EACH_BAY_WIDTH * k;
                    int x2 = (EACH_EMPTY_WIDTH + EACH_BAY_WIDTH) * (k + 1);
                    int y1 = i * EACH_FLOOR_HEIGHT + (EACH_EMPTY_HEIGHT * (j + 1) + EACH_BAY_HEIGHT * j);
                    int y2 = i * EACH_FLOOR_HEIGHT + ((EACH_EMPTY_HEIGHT + EACH_BAY_HEIGHT) * (j + 1));
                    WorkBay workBay = new WorkBay(i + "-" + num, x1, x2, y1, y2);
                    workBays.add(workBay);
                    num++;
                }
            }
            floor.setWorkBayList(workBays);
            floors.add(floor);
        }
    }

    public static class WorkBay {
        private String number;
        private int x1;
        private int x2;
        private int y1;
        private int y2;

        public WorkBay(String number, int x1, int x2, int y1, int y2) {
            this.number = number;
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        public String getNumber() {
            return number;
        }

        public int getX1() {
            return x1;
        }

        public int getX2() {
            return x2;
        }

        public int getY1() {
            return y1;
        }

        public int getY2() {
            return y2;
        }
    }

    public static class Floor {
        int x1;
        int x2;
        int y1;
        List<WorkBay> workBayList;

        public Floor(int x1, int x2, int y1) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
        }

        public void setWorkBayList(List<WorkBay> workBayList) {
            this.workBayList = workBayList;
        }

        public int getX1() {
            return x1;
        }

        public int getX2() {
            return x2;
        }

        public int getY1() {
            return y1;
        }

        public List<WorkBay> getWorkBayList() {
            return workBayList;
        }
    }

}
