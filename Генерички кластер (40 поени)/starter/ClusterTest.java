import java.util.*;

/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Cluster<Point2D> cluster = new Cluster<>();
    int n = scanner.nextInt();
    scanner.nextLine();
    for (int i = 0; i < n; ++i) {
      String line = scanner.nextLine();
      String[] parts = line.split(" ");
      long id = Long.parseLong(parts[0]);
      float x = Float.parseFloat(parts[1]);
      float y = Float.parseFloat(parts[2]);
      cluster.addItem(new Point2D(id, x, y));
    }
    int id = scanner.nextInt();
    int top = scanner.nextInt();
    cluster.near(id, top);
    scanner.close();
  }
}

// your code here