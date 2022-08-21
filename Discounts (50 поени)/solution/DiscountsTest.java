import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

class Discounts {
    List<Store> stores;

    public Discounts() {
    }

    public int readStores(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        stores = reader.lines().map(Store::ofLine).collect(Collectors.toList());
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).reversed().thenComparing(Store::getName))
            	.limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName))
            	.limit(3)
                .collect(Collectors.toList());
    }
}

class Store {
    String name;
    List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public static Store ofLine(String line) {
        String[] parts = line.split("\\s+");
        return new Store(parts[0], Arrays.stream(parts).skip(1)
                .map(Product::ofString)
                .collect(Collectors.toList()));
    }

    public double averageDiscount() {
        return products.stream()
                .mapToDouble(Product::discount)
                .average().orElse(0);
    }

    public int totalDiscount() {
        return products.stream()
                .mapToInt(Product::absoluteDiscount)
                .sum();
    }

    @Override
    public String toString() {
        String productsStr = products.stream()
                .sorted(Comparator.comparing(Product::discount)
                .thenComparing(Product::absoluteDiscount).reversed())
                .map(Product::toString)
                .collect(Collectors.joining("\n"));
        double rounded = Math.round(averageDiscount() * 10) / 10.;

        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n%s", name,
                rounded,
                totalDiscount(),
                productsStr);
    }
}

class Product {
    int price;
    int discountedPrice;

    public Product(int discountedPrice, int price) {
        this.discountedPrice = discountedPrice;
        this.price = price;
    }

    public static Product ofString(String product) {
        String[] parts = product.split(":");
        return new Product(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public int discount() {
        return (price - discountedPrice) * 100 / price;
    }

    public int absoluteDiscount() {
        return price - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", discount(), discountedPrice, price);
    }
}

