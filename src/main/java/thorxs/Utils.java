package thorxs;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    /**
     * Writes data into a CSV file
     * @param fileName Path and name of the file
     * @param data Data to be written
     */
    public static void writeCSV(String fileName, List<String[]> data) {
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
            JOptionPane.showMessageDialog(new JDialog(), "Error while writing file:\nMessage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reads a CSV file and returns its content
     * @param fileName Path and name of the file
     * @return A String list
     */
    public static List<String[]> readCSV(String fileName) {
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
                JOptionPane.showMessageDialog(new JDialog(), "Error while reading file:\n" + fileName + "Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // If the file is not found, create it
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JDialog(), "Error: File: " + fileName + "could not be created!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Check if any data was read
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(new JDialog(), "Warning: No data was read from file:\n" + fileName, "Warning", JOptionPane.WARNING_MESSAGE);
        }

        return data;
    }

    /**
     * Reads a file into a String
     * @param fileName Path and name of the file
     * @return A String
     */
    public static String readFileToString(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();

            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(ls);
            }

            sb.deleteCharAt(sb.length() - 1);
            reader.close();

            return sb.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JDialog(), "Error while reading file to string:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }
}
