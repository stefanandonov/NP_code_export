import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

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

class Audition {
	HashMap<String, HashSet<Participant>> participants;

	public Audition() {
		participants = new HashMap<String, HashSet<Participant>>();
	}

	public void addParticpant(String city, String code, String name, int age) {
		Participant participant = new Participant(code, name, age);
		HashSet<Participant> set = participants.get(city);
		if (set == null) {
			set = new HashSet<Participant>();
			participants.put(city, set);
		}
		set.add(participant);
	}

	public void listByCity(String city) {
		HashSet<Participant> byCity = participants.get(city);
		List<Participant> list = new ArrayList<Participant>(byCity.size());
		for (Participant p : byCity) {
			list.add(p);
		}
		Collections.sort(list);
		for (Participant p : list) {
			System.out.println(p);
		}
	}
}

class Participant implements Comparable<Participant> {
	String code;
	String name;
	int age;

	public Participant(String code, String name, int age) {
		this.code = code;
		this.name = name;
		this.age = age;
	}

	@Override
	public boolean equals(Object obj) {
		Participant p = (Participant) obj;
		return code.equals(p.code);
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s %s %d", code, name, age);
	}

	@Override
	public int compareTo(Participant o) {
		int x = name.compareTo(o.name);
        if(x == 0) {
            int y = Integer.compare(age, o.age);
            if(y==0) {
                return code.compareTo(o.code);
            } 
            return y;
        }
        
        return x;
	}

}
