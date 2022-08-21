import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

abstract class Shape implements Scaleble, Stackable {
	protected String id;
	protected Color color;

	public Shape(String id, Color color) {
		this.id = id;
		this.color = color;
	}
}

interface Stackable {
	public float weight();
}

interface Scaleble {
	public void scale(float scaleFactor);
}

class Circle extends Shape {
	float r;

	public Circle(String id, Color color, float r) {
		super(id, color);
		this.r = r;
	}

	@Override
	public void scale(float scaleFactor) {
		r *= scaleFactor;
	}

	@Override
	public float weight() {
		return (float) (Math.PI * r * r);
	}

	@Override
	public String toString() {
		return String.format("C: %-5s%-10s%10.2f\n", id, color, weight());
	}

}

class Rectangle extends Shape {
	float width, height;

	public Rectangle(String id, Color color, float width, float height) {
		super(id, color);
		this.width = width;
		this.height = height;
	}

	@Override
	public void scale(float scaleFactor) {
		width *= scaleFactor;
		height *= scaleFactor;
	}

	@Override
	public float weight() {
		return width * height;
	}

	@Override
	public String toString() {
		return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
	}

}

class Canvas {
	List<Shape> shapes;

	public Canvas() {
		shapes = new ArrayList<Shape>();
	}

	int find(float weight) {
		for (int i = 0; i < shapes.size(); ++i) {
			if (shapes.get(i).weight() < weight) {
				return i;
			}
		}
		return shapes.size();
	}

	public void add(String id, Color color, float radius) {
		Circle c = new Circle(id, color, radius);
		int index = find(c.weight());
		this.shapes.add(index, c);
	}

	public void add(String id, Color color, float width, float height) {
		Rectangle rect = new Rectangle(id, color, width, height);
		int index = find(rect.weight());
		this.shapes.add(index, rect);
	}

	public void scale(String id, float scaleFactor) {
		Shape s = null;
		for (int i = shapes.size() - 1; i >= 0; i--) {
			if (shapes.get(i).id.equals(id)) {
				s = shapes.get(i);
				shapes.remove(i);
				break;
			}
		}
		s.scale(scaleFactor);
		int index = find(s.weight());
		shapes.add(index, s);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Shape shape : shapes) {
			sb.append(shape);
		}
		return sb.toString();
	}

}