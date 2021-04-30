package thorxs;

import lombok.Getter;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    @Getter
    private static String studentsPath;
    @Getter
    private static String takenSubjectsPath;
    @Getter
    private static String subjectsPath;
    @Getter
    private static boolean developerMode;

    /**
     * Load configurations from config.json
     */
    public static void loadConfiguration() {
        File file = new File("./config.json");

        // If the configuration file does not exists create it
        if (!file.isFile()) {
            writeDefaultConfigurationFile();
        }

        String content = Utils.readFileToString("./config.json");

        if (content == null) {
            JOptionPane.showMessageDialog(new JDialog(), "The content of the config.json file is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JSONObject jsonObject = new JSONObject(content);

        developerMode = (boolean) jsonObject.get("developerMode");
        studentsPath = (String) jsonObject.get("studentsPath");
        takenSubjectsPath = (String) jsonObject.get("takenSubjectsPath");
        subjectsPath = (String) jsonObject.get("subjectsPath");
    }

    /**
     * Writes the default configuration file
     */
    private static void writeDefaultConfigurationFile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentsPath", "data/students.csv");
        jsonObject.put("takenSubjectsPath", "data/takensubjects.csv");
        jsonObject.put("subjectsPath", "data/subjects.csv");
        jsonObject.put("developerMode", false);

        FileWriter fw = null;

        try {
            fw = new FileWriter("./config.json");
            fw.write(jsonObject.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JDialog(), "Error while writing the config.json file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fw != null) {
                    fw.flush();
                    fw.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JDialog(), "Error while writing the config.json file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
