import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;


class SuperString {
	
	private LinkedList<String> list;
	
	private LinkedList<Integer> undo_stack;
	
	
	public SuperString () {
		list = new LinkedList<String>();
		undo_stack = new LinkedList<Integer>();
	}
	
	public void append(String s) {
		list.addLast(s);
		undo_stack.addFirst(1);
	}

	public void insert(String s) {
		list.addFirst(s);
		undo_stack.addFirst(-1);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for ( String s : list )	sb.append(s);
		return sb.toString();
	}
	
	public boolean contains(String s){
		return toString().contains(s);
	}
	
	public void reverse() {
		for ( ListIterator<String> it = list.listIterator() ; it.hasNext() ; ) 
			it.set(new StringBuilder(it.next()).reverse().toString());
		Collections.reverse(list);
        for ( ListIterator<Integer> it = undo_stack.listIterator() ; it.hasNext() ; ) 
			it.set(it.next()*(-1));
	}
	
	public void removeLast(int k) {
		for ( int i = 0 ; i < k ; ++i )
			if ( undo_stack.removeFirst() < 0 ) list.removeFirst();
			else list.removeLast();
	}
	
}

/*
 
 Test cases:

 	
 	0
 	1 ej
 	0 Gaj
 	1 r
 	0 du
 	1 d
 	1 n
 	1 A
 	0 K
 	4
 	6
 	
 	
 	0
 	1 ej
 	0 Gaj
 	1 r
 	0 du
 	1 d
 	1 n
 	1 A
 	0 K
 	2 aj
 	2 Gaj
 	2 Gajd
 	2 Gajdu
 	2 GajduK
 	2 Gajduk
 	2 Gajdur
 	2 Gajdun
 	2 ejGajrdudnAK
 	2 An
 	2 Kassandra
 	2 dnA
 	2 Kand
 	2 ejGaj
 	6
 	
 	
 	0
 	0 st
 	0 arz
 	0 andrej
 	1 st
 	0 arz
 	1 andr
 	0 ej
 	4
 	5 1
  	4
 	0 asd
 	1 qwe
 	1 ert
 	1 yui
 	4
 	5 2
 	4
  	0 aa
 	0 aadv
 	0 vbn
 	1 123
 	1 456
 	1 789
 	0 147
 	1 258
 	0 369
 	0 723
 	4
 	5 5
 	4
 	3 
 	4
 	5 2
 	4
 	6
 	
 
 
 */

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


/*
 
 Test cases:

 	
 	0
 	1 ej
 	0 Gaj
 	1 r
 	0 du
 	1 d
 	1 n
 	1 A
 	0 K
 	4
 	6
 	
 	
 	0
 	1 ej
 	0 Gaj
 	1 r
 	0 du
 	1 d
 	1 n
 	1 A
 	0 K
 	2 aj
 	2 Gaj
 	2 Gajd
 	2 Gajdu
 	2 GajduK
 	2 Gajduk
 	2 Gajdur
 	2 Gajdun
 	2 ejGajrdudnAK
 	2 An
 	2 Kassandra
 	2 dnA
 	2 Kand
 	2 ejGaj
 	6
 	
 	
 	0
 	0 st
 	0 arz
 	0 andrej
 	1 st
 	0 arz
 	1 andr
 	0 ej
 	4
 	5 1
  	4
 	0 asd
 	1 qwe
 	1 ert
 	1 yui
 	4
 	5 2
 	4
  	0 aa
 	0 aadv
 	0 vbn
 	1 123
 	1 456
 	1 789
 	0 147
 	1 258
 	0 369
 	0 723
 	4
 	5 5
 	4
 	3 
 	4
 	5 2
 	4
 	6
 	
 
 
 */
