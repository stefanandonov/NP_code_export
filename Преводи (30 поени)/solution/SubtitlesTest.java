import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubtitlesTest {
	public static void main(String[] args) {
		Subtitles subtitles = new Subtitles();
		int n = subtitles.loadSubtitles(System.in);
		System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
		subtitles.print();
		int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
		System.out.println(String.format("SHIFT FOR %d ms", shift));
		subtitles.shift(shift);
		System.out.println("+++++ SHIFTED SUBTITLES +++++");
		subtitles.print();
	}
}

class Subtitles {
	List<Element> elements;

	public Subtitles() {
		elements = new ArrayList<Element>();
	}

	public int loadSubtitles(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int number = Integer.parseInt(line);
			String time = scanner.nextLine();
			StringBuilder text = new StringBuilder();
			while (true) {
                if(!scanner.hasNextLine()) break;
				line = scanner.nextLine();
				if (line.trim().length() == 0)
					break;
				text.append(line);
				text.append("\n");
			}
			Element element = new Element(number, time, text.toString());
			elements.add(element);
		}
		return elements.size();
	}

	public void shift(int ms) {
		for (Element e : elements) {
			e.shift(ms);
		}
	}

	public void find(String text) {
		for (Element e : elements) {
			if (e.findText(text)) {
				System.out.println(e.number);
			}
		}
	}

	public void print() {
		for (Element e : elements) {
			System.out.println(e);
		}
	}
}

class Element {
	public int timeFrom;
	public int timeTo;
	public String text;
	public int number;

	public Element(int number, String time, String text) {
		this.number = number;
		String[] parts = time.split("-->");
		timeFrom = stringToTime(parts[0].trim());
		timeTo = stringToTime(parts[1].trim());
		this.text = text;
	}

	public void shift(int ms) {
		timeFrom += ms;
		timeTo += ms;
	}

	public boolean findText(String someText) {
		return text.contains(someText);
	}

	static int stringToTime(String time) {
		String[] parts = time.split(",");
		int res = Integer.parseInt(parts[1]);
		parts = parts[0].split(":");
		int sec = Integer.parseInt(parts[2]);
		int min = Integer.parseInt(parts[1]);
		int h = Integer.parseInt(parts[0]);
		res += sec * 1000;
		res += min * 60 * 1000;
		res += h * 60 * 60 * 1000;
		return res;
	}

	static String timeToString(int time) {
		int h = time / (60 * 60 * 1000);
		time = time % (60 * 60 * 1000);
		int m = time / (60 * 1000);
		time = time % (60 * 1000);
		int s = time / 1000;
		int ms = time % 1000;
		return String.format("%02d:%02d:%02d,%03d", h, m, s, ms);
	}

	@Override
	public String toString() {
		return String.format("%d\n%s --> %s\n%s", number,
				timeToString(timeFrom), timeToString(timeTo), text);
	}
}
