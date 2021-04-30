package thorxs.gui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class AddSubject {
    @Getter
    private JPanel contentPanel;
    private JTextField textFieldSubjectID;
    private JTextField textFieldSubjectName;
    private JFormattedTextField formattedTextFieldSeminars;
    private JFormattedTextField formattedTextFieldLectures;
    private JFormattedTextField formattedTextFieldCredit;
    private JButton buttonAddSubject;

    /**
     * Reset the input fields
     */
    private void resetFields() {
        textFieldSubjectID.setText("");
        textFieldSubjectName.setText("");
        formattedTextFieldSeminars.setText("");
        formattedTextFieldLectures.setText("");
        formattedTextFieldCredit.setText("");
    }

    private void createUIComponents() {
        MaskFormatter limit = null;

        try {
            limit = new MaskFormatter("##");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(new JFrame(), "There was an error while creating the MaskFormatter!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        formattedTextFieldSeminars = new JFormattedTextField(limit);
        formattedTextFieldLectures = new JFormattedTextField(limit);
        formattedTextFieldCredit = new JFormattedTextField(limit);
    }
}
