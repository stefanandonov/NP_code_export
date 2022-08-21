import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.TreeMap;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
        private final static TreeMap<Integer, String> NUMBERS = new TreeMap<>();

    static {
        NUMBERS.put(1000, "M");
        NUMBERS.put(900, "CM");
        NUMBERS.put(500, "D");
        NUMBERS.put(400, "CD");
        NUMBERS.put(100, "C");
        NUMBERS.put(90, "XC");
        NUMBERS.put(50, "L");
        NUMBERS.put(40, "XL");
        NUMBERS.put(10, "X");
        NUMBERS.put(9, "IX");
        NUMBERS.put(5, "V");
        NUMBERS.put(4, "IV");
        NUMBERS.put(1, "I");
    }

    /**
     * Roman to decimal converter
     *
     * @param number number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int number) {
        int l = NUMBERS.floorKey(number);
        if (number == l) {
            return NUMBERS.get(number);
        }
        return NUMBERS.get(l) + toRoman(number - l);
    }

}
