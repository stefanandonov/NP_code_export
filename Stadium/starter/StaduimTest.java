import java.util.Scanner;

public class StaduimTest {
		public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();
		String[] sectorNames = new String[n];
		int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
		for (int i = 0; i < n; ++i) {
			String line = scanner.nextLine();
			String[] parts = line.split(";");
			sectorNames[i] = parts[0];
			sectorSizes[i] = Integer.parseInt(parts[1]);
		}
		Stadium stadium = new Stadium(name);
		stadium.createSectors(sectorNames, sectorSizes);
		n = scanner.nextInt();
		scanner.nextLine();
		for (int i = 0; i < n; ++i) {
			String line = scanner.nextLine();
			String[] parts = line.split(";");
			try {
				stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
			} catch (SeatNotAllowedException e) {
				System.out.println("SeatNotAllowedException");
			} catch (SeatTakenException e) {
				System.out.println("SeatTakenException");
			}
		}
		stadium.showSectors();
	}
}
