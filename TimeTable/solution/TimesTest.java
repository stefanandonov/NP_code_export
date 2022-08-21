import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TimesTest {

	public static void main(String[] args) {
		TimeTable timeTable = new TimeTable();
		try {
			timeTable.readTimes(System.in);
		} catch (UnsupportedFormatException e) {
			System.out.println("UnsupportedFormatException: " + e.getMessage());
		} catch (InvalidTimeException e) {
			System.out.println("InvalidTimeException: " + e.getMessage());
		}
		System.out.println("24 HOUR FORMAT");
		timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
		System.out.println("AM/PM FORMAT");
		timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
	}

}

enum TimeFormat {
	FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
	public UnsupportedFormatException(String msg) {
		super(msg);
	}
}

class InvalidTimeException extends Exception {
	public InvalidTimeException(String msg) {
		super(msg);
	}
}

class TimeTable {
	List<Time> times;

	public TimeTable() {
		times = new ArrayList<Time>();
	}

	public void readTimes(InputStream input) throws UnsupportedFormatException,
			InvalidTimeException {
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			for (String p : parts) {
				Time time = new Time(p);
				times.add(time);
			}
		}
	}

	public void writeTimes(OutputStream output, TimeFormat format) {
		PrintWriter writer = new PrintWriter(output);
		Collections.sort(times);
		for (Time time : times) {
			if (format == TimeFormat.FORMAT_24) {
				writer.println(time);
			} else {
				writer.println(time.toStringAMPM());
			}
		}
		writer.flush();
	}
}

class Time implements Comparable<Time> {
	int hour;
	int minute;

	public Time(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public Time(String time) throws UnsupportedFormatException,
			InvalidTimeException {
		String[] parts = time.split("\\.");
		if (parts.length == 1) {
			parts = time.split(":");
		}
		if (parts.length == 1)
			throw new UnsupportedFormatException(time);
		this.hour = Integer.parseInt(parts[0]);
		this.minute = Integer.parseInt(parts[1]);
		if (hour < 0 || hour > 23 || minute < 0 || minute > 59)
			throw new InvalidTimeException(time);
	}

	public String toStringAMPM() {
		String part = "AM";
		int h = hour;
		if (h == 0) {
			h += 12;
		} else if (h == 12) {
			part = "PM";
		} else if (h > 12) {
			h -= 12;
			part = "PM";
		}
		return String.format("%2d:%02d %s", h, minute, part);
	}

	@Override
	public String toString() {
		return String.format("%2d:%02d", hour, minute);
	}

	@Override
	public int compareTo(Time o) {
		if (hour == o.hour)
			return minute - o.minute;
		else
			return hour - o.hour;
	}

}