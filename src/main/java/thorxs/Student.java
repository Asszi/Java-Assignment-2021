package thorxs;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student {
    public static final int STUDENT_COLUMNS = 4;

    @Getter
    private final int ID;
    @Getter
    private final String neptuneID;
    @Getter @Setter
    private String name;
    @Getter
    private LocalDate dateOfBirth; // TODO: Add date of birth to JTable, data file, loading, saving and error checking

    @Getter @Setter
    private List<Integer> courseList;

    public Student(int ID, String neptuneID, String name, LocalDate dateOfBirth) {
        this.ID = ID;
        this.neptuneID = neptuneID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.courseList = new ArrayList<>();
    }

    /**
     * Load the students into a list
     * @return Student list
     */
    public static List<Student> loadStudents() {
        List<String[]> data = Utils.readCSV("src/main/resources/students.csv");
        List<Student> students = new ArrayList<>();

        List<String[]> errors = new ArrayList<>();

        boolean errorFound = false;

        // Load the data of the students
        for (String[] dataLine : data) {
            boolean shouldLoad = true;

            // Check data integrity
            if (dataLine.length == STUDENT_COLUMNS) {
                // Check if any fields are empty
                for (int j = 0; j < STUDENT_COLUMNS; j++) {
                    if (dataLine[j].equals("")) {
                        errorFound = true;
                        shouldLoad = false;
                        break;
                    }
                }

                // Check if the data should load
                if (shouldLoad) {
                    students.add(new Student(
                            Integer.parseInt(dataLine[0]),
                            dataLine[1],
                            dataLine[2],
                            LocalDate.parse(dataLine[3])));
                } else {
                    errors.add(dataLine);
                }
            } else {
                errorFound = true;
                errors.add(dataLine);
            }
        }

        // Display warning message and write incomplete rows to file
        if (errorFound) {
            JOptionPane.showMessageDialog(new JFrame(), "Warning: Incomplete line(s) of student data found!\nIncomplete lines have been copied to the logs for review!", "Warning", JOptionPane.WARNING_MESSAGE);
            Utils.writeCSV("src/main/resources/student-load.log", errors);
        }

        List<String[]> sub = Utils.readCSV("src/main/resources/takensubjects.csv");

        // TODO: Error checking
        // Load the taken courses
        for (Student student : students) {
            int id = student.getID();

            for (String[] strings : sub) {
                if (Integer.parseInt(strings[0]) == id) {
                    student.courseList.add(Integer.parseInt(strings[1]));
                }
            }
        }

        return students;
    }

    /**
     * Saves the students to file
     * @param students List of Student objects
     */
    public static void saveStudents(List<Student> students) {
        List<String[]> data = new ArrayList<>();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        for (Student student : students) {
            data.add(new String[] {
                student.getID() + ";"
                + student.getNeptuneID() + ";"
                + student.getName() + ";"
                + f.format(student.getDateOfBirth())
            });
        }

        // TODO: Implement developer switch to choose file to save to
        Utils.writeCSV("src/main/resources/students2.csv", data);

        // Save the taken courses
        data = new ArrayList<>();

        for (Student student : students) {
            for (int i = 0; i < student.getCourseList().size(); i++) {
                data.add(new String[] {
                        student.getID() + ";"
                        + student.getCourseList().get(i)
                });
            }
        }

        // TODO: Change this to the final file
        Utils.writeCSV("src/main/resources/takensubjects2.csv", data);
    }

    // TODO: Add function to build the course table

    /**
     * Build an Object array to use for JTables
     * @param students Student list
     * @return Object array
     */
    public static Object[][] buildTable(List<Student> students) {
        Object[][] objectArray = new Object[students.size()][STUDENT_COLUMNS + 1];

        for (int i = 0; i < students.size(); i++) {
            objectArray[i][0] = students.get(i).getID();
            objectArray[i][1] = students.get(i).getNeptuneID();
            objectArray[i][2] = students.get(i).getName();
            objectArray[i][3] = students.get(i).getDateOfBirth();
            objectArray[i][4] = false;
        }

        return objectArray;
    }
}
