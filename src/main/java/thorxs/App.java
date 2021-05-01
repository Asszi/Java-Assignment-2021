package thorxs;

import thorxs.gui.AddStudent;
import thorxs.gui.AddSubject;
import thorxs.gui.EditSubjects;
import thorxs.gui.SummaryPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends JFrame {

    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;
    private JTable studentsTable;
    private JTable subjectsTable;
    private JButton subjectRatioButton;
    private JButton subjectStudentButton;
    private JButton creditStudentButton;
    private JButton summaryButton;
    private JPanel paneStatistics;
    private JPanel paneStatisticsMenu;
    private JScrollPane scrollPaneStudents;
    private JScrollPane scrollPaneSubjects;
    private JButton buttonAddStudent;
    private JButton buttonRemoveStudent;
    private JButton buttonAddSubject;
    private JButton buttonRemoveSubject;
    private JButton buttonEditStudentSubjects;
    private JButton buttonEditStudent;
    private JButton buttonEditSubject;
    private JButton buttonStudentsSave;
    private JButton buttonSubjectSave;

    private static final JFrame frame = new JFrame("Balazs Orehovszki - Assignment 2021 Java");

    // Object arrays
    private static List<Student> students;
    private static List<Subject> subjects;

    // Flag for edit mode
    private static boolean studentInEditMode;
    private static boolean subjectInEditMode;

    private static final String[] studentColumns
            = new String[] {
            "ID", "Neptune ID", "Name", "Date of birth", ""
    };

    private static final String[] subjectColumns = new String[] {
            "ID", "Subject ID", "Subject Name", "Seminars", "Lectures", "Credit", ""
    };

    public App() {
        /*
         * Students
         */

        // Add button
        buttonAddStudent.addActionListener(e -> {
            // Prevent action in edit mode
            if (studentInEditMode) {
                JOptionPane.showMessageDialog(new JDialog(), "Please, exit from edit mode before adding a new student!", "Exit edit mode", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Action listener for button pressed on the popup window
            ActionListener action = e12 -> rebuildStudentsTable();

            JDialog popup = new JDialog();
            popup.setTitle("Add Student");
            popup.setPreferredSize(new Dimension(300, 250));
            popup.getContentPane().add(new AddStudent(students, action).getContentPanel());
            popup.pack();
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
        });
        // Remove button
        buttonRemoveStudent.addActionListener(e -> {
            // Prevent action in edit mode
            if (studentInEditMode) {
                JOptionPane.showMessageDialog(new JDialog(), "Please, exit from edit mode before removing a student!", "Exit edit mode", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Remove the ticks
            for (int i = 0; i < studentsTable.getRowCount(); i++) {
                if ((boolean) studentsTable.getValueAt(i, studentsTable.getColumnCount() - 1)) {
                    for (int j = 0; j < students.size(); j++) {
                        if (students.get(j) != null) {
                            if (students.get(j).getID() == (int) studentsTable.getValueAt(i, 0)) {
                                // Nulling the object to avoid list shifting
                                students.set(j, null);
                            }
                        }
                    }
                }
            }

            // Remove null objects from the list
            students.removeAll(Collections.singletonList(null));

            rebuildStudentsTable();
        });
        // Edit button
        buttonEditStudent.addActionListener(e -> {
            // Check if any rows are selected for editing
            boolean validCall = false;

            for (int i = 0; i < studentsTable.getRowCount(); i++) {
                if ((boolean) studentsTable.getValueAt(i, studentsTable.getColumnCount() - 1)) {
                    validCall = true;
                }
            }

            if (!validCall) {
                return;
            }

            // Change button text according to edit mode
            if (!studentInEditMode) {
                studentInEditMode = true;
                buttonEditStudent.setText("DONE");
            } else {
                // Check for blank cells
                boolean foundBlankCell = false;

                for (int i = 0; i < studentsTable.getRowCount(); i++) {
                    for (int j = 1; j < studentsTable.getColumnCount() - 2; j++) {
                        if (studentsTable.getValueAt(i, j).equals("")) {
                            foundBlankCell = true;
                        }
                    }
                }

                if (foundBlankCell) {
                    JOptionPane.showMessageDialog(new JDialog(), "Cells can't be empty! Make sure to fill in all information!", "Empty cells", JOptionPane.WARNING_MESSAGE);
                } else {
                    studentInEditMode = false;
                    buttonEditStudent.setText("EDIT");

                    // Remove the ticks
                    for (int i = 0; i < studentsTable.getRowCount(); i++) {
                        studentsTable.setValueAt(false, i, studentsTable.getColumnCount() - 1);
                    }

                    // Modify data
                    List<Student> newList = new ArrayList<>();

                    for (int i = 0; i < studentsTable.getRowCount(); i++) {
                        newList.add(new Student(
                                (Integer) studentsTable.getValueAt(i, 0),
                                (String) studentsTable.getValueAt(i, 1),
                                (String) studentsTable.getValueAt(i, 2),
                                LocalDate.parse((String) studentsTable.getValueAt(i, 3))
                        ));
                    }

                    students = newList;
                }
            }
        });
        // Edit subjects button
        buttonEditStudentSubjects.addActionListener(e -> {
            // TODO: While window open prevent edit
            // Prevent action in edit mode
            if (studentInEditMode) {
                JOptionPane.showMessageDialog(new JDialog(), "Please, exit from edit mode before editing taken subjects!", "Exit edit mode", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if any rows are selected for editing
            List<Student> studentTempList = new ArrayList<>();

            for (int i = 0; i < studentsTable.getRowCount(); i++) {
                // Check for ticks
                if ((boolean) studentsTable.getValueAt(i, studentsTable.getColumnCount() - 1)) {
                    for (Student student : students) {
                        // Check for student id
                        if (student.getID() == (int) studentsTable.getValueAt(i, 0)) {
                            studentTempList.add(student);
                        }
                    }
                }
            }

            // If there are no students for editing
            if (studentTempList.isEmpty()) {
                return;
            }

            for (Student student : studentTempList) {
                JDialog popup = new JDialog();
                popup.setTitle("Modify taken subjects");

                // Action listener for button pressed on the popup window
                ActionListener windowClosed = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        popup.dispose();
                    }
                };

                popup.setSize(350, 400);
                popup.getContentPane().add(new EditSubjects(student, subjects, windowClosed).getContentPanel());
                popup.pack();
                popup.setLocationRelativeTo(null);
                popup.setVisible(true);

                // TODO: Wait for the dialog box to close?
            }
        });
        // Save button
        buttonStudentsSave.addActionListener(e -> Student.saveStudents(students));

        /*
         * Subjects
         */
        // Add button
        buttonAddSubject.addActionListener(e -> {
            // Prevent action in edit mode
            if (subjectInEditMode) {
                JOptionPane.showMessageDialog(new JDialog(), "Please, exit from edit mode before adding a new subject!", "Exit edit mode", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Action listener for button pressed on the popup window
            ActionListener action = e1 -> rebuildSubjectsTable();

            JDialog popup = new JDialog();
            popup.setTitle("Add Subject");
            popup.setPreferredSize(new Dimension(300, 325));
            popup.getContentPane().add(new AddSubject(subjects, action).getContentPanel());
            popup.pack();
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
        });
        // Remove button
        buttonRemoveSubject.addActionListener(e -> {
            // Prevent action in edit mode
            if (subjectInEditMode) {
                JOptionPane.showMessageDialog(new JDialog(), "Please, exit from edit mode before removing a subject!", "Exit edit mode", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Remove the ticks
            for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                if ((boolean) subjectsTable.getValueAt(i, subjectsTable.getColumnCount() - 1)) {
                    for (int j = 0; j < subjects.size(); j++) {
                        if (subjects.get(j) != null) {
                            if (subjects.get(j).getID() == (int) subjectsTable.getValueAt(i, 0)) {
                                // Nulling the object to avoid list shifting
                                subjects.set(j, null);
                            }
                        }
                    }
                }
            }

            // Remove null objects from the list
            subjects.removeAll(Collections.singletonList(null));

            rebuildSubjectsTable();
        });
        // Edit button
        buttonEditSubject.addActionListener(e -> {
            // Check if any rows are selected for editing
            boolean validCall = false;

            for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                if ((boolean) subjectsTable.getValueAt(i, subjectsTable.getColumnCount() - 1)) {
                    validCall = true;
                }
            }

            if (!validCall) {
                return;
            }

            // Change button text according to edit mode
            if (!subjectInEditMode) {
                subjectInEditMode = true;
                buttonEditSubject.setText("DONE");
            } else {
                // Check for blank cells
                boolean foundBlankCell = false;

                for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                    for (int j = 1; j < subjectsTable.getColumnCount() - 2; j++) {
                        if (subjectsTable.getValueAt(i, j).equals("")) {
                            foundBlankCell = true;
                        }
                    }
                }

                if (foundBlankCell) {
                    JOptionPane.showMessageDialog(new JDialog(), "Cells can't be empty! Make sure to fill in all information!", "Empty cells", JOptionPane.WARNING_MESSAGE);
                } else {
                    subjectInEditMode = false;
                    buttonEditSubject.setText("EDIT");

                    // Remove the ticks
                    for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                        subjectsTable.setValueAt(false, i, subjectsTable.getColumnCount() - 1);
                    }

                    // Modify data
                    List<Subject> newList = new ArrayList<>();

                    for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                        newList.add(new Subject(
                                (Integer) subjectsTable.getValueAt(i, 0),
                                (String) subjectsTable.getValueAt(i, 1),
                                (String) subjectsTable.getValueAt(i, 2),
                                (Integer) subjectsTable.getValueAt(i, 3),
                                (Integer) subjectsTable.getValueAt(i, 4),
                                (Integer) subjectsTable.getValueAt(i, 5)
                        ));
                    }

                    subjects = newList;
                }
            }
        });
        // Save button
        buttonSubjectSave.addActionListener(e -> Subject.saveSubjects(subjects));

        /*
         * Statistics
         */
        subjectRatioButton.addActionListener(e -> {
            paneStatistics.removeAll();
            paneStatistics.add(new Charts().subjectRatioChart(students, subjects));
            frame.revalidate();
        });
        subjectStudentButton.addActionListener(e -> {
            paneStatistics.removeAll();
            paneStatistics.add(new Charts().subjectStudentChart(students));
            frame.revalidate();
        });
        creditStudentButton.addActionListener(e -> {
            paneStatistics.removeAll();
            paneStatistics.add(new Charts().creditStudentChart(students, subjects));
            frame.revalidate();
        });
        summaryButton.addActionListener(e -> {
            paneStatistics.removeAll();
            paneStatistics.add(new SummaryPage(students, subjects).getContentPanel());
            frame.repaint();
            frame.revalidate();
        });
    }

    public static void main(String[] args) {
        Configuration.loadConfiguration();
        // Set the default values for the flags
        studentInEditMode = false;
        subjectInEditMode = false;

        // Load the data into the program
        subjects = Subject.loadSubjects();
        students = Student.loadStudents();

        // Initialize the frame
        frame.setContentPane(new App().mainTabbedPane);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();

        // Add custom event listener
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Button titles for the popup message
                Object[] buttonTitles = {
                        "Save",
                        "Don't save",
                        "Cancel"
                };

                // Popup message
                int option = JOptionPane.showOptionDialog(new JDialog(),
                        "Would you like to save your work?",
                        "Save before exit",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        buttonTitles,
                        buttonTitles[2]);

                // Save everything and close
                if (option == 0) {
                    Student.saveStudents(students);
                    Subject.saveSubjects(subjects);

                    frame.dispose();
                    System.exit(0);
                // Don't save and close
                } else if (option == 1) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });

        // Center the window on the main monitor and set it to visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // Build the data arrays
        Object[][] dataStudentsTable = Student.buildTable(students);
        Object[][] dataSubjectsTable = Subject.buildTable(subjects);

        /*
         * Create the table model for the students
         */
        DefaultTableModel modelStudents = new DefaultTableModel(dataStudentsTable, studentColumns) {
            @Override
            public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
                super.setDataVector(dataVector, columnIdentifiers);
            }

            public boolean isCellEditable(int row, int column){
                // ID cannot be manually edited
                if (column == 0) {
                    return false;
                }
                // Checkbox can be used if not in edit mode
                if (!studentInEditMode && column == (this.getColumnCount() - 1)) {
                    return true;
                }
                // If in edit mode, column is not the last column and the row has a checkmark
                return studentInEditMode && (column != (this.getColumnCount() - 1)) && (boolean) this.getValueAt(row, this.getColumnCount() - 1);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 4 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        /*
         * Create the table model for the subjects
         */
        DefaultTableModel modelSubjects = new DefaultTableModel(dataSubjectsTable, subjectColumns) {
            @Override
            public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
                super.setDataVector(dataVector, columnIdentifiers);
            }

            public boolean isCellEditable(int row, int column){
                // ID cannot be manually edited
                if (column == 0) {
                    return false;
                }
                // Checkbox is disabled in edit mode
                if (!subjectInEditMode && column == (this.getColumnCount() - 1)) {
                    // If not in edit mode and it is the last column
                    return true;
                }
                // If in edit mode and the row is selected
                return subjectInEditMode && (column != (this.getColumnCount() - 1)) && (boolean) this.getValueAt(row, this.getColumnCount() - 1);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 3, 4, 5 -> Integer.class;
                    case 6 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        // Initialize the JTables
        studentsTable = new JTable(modelStudents);
        subjectsTable = new JTable(modelSubjects);

        // Set the fonts for the JTables
        Font headerFont = new Font("Calibri", Font.BOLD, 16);
        Font tableFont = new Font("Calibri", Font.PLAIN, 14);
        studentsTable.getTableHeader().setFont(headerFont);
        subjectsTable.getTableHeader().setFont(headerFont);
        studentsTable.setFont(tableFont);
        subjectsTable.setFont(tableFont);

        // Initialize the JPanel
        paneStatistics = new JPanel();

        // Set the default chart to appear (Subject ratio in that case)
        paneStatistics.add(new Charts().subjectRatioChart(students, subjects));
    }

    /**
     * Rebuilds the data vector for the Students table
     */
    private void rebuildStudentsTable() {
        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
        model.setDataVector(Student.buildTable(students), studentColumns);
    }

    /**
     * Rebuilds the data vector for the Subjects table
     */
    private void rebuildSubjectsTable() {
        DefaultTableModel model = (DefaultTableModel) subjectsTable.getModel();
        model.setDataVector(Subject.buildTable(subjects), subjectColumns);
    }
}
