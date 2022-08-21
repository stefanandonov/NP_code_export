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
// vasiot kod ovde
