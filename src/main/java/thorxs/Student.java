package thorxs;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final LocalDate dateOfBirth;

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
        List<String[]> data = Utils.readCSV(Configuration.getStudentsPath());
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
            JOptionPane.showMessageDialog(new JDialog(), "Warning: Incomplete line(s) of student data found!\nIncomplete lines have been copied to the logs for review!", "Warning", JOptionPane.WARNING_MESSAGE);
            Utils.writeCSV("./data/student-incomplete.log", errors);
        }

        List<String[]> sub = Utils.readCSV(Configuration.getTakenSubjectsPath());

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

        for (Student student : students) {
            data.add(new String[] {
                student.getID() + ";"
                + student.getNeptuneID() + ";"
                + student.getName() + ";"
                + student.getDateOfBirth()
            });
        }

        if (Configuration.isDeveloperMode()) {
            Utils.writeCSV("./data/dev-students.csv", data);
        } else {
            Utils.writeCSV(Configuration.getStudentsPath(), data);
        }

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

        if (Configuration.isDeveloperMode()) {
            Utils.writeCSV("./data/dev-takensubjects.csv", data);
        } else {
            Utils.writeCSV(Configuration.getTakenSubjectsPath(), data);
        }
    }


    /**
     * Build an object array to use for JTables
     * @param student A Student object
     * @param subjects Subject list
     * @return Object array
     */
    public static Object[][] buildTakenCourseList(Student student, List<Subject> subjects) {
        Object[][] objectArray = new Object[student.getCourseList().size()][4];

        for (int i = 0; i < student.getCourseList().size(); i++) {
            for (Subject subject : subjects) {
                if (student.getCourseList().get(i) == subject.getID()) {
                    objectArray[i][0] = subject.getID();
                    objectArray[i][1] = subject.getSubjectCode();
                    objectArray[i][2] = subject.getSubjectName();
                    objectArray[i][3] = false;
                }
            }
        }

        return objectArray;
    }

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
