package thorxs.gui;

import lombok.Getter;
import thorxs.Student;
import thorxs.Subject;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class SummaryPage {
    @Getter
    private JPanel contentPanel;
    private JLabel labelNumberOfStudents;
    private JLabel labelNumberOfSubjects;
    private JLabel labelTotalNumberOfSubjectsTaken;
    private JLabel labelTotalCreditsTaken;
    private JLabel labelYoungestStudent;
    private JLabel labelOldestStudent;

    public SummaryPage(List<Student> students, List<Subject> subjects) {
        labelNumberOfStudents.setText("Total number of students: " + students.size());
        labelNumberOfSubjects.setText("Total number of subjects: " + subjects.size());

        // Sum the taken courses and credit worth
        int sum = 0;
        int sumCredit = 0;

        for (Student student : students) {
            sum += student.getCourseList().size();

            for (int i = 0; i < student.getCourseList().size(); i++) {
                for (Subject subject : subjects) {
                    if (student.getCourseList().get(i) == subject.getID()) {
                        sumCredit += subject.getCredits();
                    }
                }
            }
        }

        labelTotalNumberOfSubjectsTaken.setText("Total number of subjects taken: " + sum);
        labelTotalCreditsTaken.setText("Total credits taken: " + sumCredit);

        Student youngest = students.get(0);
        Student oldest = students.get(0);

        for (int i = 1; i < students.size(); i++) {
            // Search for the youngest student
            if (youngest.getDateOfBirth().isAfter(students.get(i).getDateOfBirth())) {
                youngest = students.get(i);
            }

            // Search for the oldest student
            if (oldest.getDateOfBirth().isBefore(students.get(i).getDateOfBirth())) {
                oldest = students.get(i);
            }
        }

        labelYoungestStudent.setText("Name of the youngest student: " + youngest.getName());
        labelOldestStudent.setText("Name of the oldest student: " + oldest.getName());
    }
}
