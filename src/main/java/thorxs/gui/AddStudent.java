package thorxs.gui;

import lombok.Getter;
import thorxs.Student;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
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
            String neptuneID = textFieldNeptuneID.getText().toUpperCase();
            String name = textFieldName.getText();
            String date = textFieldDate.getText();
            if (neptuneID.equals("") || name.equals("") || date.equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "You must fill out every field!", "Missing data", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO: neptunID to upper case

            LocalDate dateOfBirth = LocalDate.parse(date);

            Student student = new Student(students.size() + 1, neptuneID, name, dateOfBirth);
            students.add(student);
            resetFields();
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        });

        // Add action listener from the main window
        //buttonAddStudent.addActionListener(action);
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
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(new JFrame(), "There was an error while creating the MaskFormatter!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        textFieldDate = new JFormattedTextField(mask);
    }
}
