package thorxs.gui;

import lombok.Getter;
import thorxs.Student;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddStudent extends JFrame {
    private JTextField textFieldNeptuneID;
    private JTextField textFieldName;
    private JButton buttonAddStudent;

    @Getter
    private JPanel contentPanel;
    private JFormattedTextField textFieldDate;

    public AddStudent(List<Student> students) {
        //TODO: Add action listener
        //buttonAddStudent.addActionListener(listener);
        buttonAddStudent.addActionListener(e -> {
            String neptuneID = textFieldNeptuneID.getText();
            String name = textFieldName.getText();
            String date = textFieldDate.getText();
            if (neptuneID.equals("") || name.equals("") || date.equals("")) {
                // TODO: Warning message
            }

            Date dateOfBirth = null;

            try {
                dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }

            if (dateOfBirth == null) {
                // TODO: error message
            }

            // TODO: Call rebuilding of the JTable and Save
            Student student = new Student(students.size() + 1, neptuneID, name, dateOfBirth);
            students.add(student);
            resetFields();
        });
    }

    /**
     * Reset the input fields
     */
    private void resetFields() {
        textFieldNeptuneID.setText("");
        textFieldName.setText("");
    }

    private void createUIComponents() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        textFieldDate = new JFormattedTextField(dateFormatter);
    }
}
