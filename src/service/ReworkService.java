package service;

import static util.Constants.LOG_PATH;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReworkService {
    private static ReworkService _instance = new ReworkService();

    public static ReworkService getInstance() {
        return _instance;
    }

    private static BufferedWriter logHistory;
    

    private ReworkService() {
        File file = new File(LOG_PATH);
        try {
            file.createNewFile();
            logHistory = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
