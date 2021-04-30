package thorxs.gui;

import lombok.Getter;
import thorxs.Student;
import thorxs.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditSubjects {
    private JTable tableEditSubjects;
    @Getter
    private JPanel contentPanel;
    private JButton doneButton;
    private JLabel studentNeptuneID;
    private JLabel studentName;

    private final Student student;
    private final List<Subject> subjects;

    private static final String[] subjectColumns
            = new String[] {
            "ID", "Subject Code", "Subject Name", ""
    };

    public EditSubjects(Student student, List<Subject> subjects, ActionListener action) {
        this.student = student;
        this.subjects = subjects;

        studentNeptuneID.setText("Neptune ID: " + student.getNeptuneID());
        studentName.setText("Name: " + student.getName());

        // TODO: Test
        doneButton.addActionListener(e -> {
            List<Integer> newList = new ArrayList<>();
            for (int i = 0; i < tableEditSubjects.getRowCount(); i++) {
                if ((boolean) tableEditSubjects.getValueAt(i, tableEditSubjects.getColumnCount() - 1)) {
                    newList.add((int) tableEditSubjects.getValueAt(i, 0));
                }
            }

            // Update the course list
            student.setCourseList(newList);

            // Close the window
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        });
    }

    private void createUIComponents() {
        // Build the data array
        Object[][] dataSubjectsTable = Student.buildTakenCourseList(student, subjects);

        // Create the table model
        DefaultTableModel modelSubjects = new DefaultTableModel(dataSubjectsTable, subjectColumns) {
            @Override
            public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
                super.setDataVector(dataVector, columnIdentifiers);
            }

            public boolean isCellEditable(int row, int column){
                // ID cannot be manually edited
                if (column == (this.getColumnCount() - 1)) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 3 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        // Initialize the table
        tableEditSubjects = new JTable(modelSubjects);

        // Set the fonts for the JTable
        Font headerFont = new Font("Calibri", Font.BOLD, 16);
        Font tableFont = new Font("Calibri", Font.PLAIN, 14);
        tableEditSubjects.getTableHeader().setFont(headerFont);
        tableEditSubjects.setFont(tableFont);
    }
}
