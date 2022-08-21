import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EventCalendarTest {
	public static void main(String[] args) throws ParseException {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();
		int year = scanner.nextInt();
		scanner.nextLine();
		EventCalendar eventCalendar = new EventCalendar(year);
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		for (int i = 0; i < n; ++i) {
			String line = scanner.nextLine();
			String[] parts = line.split(";");
			String name = parts[0];
			String location = parts[1];
			Date date = df.parse(parts[2]);
			try {
				eventCalendar.addEvent(name, location, date);
			} catch (WrongDateException e) {
				System.out.println(e.getMessage());
			}
		}
		Date date = df.parse(scanner.nextLine());
		eventCalendar.listEvents(date);
		eventCalendar.listByMonth();
	}
}

class EventCalendar {
	private int year;
	private Map<Integer, Set<Event>> events;
	private Map<Integer, Integer> months;

	public EventCalendar(int year) {
		this.year = year;
		this.events = new HashMap<Integer, Set<Event>>();
		this.months = new HashMap<Integer, Integer>();
	}

	public void addEvent(String name, String location, Date date)
			throws WrongDateException {
		int year = getYear(date);
		if (year != this.year)
			throw new WrongDateException(date);
		Event event = new Event(name, location, date);
		int day = getDayOfYear(date);
		Set<Event> list = events.get(day);
		if (list == null) {
			list = new TreeSet<Event>();
		}
		list.add(event);
		int monthKey = getMonth(date);
		Integer count = months.get(monthKey);
		if (count == null) {
			count = 0;
		}
		++count;
		months.put(monthKey, count);
		events.put(day, list);
	}

	static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public void listEvents(Date date) {
		int day = getDayOfYear(date);
		Set<Event> list = events.get(day);
		if (list != null) {
			for (Event e : list) {
				System.out.println(e);
			}
        } else {
            System.out.println("No events on this day!");
        }
	}

	public void listByMonth() {
		for (int i = 0; i < 12; ++i) {
			System.out.printf("%d : %d\n", i + 1, months.get(i) == null ? 0
					: months.get(i));
		}
	}
}

class Event implements Comparable<Event> {
	private String name;
	private String location;
	private Date date;

	public Event(String name, String location, Date date) {
		this.name = name;
		this.location = location;
		this.date = date;
	}

	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
		return String.format("%s at %s, %s", df.format(date), location, name);
	}

	@Override
	public int compareTo(Event o) {
		int c = date.compareTo(o.date);
        if(c == 0) return name.compareTo(o.name);
        return c;
	}
}

class WrongDateException extends Exception {
	public WrongDateException(Date date) {
		super(String.format("Wrong date: %s", date));
	}
}
