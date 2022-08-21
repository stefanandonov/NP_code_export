import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CarTest {
	public static void main(String[] args) {
		CarCollection carCollection = new CarCollection();
		String manufacturer = fillCollection(carCollection);
		carCollection.sortByPrice(true);
		System.out.println("=== Sorted By Price ASC ===");
		print(carCollection.getList());
		carCollection.sortByPrice(false);
		System.out.println("=== Sorted By Price DESC ===");
		print(carCollection.getList());
		System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
		List<Car> result = carCollection.filterByManufacturer(manufacturer);
		print(result);
	}

	static void print(List<Car> cars) {
		for (Car c : cars) {
			System.out.println(c);
		}
	}

	static String fillCollection(CarCollection cc) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
			Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
					Float.parseFloat(parts[3]));
			cc.addCar(car);
		}
        scanner.close();
		return "";
	}
}

class CarCollection {
	private List<Car> cars;

	public CarCollection() {
		cars = new ArrayList<Car>();
	}

	public void addCar(Car car) {
		cars.add(car);
	}

	public void sortByPrice(boolean asscending) {
		Comparator<Car> carPriceComparator = new CarPriceComparator();
		if (!asscending) {
			carPriceComparator = Collections.reverseOrder(carPriceComparator);
		}
		Collections.sort(cars, carPriceComparator);
	}

	public List<Car> getList() {
		return cars;
	}

	public List<Car> filterByManufacturer(String manufacturer) {
		List<Car> result = new ArrayList<Car>();
		for (Car c : cars) {
			if (c.getManufacturer().equalsIgnoreCase(manufacturer)) {
				result.add(c);
			}
		}
        Collections.sort(result, new CarModelComparator());
		return result;
	}

}

class Car {
	private String model;
	private String manufacturer;
	private int price;
	private float power;

	public Car(String manufacturer, String model, int price, float power) {
		this.model = model;
		this.manufacturer = manufacturer;
		this.price = price;
		this.power = power;
	}

	public String getModel() {
		return model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public int getPrice() {
		return price;
	}
    
    public float getPower() {
        return power;
    }

	@Override
	public String toString() {
		return String.format("%s %s (%.0fKW) %d", manufacturer, model, power,
				price);
	}
}

class CarPriceComparator implements Comparator<Car> {

	@Override
	public int compare(Car c1, Car c2) {
		int p = c1.getPrice() - c2.getPrice();
        if(p == 0) {
            return (int)(c1.getPower() - c2.getPower());
        } else {
            return p;
        }
	}
}
class CarModelComparator implements Comparator<Car> {

	@Override
	public int compare(Car c1, Car c2) {
		return c1.getModel().compareTo(c2.getModel());
	}
}