package thorxs;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Charts {
    /**
     * Creates the subject ratio chart
     * @param students The list of students
     * @param subjects The lsit of subjects
     * @return The chart object
     */
    public JPanel subjectRatioChart(List<Student> students, List<Subject> subjects) {
        JFrame frame = new JFrame();

        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        // Get the names of the subjects
        for (Subject subject : subjects) {
            names.add(subject.getSubjectName());
            ids.add(subject.getID());
            values.add(0);
        }

        // Sum the times the subject has been taken
        for (Student student : students) {
            for (int i = 0; i < student.getCourseList().size(); i++) {
                for (int j = 0; j < ids.size(); j++) {
                    // If the subject ID matches
                    if (student.getCourseList().get(i).equals(ids.get(j))) {
                        values.set(j, values.get(j) + 1);
                    }
                }
            }
        }

        // Create chart
        PieChart chart = new PieChartBuilder()
                .title("Subject Ration")
                .build();

        // Customize chart
        chart.getStyler().setCircular(false);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Vertical);


        // Series
        for (int i = 0; i < names.size(); i++) {
            chart.addSeries(names.get(i), values.get(i));
        }

        return new XChartPanel(chart);
    }

    /**
     * Creates the subject / student chart
     * @param students The list of students
     * @return The chart object
     */
    public JPanel subjectStudentChart(List<Student> students) {
        JFrame frame = new JFrame();

        List<String> neptuneID = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        // Sum the number of subjects per student
        for (Student student : students) {
            neptuneID.add(student.getNeptuneID());
            values.add(student.getCourseList().size());
        }

        // Create chart
        CategoryChart chart = new CategoryChartBuilder()
                .title("Subjects per student")
                .yAxisTitle("Subjects")
                .build();

        // Customize chart
        chart.getStyler().setHasAnnotations(false);

        // Series
        chart.addSeries("Subjects", neptuneID, values);

        return new XChartPanel(chart);
    }

    /**
     * Creates the credit / student chart
     * @param students The list of students
     * @param subjects The list of subjects
     * @return The chart object
     */
    public JPanel creditStudentChart(List<Student> students, List<Subject> subjects) {
        JFrame frame = new JFrame();

        List<String> neptuneID = new ArrayList<>();
        List<Integer> credits = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        // Get the subject IDs
        for (Subject subject : subjects) {
            ids.add(subject.getID());
        }

        int counter = 0;
        // Sum the taken credits for all students
        for (Student student : students) { // For every student
            // Get the neptuneID of all the students
            neptuneID.add(student.getNeptuneID());
            credits.add(0);

            for (int i = 0; i < student.getCourseList().size(); i++) { // Do it until the end of the taken courses list
                for (int j = 0; j < ids.size(); j++) { // Check every ID
                    // If the subject ID matches
                    if (student.getCourseList().get(i).equals(ids.get(j))) { // if the I course taken is J
                        credits.set(counter, credits.get(counter) + subjects.get(j).getCredits());
                    }
                }
            }

            counter++;
        }

        // Create chart
        CategoryChart chart = new CategoryChartBuilder()
                .title("Credits per student")
                .yAxisTitle("Credits")
                .build();

        // Customize chart
        chart.getStyler().setHasAnnotations(false);

        // Series
        chart.addSeries("Credits", neptuneID, credits);

        return new XChartPanel(chart);
    }
}
