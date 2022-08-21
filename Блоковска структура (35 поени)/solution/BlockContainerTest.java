import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class BlockContainerTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int size = scanner.nextInt();
		BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
		scanner.nextLine();
		Integer lastInteger = null;
		for(int i = 0; i < n; ++i) {
			int element = scanner.nextInt();
			lastInteger = element;
			integerBC.add(element);
		}
		System.out.println("+++++ Integer Block Container +++++");
		System.out.println(integerBC);
		System.out.println("+++++ Removing element +++++");
		integerBC.remove(lastInteger);
		System.out.println("+++++ Sorting container +++++");
		integerBC.sort();
		System.out.println(integerBC);
		BlockContainer<String> stringBC = new BlockContainer<String>(size);
		String lastString = null;
		for(int i = 0; i < n; ++i) {
			String element = scanner.next();
			lastString = element;
			stringBC.add(element);
		}
		System.out.println("+++++ String Block Container +++++");
		System.out.println(stringBC);
		System.out.println("+++++ Removing element +++++");
		stringBC.remove(lastString);
		System.out.println("+++++ Sorting container +++++");
		stringBC.sort();
		System.out.println(stringBC);
	}
}

class BlockContainer<T extends Comparable<T>> {
	private List<Set<T>> elements;
	private int n;

	public BlockContainer(int n) {
		this.n = n;
		elements = new ArrayList<Set<T>>();
	}

	public void add(T a) {
		if (elements.size() == 0) {
			Set<T> s = new TreeSet<T>();
			s.add(a);
			elements.add(s);
		} else {
			Set<T> s = elements.get(elements.size() - 1);
			if (s.size() < n) {
				s.add(a);
			} else {
				s = new TreeSet<T>();
				s.add(a);
				elements.add(s);
			}
		}
	}

	public boolean remove(T a) {
		boolean res = false;
		if (elements.size() > 0) {
			Set<T> s = elements.get(elements.size() - 1);
			res = s.remove(a);
			if (s.size() == 0) {
				elements.remove(elements.size() - 1);
			}
		}
		return res;
	}

	public void sort() {
		ArrayList<T> all = new ArrayList<T>();
		for (int i = 0; i < elements.size(); ++i) {
			Iterator<T> it = elements.get(i).iterator();
			while (it.hasNext()) {
				all.add(it.next());
			}
		}
		Collections.sort(all);
        elements = new ArrayList<Set<T>>();
		for (T element : all) {
			add(element);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < elements.size(); ++i) {
			sb.append(elements.get(i).toString());
			if (i < elements.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
