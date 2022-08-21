import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test
 */
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}

class WordVectors {
    Map<Word, Vector> wordVectorMap;
    List<Vector> vectors;

    final Comparator<Word> WORD_COMPARATOR = (left, right) -> {
        return left.word.compareTo(right.word);
    };

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        wordVectorMap = new TreeMap<>(WORD_COMPARATOR);
        IntStream.range(0, words.length)
                .forEach(i -> wordVectorMap.put(new Word(words[i]), new Vector(vectors.get(i))));
    }

    public void readWords(List<String> words) {
        vectors = words.stream().map(word ->
                wordVectorMap.getOrDefault(new Word(word), Vector.DEFAULT)
        ).collect(Collectors.toList());
    }

    public List<Integer> slidingWindow(int n) {
        return IntStream.range(0, vectors.size() - n + 1)
                .mapToObj(i ->
                        vectors.subList(i, i + n).stream()
                                .reduce(Vector.IDENTITY, Vector::sum)
                )
                .peek(v -> {
                    //System.out.println("SUM");
                    //System.out.println(v);
                })
                .map(Vector::max)
                .collect(Collectors.toList());
    }
}

class Word {
    final String word;

    public Word(String word) {
        this.word = word;
    }

    public int len() {
        return word.length();
    }

    public char at(int i) {
        return word.charAt(i);
    }
}

class Vector {
    static final Vector DEFAULT = new Vector(Arrays.asList(5, 5, 5, 5, 5));
    static final Vector IDENTITY = new Vector(Arrays.asList(0, 0, 0, 0, 0));
    List<Integer> numbers;

    public Vector(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public int max() {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .max().orElse(0);
    }

    public int size() {
        return numbers.size();
    }

    public int at(int i) {
        return numbers.get(i);
    }

    public Vector sum(Vector other) {
        return new Vector(IntStream.range(0, other.size())
                .map(i -> at(i) + other.at(i))
                .boxed()
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return numbers.toString();
    }
}
