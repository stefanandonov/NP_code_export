import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
  public static void main(String[] args) {
    System.out.println("=== READING RECORDS ===");
    StudentRecords studentRecords = new StudentRecords();
    int total = studentRecords.readRecords(System.in);
    System.out.printf("Total records: %d\n", total);
    System.out.println("=== WRITING TABLE ===");
    studentRecords.writeTable(System.out);
    System.out.println("=== WRITING DISTRIBUTION ===");
    studentRecords.writeDistribution(System.out);
  }
}

class StudentRecords {
  Map<String, Set<Record>> records;
  Map<String, Map<Integer, Integer>> grades;

  public StudentRecords() {
    this.records = new TreeMap<>();
    this.grades = new HashMap<>();
  }

  public int readRecords(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream);
    int total = 0;
    while (scanner.hasNextLine()) {
      ++total;
      String line = scanner.nextLine();
      String[] parts = line.split(" ");
      String code = parts[0];
      String type = parts[1];
      Record record = new Record(code, type);
      for (int i = 2; i < parts.length; ++i) {
        int grade = Integer.parseInt(parts[i]);
        Map<Integer, Integer> map = grades.get(type);
        if (map == null) {
          map = new TreeMap<>();
          grades.put(type, map);
        }
        Integer count = map.get(grade);
        if (count == null) {
          count = 0;
        }
        ++count;
        map.put(grade, count);
        record.addGrade(grade);
      }
      Set<Record> list = records.get(type);
      if (list == null) {
        list = new TreeSet<>();
        records.put(type, list);
      }
      list.add(record);
    }
    scanner.close();
    return total;
  }

  public void writeTable(OutputStream outputStream) {
    PrintWriter writer = new PrintWriter(outputStream);
    for (String key : records.keySet()) {
      writer.println(key);
      Set<Record> set = records.get(key);
      for (Record record : set) {
        writer.println(record);
      }
    }
    writer.flush();
  }

  public void writeDistribution(OutputStream outputStream) {
    PrintWriter writer = new PrintWriter(outputStream);
    Set<Stat> stats = new TreeSet<>();
    for (Map.Entry<String, Map<Integer, Integer>> entry : grades.entrySet()) {
      stats.add(new Stat(entry.getKey(), entry.getValue().get(10)));
    }
    for (Stat stat : stats) {
      writer.println(stat.type);
      Map<Integer, Integer> typeStat = grades.get(stat.type);
      for (Map.Entry<Integer, Integer> entry : typeStat.entrySet()) {
        writer.printf("%2d | ", entry.getKey());
        for (int i = 0; i < entry.getValue(); i += 10) {
          writer.print("*");
        }
        writer.printf("(%d)", entry.getValue());
        writer.println();
      }
    }
    writer.flush();
  }

}

class Stat implements Comparable<Stat> {

  public Stat(String type, int count) {
    this.type = type;
    this.count = count;
  }

  String type;
  int count;

  @Override
  public int compareTo(Stat o) {
    int c = Integer.compare(o.count, count);
    if (c == 0) {
      return type.compareTo(o.type);
    }
    return c;
  }
}

class Record implements Comparable<Record> {
  String code;
  String type;
  List<Integer> grades;
  float sum;

  public Record(String code, String type) {
    this.code = code;
    this.type = type;
    this.grades = new ArrayList<>();
    sum = 0;
  }

  public void addGrade(int grade) {
    sum += grade;
    this.grades.add(grade);
  }

  @Override
  public int compareTo(Record o) {
    int avg = Float.compare(sum / grades.size(), o.sum / o.grades.size());
    if (avg == 0) {
      return code.compareTo(o.code);
    }
    return -avg;
  }

  @Override
  public String toString() {
    return String.format("%s %.2f", code, Math.round(sum * 100. / grades.size()) / 100.);
  }
}
