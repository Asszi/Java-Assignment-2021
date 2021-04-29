package thorxs;

import thorxs.gui.AddStudent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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

    private static final JFrame frame = new JFrame("Balazs Orehovszki - Assignment 2021 Java");

    // Object arrays
    private static List<Student> students;
    private static List<Subject> subjects;

    // TODO: Fix locking bug when tabs are switched in edit mode
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

        // TODO: Add popup window to add student
        // Add button
        buttonAddStudent.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();

            int nextID = 1;

            if (model.getRowCount() != 0) {
                nextID = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString()) + 1;
            }

            model.addRow(new Object[] { nextID, "", "", false});
        });
        // Remove button
        buttonRemoveStudent.addActionListener(e -> {
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

            // Rebuild the table
            DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
            model.setDataVector(Student.buildTable(students), studentColumns);
        });
        // Edit button
        buttonEditStudent.addActionListener(e -> {
            // Change button text according to edit mode
            if (studentInEditMode) {
                buttonEditStudent.setText("DONE");
            } else {

                // Check for blank cells
                boolean foundBlankCell = false;

                for (int i = 0; i < studentsTable.getRowCount(); i++) {
                    for (int j = 1; j < studentsTable.getColumnCount() - 2; j++) {
                        if (studentsTable.getValueAt(i, j).equals("")) {
                            foundBlankCell = true;
                            // TODO: popup warning to fill every cell
                            System.out.println("FOUND BLANK");
                        }
                    }
                }

                if (foundBlankCell) {
                    JFrame errorFrame = new JFrame();

                    JOptionPane.showMessageDialog(errorFrame, "Cells can't be empty! Make sure to fill in all information!", "Empty cells", JOptionPane.WARNING_MESSAGE);
                } else {
                    studentInEditMode = true;
                    buttonEditStudent.setText("EDIT");

                    // Remove the ticks
                    for (int i = 0; i < studentsTable.getRowCount(); i++) {
                        studentsTable.setValueAt(false, i, studentsTable.getColumnCount() - 1);
                    }
                }
            }
        });
        // Edit subjects button
        buttonEditStudentSubjects.addActionListener(e -> {
            JFrame popup = new JFrame("Add Student");
            popup.getContentPane().add(new AddStudent(students).getContentPanel());
            popup.pack();
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
            // TODO: Add event listener to the popup window
        });

        /*
         * Subjects
         */

        // TODO: Add popup window to add subject
        // Add button
        buttonAddSubject.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) subjectsTable.getModel();

            int nextID = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString()) + 1;

            model.addRow(new Object[] { nextID, "", "", "", "", "", false });
        });
        // Remove button
        buttonRemoveSubject.addActionListener(e -> {
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

            // Rebuild the table
            DefaultTableModel model = (DefaultTableModel) subjectsTable.getModel();
            model.setDataVector(Subject.buildTable(subjects), subjectColumns);
        });
        // Edit button
        buttonEditSubject.addActionListener(e -> {
            subjectInEditMode = !subjectInEditMode;

            // Change button text according to edit mode
            if (subjectInEditMode) {
                buttonEditSubject.setText("DONE");
            } else {
                buttonEditSubject.setText("EDIT");

                // Remove the ticks
                for (int i = 0; i < subjectsTable.getRowCount(); i++) {
                    subjectsTable.setValueAt(false, i, subjectsTable.getColumnCount() - 1);
                }
            }
        });

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

            frame.revalidate();
        });
    }

    public static void main(String[] args) {
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
                int option = JOptionPane.showOptionDialog(new JFrame(),
                        "Would you like to save your work?",
                        "Close program",
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
        String[] columnsT3 = new String[] {
                "ID", "Neptune ID", "Name"
        };

        // Build the data arrays
        Object[][] dataT1 = Student.buildTable(students);
        Object[][] dataT2 = Subject.buildTable(subjects);

        /*
         * Create the table model for the students
         */
        DefaultTableModel modelStudents = new DefaultTableModel(dataT1, studentColumns) {
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
                if (!studentInEditMode && column == (this.getColumnCount() - 1)) {
                    // If not in edit mode and it is the last column
                    return true;
                }
                if (studentInEditMode && (boolean) this.getValueAt(row, this.getColumnCount() - 1)) {
                    // If in edit mode and the row is selected
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class c = switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 3 -> Date.class;
                    case 4 -> Boolean.class;
                    default -> String.class;
                };

                return c;
            }
        };

        /*
         * Create the table model for the subjects
         */
        DefaultTableModel modelSubjects = new DefaultTableModel(dataT2, subjectColumns) {
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
                if (!studentInEditMode && column == (this.getColumnCount() - 1)) {
                    // If not in edit mode and it is the last column
                    return true;
                }
                if (subjectInEditMode && (boolean) this.getValueAt(row, this.getColumnCount() - 1)) {
                    // If in edit mode and the row is selected
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                Class c = switch (columnIndex) {
                    case 0, 3, 4, 5 -> Integer.class;
                    case 6 -> Boolean.class;
                    default -> String.class;
                };

                return c;
            }
        };
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                if (value instanceof Date) {
                    value = f.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };

        // Initialize the JTables
        studentsTable = new JTable(modelStudents);
        subjectsTable = new JTable(modelSubjects);

        // Apply custom date rendering format
        studentsTable.getColumnModel().getColumn(3).setCellRenderer(tableCellRenderer);

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
}
