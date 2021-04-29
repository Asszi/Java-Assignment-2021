package thorxs;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    /*
    * TODO: Make sure the file reading and writing can be done inside a jar to make the application more compact (resource access)
    */

    /**
     * Writes data into a CSV file
     * @param fileName Path and name of the file
     * @param data Data to be written
     */
    public static void writeCSV(String fileName, List<String[]> data) {
        JFrame frame = new JFrame();

        try {
            FileWriter writer = new FileWriter(fileName);

            for (String[] row : data) {
                // Remove brackets and change , to ;
                writer.append(Arrays.toString(row).replace("[", "").replace("]", "").replace(",", ";"));
                writer.append("\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error while writing file:\nMessage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reads a CSV file and returns its content
     * @param fileName Path and name of the file
     * @return A string list
     */
    public static List<String[]> readCSV(String fileName) {
        JFrame frame = new JFrame();
        List<String[]> data = new ArrayList<>();

        // Check if the file exists
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));

                String row = null;
                while ((row = br.readLine()) != null) {
                    String[] temp = row.split(";");
                    data.add(temp);
                }

                br.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error while reading file:\n" + fileName + "Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Error: File not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Check if any data was read
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Warning: No data was read from file:\n" + fileName, "Warning", JOptionPane.WARNING_MESSAGE);
        }

        return data;
    }
}
