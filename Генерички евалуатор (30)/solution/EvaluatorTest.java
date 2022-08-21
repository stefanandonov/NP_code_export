import java.util.Scanner;
import java.util.Arrays;
//import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


interface IEvaluator<T extends Comparable<T>> {
    boolean evaluate(T a, T b);
}

class IEvaluatorsBuilder {

    static <T extends Comparable<T>> IEvaluator<T> build(String operator) {

        switch (operator) {
            case "==":
                return (l, r) -> l.compareTo(r) == 0;
            case "<":
                return (l, r) -> l.compareTo(r) < 0;
            case ">":
                return (l, r) -> l.compareTo(r) > 0;
            case "!=":
                return (l, r) -> l.compareTo(r) != 0;
            default:
                return (l,r) -> false;
        }
    }
}

class Evaluator {

    public  static <T extends Comparable<T>> boolean evaluateExpression(T a, T b, String operator) {

        IEvaluator<T> evaluator = IEvaluatorsBuilder.build(operator);
        return evaluator.evaluate(a,b);
    }
}

public class EvaluatorTest {

    

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");
            String operator = parts[2];

            if (parts[0].equals("1")) { //Integers
                Integer left = Integer.valueOf(parts[1]);
                Integer right = Integer.valueOf(parts[3]);
                System.out.println(Evaluator.evaluateExpression(left,right,operator));

            }
            else if (parts[0].equals("2")) { //Double
                Double left = Double.valueOf(parts[1]);
                Double right = Double.valueOf(parts[3]);
                System.out.println(Evaluator.evaluateExpression(left,right,operator));
            }
            else if (parts[0].equals("3")) { //Characters
                Character left = parts[1].charAt(0);
                Character right = parts[3].charAt(0);
                System.out.println(Evaluator.evaluateExpression(left,right,operator));
            }
            else if (parts[0].equals("4")) { //String
                System.out.println(Evaluator.evaluateExpression(parts[1],parts[3],operator));
            }
        }

    }
}