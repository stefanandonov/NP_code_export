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

class GenericFraction<T extends Number, U extends Number> {

	private T numerator;
	private U denominator;

	public GenericFraction(T numerator, U denominator)
			throws ZeroDenominatorException {
		if (denominator.doubleValue() == 0) {
			throw new ZeroDenominatorException();
		}
		this.numerator = numerator;
		this.denominator = denominator;

	}

	double toDouble() {
		return this.numerator.doubleValue() / this.denominator.doubleValue();
	}

	static double gcd(double a, double b) {
		if (b == 0)
			return a;
		if (a < b)
			return gcd(a, b - a);
		else
			return gcd(b, a - b);
	}

	public double gcd() {
		return gcd(this.numerator.doubleValue(), this.denominator.doubleValue());
	}

	public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf)
			throws ZeroDenominatorException {
		return new GenericFraction<Double, Double>(this.numerator.doubleValue()
				* gf.denominator.doubleValue() + this.denominator.doubleValue()
				* gf.numerator.doubleValue(), this.denominator.doubleValue()
				* gf.denominator.doubleValue());

	}

	@Override
	public String toString() {
		double gcd = this.gcd();
		return String.format("%.2f / %.2f", this.numerator.doubleValue() / gcd,
				this.denominator.doubleValue() / gcd);
	}

	public static void main(String[] args) throws ZeroDenominatorException {

	}
}

class ZeroDenominatorException extends Exception {
	public ZeroDenominatorException() {
		super("Denominator cannot be zero");
	}
}