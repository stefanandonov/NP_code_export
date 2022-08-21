import java.util.Scanner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

class IntegerList {
	
	private LinkedList<Integer> list;
	
	public IntegerList() {
		list = new LinkedList<Integer>();
	}
	
	public IntegerList( Integer... numbers ) {
		this();
		list.addAll(Arrays.asList(numbers));
	}

	public void add(Integer el , int idx) {
		while ( idx > list.size() ) {
			list.add(0);
		}
		list.add(idx,el);
	}
	
	public int remove( int idx) {
		return list.remove(idx);
	}
	
	public void set(int el , int idx ) {
		list.set(idx, el);
	}
	
	public int get( int idx ) {
		return list.get(idx);
	}
	
	public int count(int el){
		int counter = 0;
		for ( int e : list ) if ( e == el ) ++counter;
		return counter;
	}
	
	public void removeDuplicates() {
		TreeSet<Integer> to_remove = new TreeSet<Integer>();
		for ( Iterator<Integer> it = list.descendingIterator(); it.hasNext() ; ) {
			int k = it.next();
			if ( to_remove.contains(k) ) it.remove();
			else if ( count(k) >= 2 ) to_remove.add(k);
		}
	}
	
	public int sumFirst(int k) {
		int result = 0;
		for ( Iterator<Integer> it = list.iterator(); it.hasNext()&&k > 0 ; --k ) 
			result += it.next();
		return result;
	}
	
	public int sumLast(int k) {
		int result = 0;
		for ( Iterator<Integer> it = list.descendingIterator(); it.hasNext()&&k > 0 ; --k ) 
			result += it.next();
		return result;
	}
	
	public IntegerList addValue ( int value ) {
		IntegerList result = new IntegerList();int k = 0;
		for ( Iterator<Integer> it = list.iterator();it.hasNext(); ++k ) 
			result.add(it.next()+value,k);
		return result;
	}
	
	public void shiftRight( int idx , int k ) {
		shift(idx,k);
	}
	
	public void shiftLeft( int idx , int k ) {
		shift(idx,-k);
	}
	
	private void shift( int idx , int k ) {
		int new_pos = ((idx+k)%list.size()+list.size())%list.size();
		add(remove(idx),new_pos);
	}

	public int size() {
		return list.size();
	}
	
}

/*
  Test cases:
  
  0 0
  0 2 3
  0 56 12
  0 2 1
  0 48 1
  0 3 5
  0 8 9
  0 1 2
  2
  0 5 6
  0 7 8
  0 8 5
  0 0 0
  0 0 0
  0 0 0
  2
  1 2
  1 0
  2
  1 2
  0 0 1
  2
  1 0
  2
  3
  
  
  
  0 1
  10
  1 2 3 4 5 6 7 8 9 10
  
  
  
  1
  10
  1 2 3 4 5 6 7 8 9 10 
  0 1
  0 2
  0 5
  0 10
  1
  2 -1
  4
  5
  
  
  1
  10
  2 3 2 4 2 5 6 3 5 1
  4
  0 2
  0 3
  0 4
  0 5
  0 6
  0 3
  0 1
  1
  4
  3 1 1
  3 2 3
  3 2 4
  3 2 5
  3 1 1
  3 2 3
  3 2 4
  3 2 5
  4
  1
  4
  1
  2 10
  4
  5
  
  
  
  
   2
   10
   1 2 3 4 5 6 7 8 9 10
   2 0
   2 1
   2 5
   2 10
   2 15
   3 1
   3 5
   3 10
   2 0
   2 1
   2 5
   2 10
   2 15
   3 1
   3 5
   3 10
   1 5 2
   4
   1 5 7
   4
   1 5 8156
   4
   0 4 2
   4
   0 2 1
   4
   0 2 8
   4
   0 2 456
   4
   2 0
   2 1
   2 5
   2 10
   2 15
   3 1
   3 5
   3 10
   2 0
   2 1
   2 5
   2 10
   2 15
   3 1
   3 5
   3 10 
   5
   
   
  
  
  
  
  
  
 */


public class IntegerListTest {
	
	public static void main(String[] args) {
		Scanner jin = new Scanner(System.in);
		int k = jin.nextInt();
		if ( k == 0 ) { //test standard methods
			int subtest = jin.nextInt();
			if ( subtest == 0 ) {
				IntegerList list = new IntegerList();
				while ( true ) {
					int num = jin.nextInt();
					if ( num == 0 ) {
						list.add(jin.nextInt(), jin.nextInt());
					}
					if ( num == 1 ) {
						list.remove(jin.nextInt());
					}
					if ( num == 2 ) {
						print(list);
					}
					if ( num == 3 ) {
						break;
					}
				}
			}
			if ( subtest == 1 ) {
				int n = jin.nextInt();
				Integer a[] = new Integer[n];
				for ( int i = 0 ; i < n ; ++i ) {
					a[i] = jin.nextInt();
				}
				IntegerList list = new IntegerList(a);
				print(list);
			}
		}
		if ( k == 1 ) { //test count,remove duplicates, addValue
			int n = jin.nextInt();
			Integer a[] = new Integer[n];
			for ( int i = 0 ; i < n ; ++i ) {
				a[i] = jin.nextInt();
			}
			IntegerList list = new IntegerList(a);
			while ( true ) {
				int num = jin.nextInt();
				if ( num == 0 ) { //count
					System.out.println(list.count(jin.nextInt()));
				}
				if ( num == 1 ) {
					list.removeDuplicates();
				}
				if ( num == 2 ) {
					print(list.addValue(jin.nextInt()));
				}
				if ( num == 3 ) {
					list.add(jin.nextInt(), jin.nextInt());
				}
				if ( num == 4 ) {
					print(list);
				}
				if ( num == 5 ) {
					break;
				}
			}
		}
		if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
			int n = jin.nextInt();
			Integer a[] = new Integer[n];
			for ( int i = 0 ; i < n ; ++i ) {
				a[i] = jin.nextInt();
			}
			IntegerList list = new IntegerList(a);
			while ( true ) {
				int num = jin.nextInt();
				if ( num == 0 ) { //count
					list.shiftLeft(jin.nextInt(), jin.nextInt());
				}
				if ( num == 1 ) {
					list.shiftRight(jin.nextInt(), jin.nextInt());
				}
				if ( num == 2 ) {
					System.out.println(list.sumFirst(jin.nextInt()));
				}
				if ( num == 3 ) {
					System.out.println(list.sumLast(jin.nextInt()));
				}
				if ( num == 4 ) {
					print(list);
				}
				if ( num == 5 ) {
					break;
				}
			}
		}
	}
	
	public static void print(IntegerList il) {
		if ( il.size() == 0 ) System.out.print("EMPTY");
		for ( int i = 0 ; i < il.size() ; ++i ) {
			if ( i > 0 ) System.out.print(" ");
			System.out.print(il.get(i));
		}
		System.out.println();
	}

}
