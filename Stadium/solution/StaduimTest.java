import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

class Stadium {
	String name;
	HashMap<String, Sector> sectors;

	public Stadium(String name) {
		this.name = name;
		sectors = new HashMap<String, Sector>();
	}

	public void createSectors(String[] sectorNames, int[] sectorSizes) {
		for (int i = 0; i < sectorNames.length; ++i) {
			addSector(sectorNames[i], sectorSizes[i]);
		}
	}

	void addSector(String name, int size) {
		Sector sector = new Sector(name, size);
		sectors.put(name, sector);
	}
	
	public void buyTicket(String sectorName, int seat, int type)
			throws SeatNotAllowedException, SeatTakenException {
		Sector sector = sectors.get(sectorName);
		if (sector.isTaken(seat))
			throw new SeatTakenException();
		sector.takeSeat(seat, type);
	}

	public void showSectors() {
		List<Sector> sectorsList = new ArrayList<Sector>(sectors.values());
		Collections.sort(sectorsList);
		for (Sector sector : sectorsList) {
			System.out.println(sector);
		}
	}
	
}

class Sector implements Comparable<Sector> {
	String name;
	int size;
	HashMap<Integer, Integer> taken;
	HashSet<Integer> types;

	public Sector(String name, int size) {
		this.name = name;
		this.size = size;
		taken = new HashMap<Integer, Integer>();
		types = new HashSet<Integer>();
	}

	int free() {
		return size - taken.size();
	}

	public void takeSeat(int seat, int type) throws SeatNotAllowedException {
		if (type == 1) {
			if (types.contains(2))
				throw new SeatNotAllowedException();
		} else if (type == 2) {
			if (types.contains(1))
				throw new SeatNotAllowedException();
		}
		types.add(type);
		taken.put(seat, type);
	}

	public boolean isTaken(int seat) {
		return taken.containsKey(seat);
	}

	@Override
	public int compareTo(Sector o) {
		int s1 = free();
		int s2 = o.free();
		if (s1 == s2) {
			return name.compareTo(o.name);
		}
		return Integer.compare(s2, s1);
	}

	@Override
	public String toString() {
		return String.format("%s\t%d/%d\t%.1f%%", name, free(), size,
				(size - free()) * 100.0 / size);
	}

}

class SeatNotAllowedException extends Exception {

}

class SeatTakenException extends Exception {

}