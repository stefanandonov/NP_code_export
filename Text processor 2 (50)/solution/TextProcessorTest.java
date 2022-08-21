//package mk.ukim.finki.primeri;

import javax.xml.soap.Text;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class CosineSimilarityCalculator {
    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
        int [] array1;
        int [] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1=0, down2=0;

        for (int i=0;i<c1.size();i++) {
            up+=(array1[i] * array2[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down1+=(array1[i]*array1[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down2+=(array2[i]*array2[i]);
        }

        return up/(Math.sqrt(down1)*Math.sqrt(down2));
    }
}

class TextProcessor {
    Map<String, Integer> allWordsMap;
    List<Map<String, Integer>> wordsByDocumentsMaps;
    List<String> rawTexts;

    TextProcessor() {
        allWordsMap = new TreeMap<>();
        wordsByDocumentsMaps = new ArrayList<>();
        rawTexts = new ArrayList<>();
    }

    public void readText(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            Map<String, Integer> wordsMapForText = new TreeMap<>();
            String line = sc.nextLine();
            if (line.equals("END"))
                break;
            rawTexts.add(line);
            line = line.replaceAll("[^A-Za-z\\s+]", "");
            line = line.toLowerCase();
            String[] words = line.split("\\s+");
            Arrays.stream(words)
                    .forEach(word -> {
                        fillMap(wordsMapForText, word);
                        fillMap(allWordsMap, word);
                    });

            wordsByDocumentsMaps.add(wordsMapForText);
        }
        fillMaps();
    }

    private void fillMap(Map<String, Integer> map, String word) {
        map.putIfAbsent(word, 0);
        map.computeIfPresent(word, (k, v) -> {
            v++;
            return v;
        });
    }

    private void fillMaps() {
        allWordsMap.keySet().forEach(word -> {
            wordsByDocumentsMaps.forEach(map -> {
                map.putIfAbsent(word, 0);
            });
        });
    }

    public void printTextsVectors (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        wordsByDocumentsMaps.stream().map(Map::values).forEach(pw::println);
        pw.flush();
    }

    public void printCorpus(OutputStream os, int n, boolean ascending) {

        PrintWriter pw = new PrintWriter(os);
        allWordsMap.entrySet().stream()
                .sorted(ascending ? Map.Entry.comparingByValue(Comparator.naturalOrder()) : Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .forEach(entry -> pw.println(String.format("%s : %d", entry.getKey(), entry.getValue())));
        pw.flush();
    }

    public void mostSimilarTexts (OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        double maxSimilarity = 0;
        int iMax=0, jMax = 0;
        for (int i=0;i<rawTexts.size();i++) {
            for (int j=0;j<rawTexts.size();j++) {
                if (i != j) {
                    double sim = CosineSimilarityCalculator.cosineSimilarity(
                            wordsByDocumentsMaps.get(i).values(),
                            wordsByDocumentsMaps.get(j).values()
                    );
                    //System.out.println(String.format("%d %d %.10f", i, j, sim));
                    if (sim>maxSimilarity) {
                        maxSimilarity = sim;
                        iMax = i;
                        jMax = j;
                    }
                }
            }
        }

        pw.println(rawTexts.get(iMax));
        pw.println(rawTexts.get(jMax));
        pw.println(String.format("%.10f", maxSimilarity));

        pw.flush();
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out,  20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}
