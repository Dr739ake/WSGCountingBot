package wsg.freeway.other;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static String getToken() {
        String str = getJsonString();
        JSONObject jo = new JSONObject(str);

        return (String) jo.get("token");
    }

    public static String getChannel() {
        String str = getJsonString();
        JSONObject jo = new JSONObject(str);

        return (String) jo.get("channel");
    }

    public static String getGameFile() {
        String str = getJsonString();
        JSONObject jo = new JSONObject(str);

        return (String) jo.get("gamedatafile");
    }

    public static String getAdminFile() {
        String str = getJsonString();
        JSONObject jo = new JSONObject(str);

        return (String) jo.get("adminfile");
    }

    public static String getGuild() {
        String str = getJsonString();
        JSONObject jo = new JSONObject(str);

        return (String) jo.get("guild");
    }

    private static String getJsonString() {
        Path fileName = Path.of("properties.json");
        String str;
        File file = new File("properties.json");
        if (file.exists()) {
            try {
                str = Files.readString(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
                PrintWriter printWriter = new PrintWriter(new FileWriter(file, true));
                printWriter.println("{");
                printWriter.println("\"guild\": \"default\",");
                printWriter.println("\"channel\": \"default\",");
                printWriter.println("\"token\": \"default\",");
                printWriter.println("\"adminfile\": \"admins.json\",");
                printWriter.println("\"gamedatafile\": \"gamedata.json\"");
                printWriter.println("}");
                printWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                str = Files.readString(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return str;
    }
}
