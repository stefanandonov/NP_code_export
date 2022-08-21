//package mk.ukim.finki.primeri;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public double averagePoints() {
        return points.stream().mapToInt(i -> i).sum() / 10.0;
    }

    public boolean hasSignature() {
        return points.size() >= 8;
    }

    public boolean lostSignature() {
        return !hasSignature();
    }

    public String toString() {
        return String.format("%s %s %.2f",
                index,
                hasSignature() ? "YES" : "NO",
                averagePoints());
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }
}

class LabExercises {
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        Comparator<Student> studentComparator = Comparator.comparingDouble(Student::averagePoints)
                .thenComparing(Student::getIndex);

        if (!ascending)
            studentComparator = studentComparator.reversed();

        students.stream().sorted(studentComparator).limit(n).forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        Comparator<Student> studentComparator = Comparator.comparing(Student::getIndex)
                .thenComparing(Student::averagePoints);

        return students.stream()
                .filter(Student::lostSignature)
                .sorted(studentComparator)
                .collect(Collectors.toList());
    }

    private static Integer getStudentYear(String index) {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    public Map<Integer, Double> getStatisticsByYear() {
        Map<Integer, Double> averageMap = new TreeMap<>();
        Map<Integer, Integer> countingMap = new TreeMap<>();

        students.stream()
                .filter(Student::hasSignature)
                .forEach(student -> {
                    Integer year = getStudentYear(student.getIndex());
                    averageMap.putIfAbsent(year, 0.0);
                    countingMap.putIfAbsent(year, 0);

                    countingMap.computeIfPresent(year, (k, v) -> {
                        v++;
                        return v;
                    });
                    averageMap.computeIfPresent(year, (k, v) -> {
                        v += student.averagePoints();
                        return v;
                    });
                });

        for (Integer year : averageMap.keySet()) {
            averageMap.computeIfPresent(year, (k, v) -> {
                v /= countingMap.get(year);
                return v;
            });
        }

        //System.out.println(countingMap);

        return averageMap;
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
