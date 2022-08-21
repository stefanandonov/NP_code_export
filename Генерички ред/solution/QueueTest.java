import java.util.Scanner;
import java.util.LinkedList;

@SuppressWarnings("serial")
class EmptyQueueException extends Exception {

	public EmptyQueueException() {
		this("The queue is empty.");
	}
	
	public EmptyQueueException(String message) {
		super(message);
	}
	
}


class Queue <T> {
	
	private Node<T> head,tail;
	
	private int count = 0;
	
	public Queue() {
		head = null;
		tail = null;
		count = 0;
	}
	
	public boolean isEmpty() {
		return count==0;
	}
	
	public void enqueue( T element ) {
		Node<T> n = new Node<T>(element,null);
		if ( isEmpty() ) {
			tail = n;
			head = n;
		}
		else {
			tail.setNext(n);
			tail = n;
		}
		++count;
	}
	
	public T dequeue() throws EmptyQueueException {
		if ( isEmpty() ) throw new EmptyQueueException();
		T res = head.getElement();
		head = head.getNext();
		--count;
		return res;
	}
	
	public T peek() throws EmptyQueueException {
		if ( isEmpty() ) throw new EmptyQueueException();
		return head.getElement();
	}
	
	public T inspect() throws EmptyQueueException {
		if ( isEmpty() ) throw new EmptyQueueException();
		return tail.getElement();
	}
	
	public int count() {
		return count;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node<T> walker = head;
		while ( walker != null ) {
			sb.append(walker.getElement()+" -> ");
			walker = walker.getNext();
		}
		sb.append("null");
		return sb.toString();
	}

}


class Node <T> {
private T element;
	
	private Node<T> next;

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T getElement() {
		return element;
	}

	public Node(T element, Node<T> next) {
		super();
		this.element = element;
		this.next = next;
	}
	
}


public class QueueTest {

	
    public static void main(String[] args) throws EmptyQueueException {
		Scanner jin = new Scanner(System.in);
		int k = jin.nextInt();
		if ( k == 0 ) { //Simple test case with one int element
			int t = jin.nextInt();
			Queue<Integer> queue = new Queue<Integer>();
			System.out.println("Queue empty? - "+queue.isEmpty());
			System.out.println("Queue count? - "+queue.count());
			System.out.println("Queue enqueue "+t);
			queue.enqueue(t);
			System.out.println("Queue empty? - "+queue.isEmpty());
			System.out.println("Queue count? - "+queue.count());
			System.out.println("Queue dequeue? - "+queue.dequeue());
			System.out.println("Queue empty? - "+queue.isEmpty());
			System.out.println("Queue count? - "+queue.count());
		}
		if ( k == 1 ) { //a more complex test with strings
			Queue<String> queue = new Queue<String>();
			int counter = 0;
			while ( jin.hasNextInt() ) {
				String t = jin.next();
				queue.enqueue(t);
				++counter;
			}
			for ( int i = 0 ; i < counter ; ++i ) {
				System.out.println(queue.dequeue());
			}
			queue.enqueue(jin.next());
			System.out.println("Queue inspect? - "+queue.inspect());
			System.out.println("Queue peek? - "+queue.peek());
			queue.enqueue(queue.dequeue());
			queue.enqueue(jin.next());
			System.out.println("Queue inspect? - "+queue.inspect());
			System.out.println("Queue peek? - "+queue.peek());
		}
		if ( k == 2 ) {
			Queue<String> queue = new Queue<String>();
			String next = "";
			int counter = 0;
			while ( true ) {
				next = jin.next();
				if ( next.equals("stop") ) break;
				queue.enqueue(next);
				++counter;
			}
			while ( !queue.isEmpty() ) {
				if ( queue.count()<counter) System.out.print(" ");
				System.out.print(queue.dequeue());
			}
		}
		if ( k == 3 ) { //random testing
			Queue<Double> queue = new Queue<Double>();
			LinkedList<Double> java_queue = new LinkedList<Double>();
			boolean flag = true;
            int n = jin.nextInt();
			for ( int i = 0 ; i < n ; ++i ) {
				double q = Math.random();
				if ( q < 0.5 ) {
					double t = Math.random();
					queue.enqueue(t);
					java_queue.addFirst(t);
				}
				if ( q < 0.8&&q >= 0.5 ) {
					if ( ! java_queue.isEmpty() ) {
						double t1 = java_queue.removeLast();
						double t2 = queue.dequeue();
						flag &= t1==t2;
					}
					else {
						flag &= java_queue.isEmpty()==queue.isEmpty();
					}
				}
				if ( q < 0.9&&q >= 0.8 ) {
					if ( ! java_queue.isEmpty() ) {
						double t1 = java_queue.peekLast();
						double t2 = queue.peek();
						flag &= t1==t2;
					}
					else {
						flag &= java_queue.isEmpty()==queue.isEmpty();
					}
				}
				if ( q < 1 && q >= 0.9 ) {
					if ( ! java_queue.isEmpty() ) {
						double t1 = java_queue.peekFirst();
						double t2 = queue.inspect();
						flag &= t1==t2;
					}
					else {
						flag &= java_queue.isEmpty()==queue.isEmpty();
					}
				}
				flag &= java_queue.size()==queue.count();
			}
			System.out.println("Compared to the control queue the results were the same? - "+flag);
		}
		if ( k == 4 ) { //performance testing
			Queue<Double> queue = new Queue<Double>();
			int n = jin.nextInt();
			for ( int i = 0 ; i < n ; ++i ) {
				if ( Math.random() < 0.5 ) {
					queue.enqueue(Math.random());
				}
				else {
					if ( ! queue.isEmpty() ) {
						queue.dequeue();
					}
				}
			}
			System.out.println("You implementation finished in less then 3 seconds, well done!");
		}
		if ( k == 5 ) { //Exceptions testing
			Queue<String> queue = new Queue<String>();
			try {
				queue.dequeue();
			}
			catch ( Exception e ) {
				System.out.println(e.getClass().getSimpleName());
			}
			try {
				queue.peek();
			}
			catch ( Exception e ) {
				System.out.println(e.getClass().getSimpleName());
			}
			try {
				queue.inspect();
			}
			catch ( Exception e ) {
				System.out.println(e.getClass().getSimpleName());
			}
		}
	}
    
}



