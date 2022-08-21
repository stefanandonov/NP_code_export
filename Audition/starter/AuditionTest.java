import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuditionTest {
	public static void main(String[] args) {
		Audition audition = new Audition();
		List<String> cities = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(";");
			if (parts.length > 1) {
				audition.addParticpant(parts[0], parts[1], parts[2],
						Integer.parseInt(parts[3]));
			} else {
				cities.add(line);
			}
		}
		for (String city : cities) {
			System.out.printf("+++++ %s +++++\n", city);
			audition.listByCity(city);
		}
		scanner.close();
	}
}