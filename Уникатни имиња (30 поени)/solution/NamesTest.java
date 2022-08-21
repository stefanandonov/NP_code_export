import java.util.*;

public class NamesTest {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    scanner.nextLine();
    Names names = new Names();
    for (int i = 0; i < n; ++i) {
      String name = scanner.nextLine();
      names.addName(name);
    }
    n = scanner.nextInt();
    System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
    names.printN(n);
    System.out.println("===== FIND NAME =====");
    int len = scanner.nextInt();
    int index = scanner.nextInt();
    System.out.println(names.findName(len, index));
    scanner.close();

  }
}

class Names {
  Map<String, Integer> namesCount;

  public Names() {
    namesCount = new TreeMap<>();
  }

  public void addName(String name) {
    Integer count = namesCount.get(name);
    if (count == null) {
      count = 0;
    }
    ++count;
    namesCount.put(name, count);
  }


  public void printN(int n) {
    for (Map.Entry<String, Integer> entry : namesCount.entrySet()) {
      if (entry.getValue() >= n) {
        Set<Character> unique = new HashSet<>();
        char[] w = entry.getKey().toCharArray();
        for (char c : w) {
          unique.add(Character.toLowerCase(c));
        }
        System.out.printf("%s (%d) %d\n", entry.getKey(), entry.getValue(), unique.size());
      }
    }
  }

  public String findName(int len, int x) {
    List<String> names = new LinkedList<>(namesCount.keySet());
    ListIterator<String> it = names.listIterator();
    while (it.hasNext()) {
      String name = it.next();
      if (name.length() >= len) {
        it.remove();
      }
    }
    it = names.listIterator();
    String name = null;
    x = x % names.size();
    for (int i = 0; i <= x; ++i) {
      name = it.next();
    }
    return name;
  }
}
