package wsg.freeway;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.util.Properties;

public class GameData {
    private int curNum;
    private int highScore;
    private String lastUser;
    private static File file;
    private static final Properties properties = new Properties();

    public GameData() throws Exception {
        curNum = 0;
        highScore = 0;
        lastUser = "";
        properties.load(new FileInputStream("countingBot.properties"));
        file = new File(properties.getProperty("gameDataFile"));
    }

    public static void saveObjToJSON(GameData gameData) {
        JSONObject jsonObject = new JSONObject(gameData);
        String myJson = jsonObject.toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(myJson);
        } catch (IOException ignored) {
            // nothing
        }
    }

    public static @NotNull GameData loadObjFromJSON() {
        GameData obj;
        try {
            obj = new GameData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            saveObjToJSON(obj);
        } else {
            String jsonString;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                jsonString = reader.readLine();

                JSONObject jsonObject = new JSONObject(jsonString);

                obj.setCurNum((Integer) jsonObject.get("curNum"));
                obj.setHighScore((Integer) jsonObject.get("highScore"));
                obj.setLastUser((String) jsonObject.get("lastUser"));
            } catch (IOException e) {
                // nothing
            }
        }
        return obj;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "curNum=" + curNum +
                ", highScore=" + highScore +
                ", lastUser='" + lastUser + '\'' +
                '}';
    }
}
