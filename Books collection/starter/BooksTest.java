import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class BooksTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();
		BookCollection booksCollection = new BookCollection();
		Set<String> categories = fillCollection(scanner, booksCollection);
		System.out.println("=== PRINT BY CATEGORY ===");
		for (String category : categories) {
			System.out.println("CATEGORY: " + category);
			booksCollection.printByCategory(category);
		}
		System.out.println("=== TOP N BY PRICE ===");
		print(booksCollection.getCheapestN(n));
	}

	static void print(List<Book> books) {
		for (Book book : books) {
			System.out.println(book);
		}
	}

	static TreeSet<String> fillCollection(Scanner scanner,
			BookCollection collection) {
		TreeSet<String> categories = new TreeSet<String>();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String[] parts = line.split(":");
			Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
			collection.addBook(book);
			categories.add(parts[1]);
		}
		return categories;
	}
}

// Вашиот код овде