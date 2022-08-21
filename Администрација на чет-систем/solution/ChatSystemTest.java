import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 Test cases:
 
 
 0 ChatRoom1
 5
 0 user1
 0 user2
 0 user3
 0 user4
 0 user5
 0	
 
 
 0 ChatRoom1
 17
 0 user1
 0 user2
 0 user3
 0 user4
 0 user5
 2 user1
 2 user2
 2 user3
 2 user4
 2 user5
 1 user2
 1 user5
 2 user1
 2 user2
 2 user3
 2 user4
 2 user5
 5 ChatRoom2
 0 user1
 0 user2
 0 user3
 0 user4
 0 user5
 
 
 0 ChatRoom1
 23
 0 user1
 0 user2
 0 user3
 0 user4
 0 user5
 2 user1
 2 user2
 2 user3
 2 user4
 2 user5
 1 user2
 1 user5
 2 user1
 2 user2
 2 user3
 2 user4
 2 user5
 0 user2
 0 user2
 0 user2
 1 user2
 1 user55
 1 user0
 11 ChatRoom2
 0 user1
 0 user2
 0 user3
 0 user4
 0 user5
 1 user1
 1 user3
 0 user6
 0 user7
 0 user8
 1 user8
 
 
 1
 addRoom room1
 addRoom room2
 registerAndJoin user1 room1
 registerAndJoin user2 room1
 print room1
 print room2
 
 1
 addRoom room1
 addRoom room2
 addRoom room3
 registerAndJoin user1 room1
 registerAndJoin user2 room1
 print room1
 register user3
 register user4 room3
 joinRoom user2 room2
 joinRoom user2 room3
 followFriend user4 user2
 print room1
 print room2
 print room3
 
 
 */

class NoSuchUserException extends Exception {

	
	public NoSuchUserException() {
		super("default");
	}
	
	public NoSuchUserException(String user) {
		super(user);
	}

}


class NoSuchRoomException extends Exception {

	public NoSuchRoomException() {
		super("default");
	}
	
	public NoSuchRoomException(String user) {
		super(user);
	}
	
}


class ChatSystem {
	
	private TreeMap<String,ChatRoom> rooms;
	private TreeSet<String> users;
	
	public ChatSystem() {
		rooms = new TreeMap<String,ChatRoom>();
		users = new TreeSet<String>();
	}
	
	public void addRoom ( String room_name ) {
		rooms.put(room_name, new ChatRoom(room_name));
	}
	
	public void removeRoom ( String room_name ) {
		rooms.remove(room_name);
	}
	
	public ChatRoom getRoom(String room_name) throws NoSuchRoomException {
		if ( !rooms.containsKey(room_name)) throw new NoSuchRoomException(room_name);
		return rooms.get(room_name);
	}
	
	public String getUser(String user) throws NoSuchUserException {
		if ( !users.contains(user)) throw new NoSuchUserException(user);
		return user;
	}
	
	public void register(String user){
		users.add(user);
		LinkedList<ChatRoom> min_rooms = new LinkedList<ChatRoom>();
		int min = Integer.MAX_VALUE;
		for ( ChatRoom cr : rooms.values() ) {
			if ( cr.numUsers() < min ) {
				min_rooms = new LinkedList<ChatRoom>();
				min = cr.numUsers();
			}
			if ( cr.numUsers() == min ) min_rooms.add(cr);
		}
		if ( min_rooms.isEmpty() ) return;
		min_rooms.getFirst().addUser(user);
	}
	
	public void registerAndJoin(String user , String room) throws NoSuchUserException, NoSuchRoomException{
		users.add(user);
		joinRoom(user, room);
	}
	
	public void joinRoom(String user , String room ) throws NoSuchUserException, NoSuchRoomException {
		getRoom(room).addUser(getUser(user));
	}
	
	public void leaveRoom(String user , String room ) throws NoSuchUserException, NoSuchRoomException {
		getRoom(room).removeUser(getUser(user));
	}
	
	public void followFriend(String user , String user2 ) throws NoSuchUserException, NoSuchRoomException {
		for ( Map.Entry<String,ChatRoom> cr : rooms.entrySet() ) 
			if ( cr.getValue().hasUser(getUser(user2)) ) joinRoom(getUser(user), cr.getKey());
	}
	

}


class ChatRoom {
	
	private final String name;
	
	private TreeSet<String> usernames;
	
	public ChatRoom(String n) {
		name = n;
		usernames = new TreeSet<String>();
	}
	
	public void addUser(String username) {
		usernames.add(username);
	}
	
	public void removeUser(String username) {
		usernames.remove(username);
	}
	
	public boolean hasUser(String username) {
		return usernames.contains(username);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name+"\n");
		if ( usernames.size() == 0 ) 
			sb.append("EMPTY\n");
		for ( String user : usernames ) 
			sb.append(user+"\n");
		return sb.toString();
	}
	
	public ChatRoom intersect(ChatRoom a){
		ChatRoom res = new ChatRoom(name+"&"+a.name);
		res.usernames = new TreeSet<String>(usernames);
		res.usernames.retainAll(a.usernames);
		return res;
	}
	
	public ChatRoom union(ChatRoom a){
		ChatRoom res = new ChatRoom(name+"|"+a.name);
		res.usernames = new TreeSet<String>(usernames);
		res.usernames.addAll(a.usernames);
		return res;
	}
	
	public int numUsers() {
		return usernames.size();
	}

}



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
