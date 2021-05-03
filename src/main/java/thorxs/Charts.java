package thorxs;

import lombok.Getter;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
 * NOTE: Could have used https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.Change.html to detect changes, but the assignment required the use of threads
 */
public class Charts implements Runnable {
    @Getter
    private JPanel currentChart;
    private final JPanel statisticsPage;
    private int chartType;
    private final List<Student> students;
    private final List<Subject> subjects;

    private boolean running = true;

    public Charts(List<Student> students, List<Subject> subjects, JPanel statisticsPage) {
        this.statisticsPage = statisticsPage;
        this.students = students;
        this.subjects = subjects;

        this.subjectRatioChart();
    }

    /**
     * While running, check for changes and update the chart
     */
    public void run() {
        // Make an initial copy of the data arrays
        List<Student> prevStudents = new ArrayList<>(students);
        List<Subject> prevSubjects = new ArrayList<>(subjects);

        while (running) {
            // Check if the arrays are matching
            if (!listEquals(prevStudents, students) || !listEquals(prevSubjects, subjects)) {
                switch (chartType) {
                    case 1 -> subjectRatioChart();
                    case 2 -> subjectStudentChart();
                    case 3 -> creditStudentChart();
                }

                // Make a copy of the new values
                prevStudents = new ArrayList<>(students);
                prevSubjects = new ArrayList<>(subjects);

                // Update the JPanel
                statisticsPage.removeAll();
                statisticsPage.add(currentChart);
                statisticsPage.revalidate();
            }
        }
    }

    /**
     * Stop the thread
     */
    public void stop() {
        running = false;
    }

    /**
     * Creates the subject ratio chart
     */
    public void subjectRatioChart() {
        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        // Get the names of the subjects
        for (Subject subject : subjects) {
            if (subject == null) {
                return;
            }

            names.add(subject.getSubjectName());
            ids.add(subject.getID());
            values.add(0);
        }

        // Sum the times the subject has been taken
        for (Student student : students) {
            if (student == null) {
                return;
            }
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
                .title("Subject Ratio")
                .build();

        // Customize chart
        chart.getStyler().setCircular(false);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Vertical);


        // Series
        for (int i = 0; i < names.size(); i++) {
            chart.addSeries(names.get(i), values.get(i));
        }

        chartType = 1;
        currentChart = new XChartPanel(chart);
    }

    /**
     * Creates the subject / student chart
     */
    public void subjectStudentChart() {
        List<String> neptuneID = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        // Sum the number of subjects per student
        for (Student student : students) {
            if (student == null) {
                return;
            }

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

        chartType = 2;
        currentChart = new XChartPanel(chart);
    }

    /**
     * Creates the credit / student chart
     */
    public void creditStudentChart() {
        List<String> neptuneID = new ArrayList<>();
        List<Integer> credits = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        // Get the subject IDs
        for (Subject subject : subjects) {
            if (subject == null) {
                return;
            }
            ids.add(subject.getID());
        }

        int counter = 0;
        // Sum the taken credits for all students
        for (Student student : students) { // For every student
            if (student == null) {
                return;
            }
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

        chartType = 3;
        currentChart = new XChartPanel(chart);
    }

    /**
     * Check if two lists have the same content, ignoring order
     * @param list1 First list object
     * @param list2 Second list object
     * @param <T> Type
     * @return If the lists have the same content
     */
    private <T> boolean listEquals(List<T> list1, List<T> list2) {
        HashSet<T> first = new HashSet<T>(list1);
        HashSet<T> second = new HashSet<T>(list2);

        return first.equals(second);
    }
}
