package thorxs.gui;

import lombok.Getter;
import thorxs.Subject;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

public class AddSubject {
    @Getter
    private JPanel contentPanel;
    private JTextField textFieldSubjectID;
    private JTextField textFieldSubjectName;
    private JFormattedTextField formattedTextFieldSeminars;
    private JFormattedTextField formattedTextFieldLectures;
    private JFormattedTextField formattedTextFieldCredit;
    private JButton buttonAddSubject;

    public AddSubject(List<Subject> subjects, ActionListener action) {
        buttonAddSubject.addActionListener(e -> {
            String subjectID = textFieldSubjectID.getText();
            String subjectName = textFieldSubjectName.getText();
            String seminars = formattedTextFieldSeminars.getText();
            String lectures = formattedTextFieldLectures.getText();
            String credit = formattedTextFieldCredit.getText();

            if (subjectID.equals("") || subjectName.equals("") || seminars.equals("") || lectures.equals("") || credit.equals("")) {
                JOptionPane.showMessageDialog(new JDialog(), "You must fill out every field!", "Missing data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Subject subject = new Subject(subjects.size() + 1, subjectID, subjectName, Integer.parseInt(seminars), Integer.parseInt(lectures), Integer.parseInt(credit));
            subjects.add(subject);
            resetFields();
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        });
    }

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
            limit = new MaskFormatter("#");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(new JDialog(), "There was an error while creating the MaskFormatter!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        formattedTextFieldSeminars = new JFormattedTextField(limit);
        formattedTextFieldLectures = new JFormattedTextField(limit);
        formattedTextFieldCredit = new JFormattedTextField(limit);
    }
}
