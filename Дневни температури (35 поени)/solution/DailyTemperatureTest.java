import java.io.*;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

class DailyTemperatures {
    List<DailyTemperature> temperatures;

    public DailyTemperatures() {
    }

    public void readTemperatures(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        temperatures = reader.lines()
                .map(DailyTemperature::fromString)
                .collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter writer = new PrintWriter(outputStream);
        temperatures.stream()
                .sorted(Comparator.comparing(DailyTemperature::getDay))
                .forEach(each -> writer.println(String.format("%s: %s", each, each.summary(scale))));
        writer.flush();
    }
}

class DailyTemperature {
    int day;
    List<Double> temperatures;
    DoubleSummaryStatistics doubleSummaryStatistics;

    public DailyTemperature(int day, List<Double> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
        doubleSummaryStatistics = temperatures.stream().collect(Collectors.summarizingDouble(x -> x));
    }

    public int getDay() {
        return day;
    }

    static double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    static double toFahrenheit(double celsius) {
        return (celsius * 9) / 5 + 32;
    }

    static double tempFromString(String temp) {
        if (temp.endsWith("C")) {
            return Double.parseDouble(temp.substring(0, temp.length() - 1));
        } else {
            return toCelsius(Double.parseDouble(temp.substring(0, temp.length() - 1)));
        }
    }

    static DailyTemperature fromString(String line) {
        String[] parts = line.split("\\s+");
        List<Double> list = IntStream.range(1, parts.length)
                .mapToObj(i -> tempFromString(parts[i]))
                .collect(Collectors.toList());
        return new DailyTemperature(Integer.parseInt(parts[0]), list);
    }

    public String summary(char c) {
        double min = doubleSummaryStatistics.getMin();
        double max = doubleSummaryStatistics.getMax();
        double average = doubleSummaryStatistics.getAverage();
        if (c == 'F') {
            min = toFahrenheit(min);
            max = toFahrenheit(max);
            average = toFahrenheit(average);
        }
        return String.format("Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c",
                doubleSummaryStatistics.getCount(),
                min, c,
                max, c,
                average, c);
    }

    @Override
    public String toString() {
        return String.format("%3d", day);
    }
}



