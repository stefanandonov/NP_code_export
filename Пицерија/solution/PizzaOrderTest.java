import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * test cases
 * <p>
 * 0 Pizza Standard
 * 0 Extra Ketchup
 * 0 Pizza Fake
 * 0 Extra Fake
 * 1 Pizza Standard 2 Pizza Vegetarian 1 Extra Coke 3 Extra Coke stop Extra Coke stop
 * 1 Pizza Standard 2 Pizza Vegetarian 1 Extra Coke 3 Extra Coke stop Pizza Standard 1 Extra Coke 2
 * 2 Pizza Standard 1 Pizza Vegetarian 1 Pizza Pepperoni 1 Extra Coke 2 Extra Ketchup 2 Extra Coke stop 5 1 3 stop
 */
interface Item {

    boolean isPizza();

    int getPrice();

    String getType();
}

enum ExtraType {
    Coke(0), OrangeJuice(1), Ketchup(2);

    private int value;
    private int cost;

    ExtraType(int value) {
        this.value = value;
        if (value == 0) cost = 5;
        else if (value == 1) cost = 8;
        else if (value == 2) cost = 3;
    }

    public int getCost() {
        return cost;
    }

    public int getValue() {
        return value;
    }

}

enum PizzaType {
    Standard(0), Pepperoni(1), Extra_cheese(2), Vegetarian(3);
    private int value;
    private int cost;

    PizzaType(int value) {
        this.value = value;
        switch (value) {
            case 0:
                cost = 10;
                break;
            case 1:
                cost = 12;
                break;
            case 2:
                cost = 15;
                break;
            case 3:
                cost = 8;
                break;
        }
    }

    public int getCost() {
        return cost;
    }

    public int getValue() {
        return value;
    }

}

class EmptyOrder extends Exception {

    public EmptyOrder() {
        this("The order must not be empty");
    }

    public EmptyOrder(String message) {
        super(message);
    }

}


class ExtraItem implements Item {

    private ExtraType type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        try {
            this.type = ExtraType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidExtraTypeException(type);
        }
    }

    public ExtraItem(ExtraType type) {
        this.type = type;
    }

    public int getPrice() {
        return type.getCost();
    }

    public boolean isPizza() {
        return false;
    }

    @Override
    public String getType() {
        return type.name();
    }

}

class PizzaItem implements Item {

    private PizzaType type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        try {
            this.type = PizzaType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        return type.getCost();
    }

    public boolean isPizza() {
        return true;
    }

    @Override
    public String getType() {
        return type.name();
    }

}


class InvalidExtraTypeException extends Exception {

    private static final long serialVersionUID = -1906250852963017746L;

    public InvalidExtraTypeException(String type) {
        super(type);
    }

    public InvalidExtraTypeException() {
        this("Nepoznat tip");
    }
}

class ItemOutOfStockException extends Exception {

    private Item item;

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }

    public ItemOutOfStockException() {
        this("Unknown item is out of stock");
    }

    public ItemOutOfStockException(String message) {
        super(message);
    }

    public Item getItem() {
        return item;
    }

}

class Order {

    private class OrderItem {
        private final Item item;
        private int count;

        public Item getItem() {
            return item;
        }

        public int getCount() {
            return count;
        }

        public OrderItem(Item item, int count) {
            this.item = item;
            this.count = count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return getItem().getPrice() * getCount();
        }

    }

    private ArrayList<OrderItem> items;

    private boolean locked;

    public Order() {
        items = new ArrayList<>();
        locked = false;
    }

    public void addItem(Item item, int count) throws OrderLockedException, ItemOutOfStockException {
        if (locked) {
            throw new OrderLockedException();
        }
        if (count > 10) {
            throw new ItemOutOfStockException(item);
        }
        Optional<OrderItem> orderItem = items.stream()
                .filter(each -> each.getItem().getType().equals(item.getType()))
                .findFirst();
        if (orderItem.isPresent()) {
            orderItem.ifPresent(oi -> oi.setCount(count));
            return;
        }
        items.add(new OrderItem(item, count));
    }

    public void removeItem(int index) throws OrderLockedException {
        if (locked) {
            throw new OrderLockedException();
        }
        items.remove(index);
    }

    public int getPrice() {
        return items.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }

    public void displayOrder() {
        IntStream.range(0, items.size())
                .forEach(i -> {
                    OrderItem order = items.get(i);
                    System.out.printf("%3d.%-15sx%2d%5d$\n", i + 1, order.getItem().getType(),
                            order.getCount(), order.getPrice());
                });
        System.out.printf("%-22s%5d$\n", "Total:", getPrice());
    }

    public void lock() throws EmptyOrder {
        if (items.size() == 0) {
            throw new EmptyOrder();
        }
        locked = true;
    }


}

class InvalidPizzaTypeException extends Exception {

    public InvalidPizzaTypeException() {
    }

}


class OrderLockedException extends Exception {

    private static final long serialVersionUID = 1L;

    public OrderLockedException() {
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
