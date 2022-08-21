import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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

// vashiot kod ovde