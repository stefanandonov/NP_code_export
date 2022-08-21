import java.util.Scanner;

public class SuperStringTest {
	
	public static void main(String[] args) {
		Scanner jin = new Scanner(System.in);
		int k = jin.nextInt();
		if (  k == 0 ) {
			SuperString s = new SuperString();
			while ( true ) {
				int command = jin.nextInt();
				if ( command == 0 ) {//append(String s)
					s.append(jin.next());
				}
				if ( command == 1 ) {//insert(String s)
					s.insert(jin.next());
				}
				if ( command == 2 ) {//contains(String s)
					System.out.println(s.contains(jin.next()));
				}
				if ( command == 3 ) {//reverse()
					s.reverse();
				}
				if ( command == 4 ) {//toString()
					System.out.println(s);
				}
				if ( command == 5 ) {//removeLast(int k)
					s.removeLast(jin.nextInt());
				}
				if ( command == 6 ) {//end
					break;
				}
			}
		}
	}

}
