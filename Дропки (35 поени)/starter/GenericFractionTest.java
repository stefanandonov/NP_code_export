import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
        	GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
        	GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
        	GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }
        
        scanner.close();
    }

}

// вашиот код овде
