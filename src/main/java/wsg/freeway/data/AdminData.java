package wsg.freeway.data;

import org.json.JSONArray;
import wsg.freeway.other.Config;

import java.io.*;
import java.util.Arrays;

public class AdminData {
    private String[] admins;

    public AdminData() {
        admins = new String[]{"empty"};
    }

    public static void saveObjToJSON(AdminData adminData) {

        if (adminData.admins == null) {
            adminData = new AdminData();
        }

        JSONArray jsonArray = new JSONArray(adminData.admins);
        String myJson = jsonArray.toString();

        File file = new File(Config.getAdminFile());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(myJson);
        } catch (IOException ignored) {
            // nothing
        }
    }

    public static AdminData loadObjFromJSON() {
        AdminData obj = new AdminData();

        File file = new File(Config.getAdminFile());
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
