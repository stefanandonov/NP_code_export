import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class TermFrequencyTest {
	public static void main(String[] args) throws FileNotFoundException {
		String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
				"ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
				"што", "на", "а", "но", "кој", "ја" };
		TermFrequency tf = new TermFrequency(System.in,
				stop);
		System.out.println(tf.countTotal());
		System.out.println(tf.countDistinct());
		System.out.println(tf.mostOften(10));
	}
}

class TermFrequency {
	Map<String, Integer> frequency;
	Set<String> stop;

	public TermFrequency(InputStream inputStream, String[] stopWords) {
		frequency = new TreeMap<String, Integer>();
		stop = new HashSet<String>();
		for (String w : stopWords) {
			stop.add(w);
		}
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			line = line.trim();
			if (line.length() > 0) {
				String[] words = line.split(" ");
				for (String word : words) {
					String key = normalize(word);
					if (key.isEmpty() || stop.contains(key)) {
						continue;
					}
					if (frequency.containsKey(key)) {
						Integer count = frequency.get(key);
						frequency.put(key, count + 1);
					} else {
						frequency.put(key, 1);
					}
				}
			}
		}
		scanner.close();
	}

	private static String normalize(String word) {
		return word.toLowerCase().replace(",", "").replace(".", "").trim();
	}

	public int countTotal() {
		int total = 0;
		for (Integer count : frequency.values()) {
			total += count;
		}
		return total;
	}

	public int countDistinct() {
		return frequency.keySet().size();
	}

	public List<String> mostOften(int k) {
		List<String> list = new ArrayList<String>();
		SortedSet<Map.Entry<String, Integer>> sorted = entriesSortedByValues(frequency);
		for (Entry<String, Integer> entry : sorted) {
			list.add(entry.getKey());
			--k;
			if (k == 0) {
				break;
			}
		}
		return list;
	}

	static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
				new Comparator<Map.Entry<K, V>>() {
					@Override
					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
						int res = e1.getValue().compareTo(e2.getValue());
						return res != 0 ? -res : 1;
					}
				});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
}
