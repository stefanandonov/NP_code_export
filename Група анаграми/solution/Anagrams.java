import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {
	public static void findAll(InputStream inputStream) {
		Scanner fileScanner = null;
		Map<String, Set<String>> anagrams = new TreeMap<String, Set<String>>();
                ArrayList<String> order = new ArrayList<String>();
		fileScanner = new Scanner(inputStream);
		while (fileScanner.hasNextLine()) {
			String word = fileScanner.nextLine();
			char[] w = word.toCharArray();
			Arrays.sort(w);
			String rep = new String(w);
			if (anagrams.containsKey(rep)) {
				Set<String> words = anagrams.get(rep);
				words.add(word);
			} else {
                            order.add(rep);
                            Set<String> words = new TreeSet<String>();
				words.add(word);
				anagrams.put(rep, words);
			}
		}
		fileScanner.close();
		ListIterator<String> it = order.listIterator();
		while (it.hasNext()) {
			Set<String> values = anagrams.get(it.next());
			if (values.size() >= 5) {
				Iterator<String> setIt = values.iterator();
				while (setIt.hasNext()) {
					System.out.print(setIt.next());
					if(setIt.hasNext()) {
						System.out.print(" ");
					}
				}
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		findAll(System.in);
	}
}