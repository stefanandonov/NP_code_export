import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class AmountNotAllowedException extends Exception {

    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

enum DDV_TYPE {
    A,
    B,
    V
}

class Item {
    int price;
    DDV_TYPE type;

    public Item(int price, DDV_TYPE type) {
        this.price = price;
        this.type = type;
    }

    public Item() {

    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(DDV_TYPE type) {
        this.type = type;
    }

    public double getTaxReturn() {
        switch (type) {
            case A:
                return price * 0.18 * 0.15;
            case B:
                return price * 0.05 * 0.15;
            default:
                return 0;
        }
    }
}

class Record {

    String id;
    List<Item> itemList;

    public Record(String id, List<Item> itemList) throws AmountNotAllowedException {
        this.id = id;
        int sum = itemList.stream().mapToInt(item -> item.price).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        this.itemList = itemList;
    }

    public static Record createInstance(String input) throws AmountNotAllowedException {
        String[] parts = input.split("\\s+");
        List<Item> itemList = new ArrayList<>();
        String id = parts[0];

        Item item = new Item();
        for (int i = 1; i < parts.length; i++) {
            if (i % 2 == 0) { //tax type
                item.setType(DDV_TYPE.valueOf(parts[i]));
                itemList.add(item);
                item = new Item();
            } else { //price
                item.setPrice(Integer.parseInt(parts[i]));
            }

        }

        return new Record(id, itemList);
    }

    public double getTaxReturn() {

        return itemList.stream()
                .mapToDouble(Item::getTaxReturn)
                .sum();
    }

    public String getId() {
        return id;
    }

    public int getSum() {
        return itemList.stream().mapToInt(item -> item.price).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", this.id, this.getSum(), this.getTaxReturn());
    }
}

class MojDDV {

    List<Record> records;

    public MojDDV() {
        records = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        records = br.lines().map(line -> {
            try {
                return Record.createInstance(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    void printSorted(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        //Comparator<Record> comparator = Comparator.comparingDouble(Record::getTaxReturn)
        //        .thenComparing(Record::getSum)
         //       .thenComparing(Record::getId);

       // records.sort(comparator);

        records.forEach(record -> pw.println(record.toString()));
        pw.flush();
    }

    void printStatistics(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        DoubleSummaryStatistics summaryStatistics = records.stream().mapToDouble(Record::getTaxReturn).summaryStatistics();

        pw.println(String.format("min:\t%05.03f\nmax:\t%05.03f\nsum:\t%05.03f\ncount:\t%-5d\navg:\t%05.03f",
                summaryStatistics.getMin(),
                summaryStatistics.getMax(),
                summaryStatistics.getSum(),
                summaryStatistics.getCount(),
                summaryStatistics.getAverage()));

        pw.flush();
    }

}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printSorted(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
