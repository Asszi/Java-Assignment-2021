package thorxs;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Subject {
    public static final int SUBJECT_COLUMNS = 6;

    @Getter
    private final int  ID;
    @Getter @Setter
    private String subjectCode;
    @Getter @Setter
    private String subjectName;
    @Getter @Setter
    private int seminars;
    @Getter @Setter
    private int lectures;
    @Getter @Setter
    private int credits;

    public Subject(int ID, String subjectCode, String subjectName, int seminars, int lectures, int credits) {
        this.ID = ID;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.seminars = seminars;
        this.lectures = lectures;
        this.credits = credits;
    }

    /**
     * Loads the subjects into a List
     * @return Subject list
     */
    public static List<Subject> loadSubjects() {
        JFrame frame = new JFrame();
        List<String[]> data = Utils.readCSV("src/main/resources/subjects.csv");
        List<Subject> subjects = new ArrayList<>();

        List<String[]> errors = new ArrayList<>();

        boolean errorFound = false;

        // Load the data of the subjects
        for (String[] dataLine : data) {
            boolean shouldLoad = true;

            // Check data integrity
            if (dataLine.length == SUBJECT_COLUMNS) {
                // Check if any fields are empty
                for (int j = 0; j < SUBJECT_COLUMNS; j++) {
                    if (dataLine[j].equals("")) {
                        errorFound = true;
                        shouldLoad = false;
                        break;
                    }
                }

                // Check if the data should load
                if (shouldLoad) {
                    subjects.add(new Subject(
                            Integer.parseInt(dataLine[0]),
                            dataLine[1],
                            dataLine[2],
                            Integer.parseInt(dataLine[3]),
                            Integer.parseInt(dataLine[4]),
                            Integer.parseInt(dataLine[5])));
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
            JOptionPane.showMessageDialog(frame, "Warning: Incomplete line(s) of subject data found!\nIncomplete lines have been copied to the logs for review!", "Warning", JOptionPane.WARNING_MESSAGE);
            Utils.writeCSV("src/main/resources/subject-load.log", errors);
        }

        return subjects;
    }

    /**
     * Saves the subjects to file
     * @param subjects List of Subject objects
     */
    public static void saveSubjects(List<Subject> subjects) {
        List<String[]> data = new ArrayList<>();

        for (Subject subject : subjects) {
            data.add( new String[] {
                subject.getID() + ";"
                + subject.getSubjectCode() + ";"
                + subject.getSubjectName() + ";"
                + subject.getSeminars() + ";"
                + subject.getLectures() + ";"
                + subject.getCredits()
            });
        }

        // TODO: Change this to the final file
        Utils.writeCSV("src/main/resources/subjects2.csv", data);
    }

    /**
     * Build an Object array to use for JTables
     * @param subjects Subject list
     * @return Object array
     */
    public static Object[][] buildTable(List<Subject> subjects) {
        Object[][] objectArray = new Object[subjects.size()][SUBJECT_COLUMNS + 1];

        for (int i = 0; i < subjects.size(); i++) {
            objectArray[i][0] = subjects.get(i).getID();
            objectArray[i][1] = subjects.get(i).getSubjectCode();
            objectArray[i][2] = subjects.get(i).getSubjectName();
            objectArray[i][3] = subjects.get(i).getLectures();
            objectArray[i][4] = subjects.get(i).getSeminars();
            objectArray[i][5] = subjects.get(i).getCredits();
            objectArray[i][6] = false;
        }

        return objectArray;
    }
}
