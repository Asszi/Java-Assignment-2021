package thorxs.gui;

import lombok.Getter;
import thorxs.Student;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class AddStudent extends JFrame {
    private JTextField textFieldNeptuneID;
    private JTextField textFieldName;
    private JButton buttonAddStudent;

    @Getter
    private JPanel contentPanel;

    public AddStudent(List<Student> students) {
        //TODO: Add action listener
        //buttonAddStudent.addActionListener(listener);
        buttonAddStudent.addActionListener(e -> {
            String neptuneID = textFieldNeptuneID.getText();
            String name = textFieldName.getText();
            if (neptuneID.equals("") || name.equals("")) {
                // TODO: Warning message
            }

            // TODO: Call rebuilding of the JTable and Save
            Student student = new Student(students.size() + 1, neptuneID, name);
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
}
