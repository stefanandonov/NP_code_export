import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ChatSystemTest {
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
		Scanner jin = new Scanner(System.in);
		int k = jin.nextInt();
		if ( k == 0 ) {
			ChatRoom cr = new ChatRoom(jin.next());
			int n = jin.nextInt();
			for ( int i = 0 ; i < n ; ++i ) {
				k = jin.nextInt();
				if ( k == 0 ) cr.addUser(jin.next());
				if ( k == 1 ) cr.removeUser(jin.next()); 
				if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));  
			}
			System.out.println("");
			System.out.println(cr.toString());
			n = jin.nextInt();
			if ( n == 0 ) return;
			ChatRoom cr2 = new ChatRoom(jin.next());
			for ( int i = 0 ; i < n ; ++i ) {
				k = jin.nextInt();
				if ( k == 0 ) cr2.addUser(jin.next());
				if ( k == 1 ) cr2.removeUser(jin.next()); 
				if ( k == 2 ) cr2.hasUser(jin.next());  
			}
            System.out.println(cr2.toString());
		}	
       if ( k == 1 ) {
			ChatSystem cs = new ChatSystem();
			Method mts[] = cs.getClass().getMethods();
			while ( true ) {
				String cmd = jin.next();
				if ( cmd.equals("stop") ) break;
				if ( cmd.equals("print") ) {
					System.out.println(cs.getRoom(jin.next())+"\n");continue;
				}
				for ( Method m : mts ) {
					if ( m.getName().equals(cmd) ) {
						String params[] = new String[m.getParameterTypes().length];
						for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
						m.invoke(cs,params);
					}
				}				
			}
		}
	}

}
