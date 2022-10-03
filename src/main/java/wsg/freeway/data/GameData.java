package wsg.freeway.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import wsg.freeway.other.Config;

import java.io.*;

public class GameData {
    private int curNum;
    private int nextNum;
    private int highScore;
    private String lastUser;

    public GameData() {
        curNum = 0;
        nextNum = 1;
        highScore = 0;
        lastUser = "";
    }

    public static void saveObjToJSON(GameData gameData) {
        JSONObject jsonObject = new JSONObject(gameData);
        String myJson = jsonObject.toString();

        File file = new File(Config.getGameFile());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(myJson);
        } catch (IOException ignored) {
            // nothing
        }
    }

    public static @NotNull GameData loadObjFromJSON() {
        GameData obj = new GameData();

        File file = new File(Config.getGameFile());
        if (!file.exists()) {
            saveObjToJSON(obj);
        } else {
            String jsonString;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                jsonString = reader.readLine();

                JSONObject jsonObject = new JSONObject(jsonString);

                obj.setCurNum((Integer) jsonObject.get("curNum"));
                obj.setNextNum((Integer) jsonObject.get("nextNum"));
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

    public int getNextNum() {
        return nextNum;
    }

    public void setNextNum(int nextNum) {
        this.nextNum = nextNum;
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
                ", nextNum=" + nextNum +
                ", highScore=" + highScore +
                ", lastUser='" + lastUser + '\'' +
                '}';
    }
}
