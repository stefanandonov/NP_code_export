import java.util.Scanner;

enum Color {
	RED, GREEN, BLUE
}
public class ShapesTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Canvas canvas = new Canvas();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			int type = Integer.parseInt(parts[0]);
			String id = parts[1];
			if (type == 1) {
                Color color = Color.valueOf(parts[2]);
				float radius = Float.parseFloat(parts[3]);
				canvas.add(id, color, radius);
			} else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
				float width = Float.parseFloat(parts[3]);
				float height = Float.parseFloat(parts[4]);
				canvas.add(id, color, width, height);
			} else if (type == 3) {
				float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
				System.out.print(canvas);
				canvas.scale(id, scaleFactor);
				System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
				System.out.print(canvas);
			}

		}
	}
}

enum Color {
	RED, GREEN, BLUE
}

class Canvas {

}