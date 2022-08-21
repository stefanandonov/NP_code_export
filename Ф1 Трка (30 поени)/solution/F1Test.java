import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class F1Test {

	public static void main(String[] args) {
		F1Race f1Race = new F1Race();
		f1Race.readResults(System.in);
		f1Race.printSorted(System.out);
	}

}

class F1Race {
	private ArrayList<Driver> drivers;

	public F1Race() {
		drivers = new ArrayList<Driver>();
	}

	public void readResults(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			Driver driver = new Driver(parts[0], Driver.stringToTime(parts[1]),
					Driver.stringToTime(parts[2]),
					Driver.stringToTime(parts[3]));
			drivers.add(driver);
		}
		scanner.close();
	}

	public void printSorted(OutputStream outputStream) {
		Collections.sort(drivers);
		PrintWriter printWriter = new PrintWriter(outputStream);
        int i = 1;
		for(Driver driver : drivers) {
			printWriter.printf("%d. %s\n", i++, driver);
		}
		printWriter.close();
	}
}

class Driver implements Comparable<Driver> {
	private String name;
	private int lap1;
	private int lap2;
	private int lap3;
	private int best;

	public Driver(String name, int lap1, int lap2, int lap3) {
		this.name = name;
		this.lap1 = lap1;
		this.lap2 = lap2;
		this.lap3 = lap3;
		this.best = Math.min(Math.min(lap1, lap2), lap3);
	}

	public static int stringToTime(String time) {
		String[] parts = time.split(":");
		return Integer.parseInt(parts[0]) * 60 * 1000
				+ Integer.parseInt(parts[1]) * 1000
				+ Integer.parseInt(parts[2]);
	}

	public static String timeToString(int time) {
		int minutes = (time / 1000) / 60;
		int seconds = (time - minutes * 1000 * 60) / 1000;
		int ms = time % 1000;
		return String.format("%d:%02d:%03d", minutes, seconds, ms);
	}

	@Override
	public int compareTo(Driver o) {
		return this.best - o.best;
	}

	@Override
	public String toString() {
		return String.format("%-10s%10s", name, timeToString(best));
	}
}