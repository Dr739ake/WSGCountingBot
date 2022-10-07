package wsg.freeway;

import org.json.JSONArray;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class AdminData {
    private String[] admins;
    private static File file;
    private static final Properties properties = new Properties();

    public AdminData() throws Exception {
        admins = new String[]{"empty"};
        properties.load(new FileInputStream("countingBot.properties"));
        file = new File(properties.getProperty("adminFile"));
    }

    public static void saveObjToJSON(AdminData adminData) {

        if (adminData.admins == null) {
            try {
                adminData = new AdminData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        JSONArray jsonArray = new JSONArray(adminData.admins);
        String myJson = jsonArray.toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(myJson);
        } catch (IOException ignored) {
            // nothing
        }
    }

    public static AdminData loadObjFromJSON() {
        AdminData obj;
        try {
            obj = new AdminData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            saveObjToJSON(obj);
        } else {
            String jsonString;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                jsonString = reader.readLine();
                JSONArray jsonArray = new JSONArray(jsonString);
                String[] arr = new String[jsonArray.length()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = jsonArray.optString(i);
                }
                obj.admins = arr;
            } catch (IOException e) {
                // nothing
            }
        }
        return obj;
    }

    @Override
    public String toString() {
        return "AdminData{" +
                "admins=" + Arrays.toString(admins) +
                '}';
    }

    public String[] getAdmins() {
        return admins;
    }
}
