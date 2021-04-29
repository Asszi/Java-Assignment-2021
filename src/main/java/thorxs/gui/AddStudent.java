package thorxs.gui;

import lombok.Getter;
import thorxs.Student;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;
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

    public AddStudent(List<Student> students, ActionListener action) {
        buttonAddStudent.addActionListener(e -> {
            String neptuneID = textFieldNeptuneID.getText();
            String name = textFieldName.getText();
            String date = textFieldDate.getText();
            if (neptuneID.equals("") || name.equals("") || date.equals("")) {
                // TODO: Warning message
                return;
            }

            Date dateOfBirth = null;

            try {
                dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                System.out.println(dateOfBirth.toString());
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }

            if (dateOfBirth == null) {
                // TODO: error message
                System.out.println("error2");
                return;
            }

            Student student = new Student(students.size() + 1, neptuneID, name, dateOfBirth);
            students.add(student);
            resetFields();
        });

        // Add action listener from the main window
        buttonAddStudent.addActionListener(action);
    }

    /**
     * Reset the input fields
     */
    private void resetFields() {
        textFieldNeptuneID.setText("");
        textFieldName.setText("");
        textFieldDate.setText("");
    }

    private void createUIComponents() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            // TODO: Error handling
            e.printStackTrace();
        }

        textFieldDate = new JFormattedTextField(mask);
    }
}
