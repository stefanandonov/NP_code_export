import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ComponentTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String name = scanner.nextLine();
		Window window = new Window(name);
		Component prev = null;
		while (true) {
            try {
				int what = scanner.nextInt();
				scanner.nextLine();
				if (what == 0) {
					int position = scanner.nextInt();
					window.addComponent(position, prev);
				} else if (what == 1) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev = component;
				} else if (what == 2) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev.addComponent(component);
                    prev = component;
				} else if (what == 3) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev.addComponent(component);
				} else if(what == 4) {
                	break;
                }
                
            } catch (InvalidPositionException e) {
				System.out.println(e.getMessage());
			}
            scanner.nextLine();			
		}
		
        System.out.println("=== ORIGINAL WINDOW ===");
		System.out.println(window);
		int weight = scanner.nextInt();
		scanner.nextLine();
		String color = scanner.nextLine();
		window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
		System.out.println(window);
		int pos1 = scanner.nextInt();
		int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
		window.swichComponents(pos1, pos2);
		System.out.println(window);
	}
}

class Component implements Comparable<Component> {
	String color;
	int weight;
	Set<Component> inner;

	public Component(String color, int weight) {
		this.color = color;
		this.weight = weight;
		this.inner = new TreeSet<Component>();
	}

	public void addComponent(Component component) {
		inner.add(component);
	}

	public void changeColor(int weight, String color) {
		if(this.weight < weight) {
			this.color = color;
		}
		for (Component composite : inner) {
			change(composite, weight, color);
		}
	}

	static void change(Component component, int weight, String color) {
		if(component.weight < weight) {
			component.color = color;
		}
		for (Component c : component.inner) {
			change(c, weight, color);
		}
	}

	static void createString(StringBuilder sb, Component component, int level) {
		for (int i = 0; i < level; ++i)
			sb.append("---");
		sb.append(String.format("%d:%s\n", component.weight, component.color));
		for(Component c : component.inner) {
			createString(sb, c, level + 1);
		}
	}

	@Override
	public int compareTo(Component o) {
		int w = this.weight - o.weight;
		if(w == 0) return this.color.compareTo(o.color);
		return w;
	}
}

class Window {
	String name;
	Map<Integer, Component> components;

	public Window(String name) {
		this.name = name;
		this.components = new TreeMap<Integer, Component>();
	}

	public void addComponent(int position, Component component)
			throws InvalidPositionException {
		if (components.containsKey(position)) {
			throw new InvalidPositionException(position);
		}
		components.put(position, component);
	}

	public void swichComponents(int pos1, int pos2) {
		Component c1 = components.get(pos1);
		Component c2 = components.get(pos2);        
		components.put(pos1, c2);
		components.put(pos2, c1);
	}

	public void changeColor(int weight, String color) {
		for (Component c : components.values()) {
			c.changeColor(weight, color);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("WINDOW %s\n", name));
		for (Entry<Integer, Component> entry : components.entrySet()) {
			sb.append(String.format("%d:", entry.getKey()));
			Component.createString(sb, entry.getValue(), 0);
		}
		return sb.toString();
	}

}

class InvalidPositionException extends Exception {

	public InvalidPositionException(int position) {
		super(String.format("Invalid position %d, alredy taken!", position));
	}
}
