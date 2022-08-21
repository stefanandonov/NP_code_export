import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

class InvalidFormatException extends Exception {
	
}

class InvalidNameException extends Exception {
	
	String name;
	Reason reason;
	public static enum Reason {
		NOT_UNIQUE,INVALID_CHARACTER,INVALID_SIZE;
	}

	public InvalidNameException(String name,Reason reason) {
		this.name = name;
		this.reason = reason;
	}
	
	public String getName() {
		return name;
	}
	
	public Reason getReason() {
		return reason;
	}
	
}

class MaximumSizeExceddedException extends Exception {
	
	int max_size;

	public MaximumSizeExceddedException(int max_size) {
		this.max_size = max_size;
	}
	
	public int getMaxSize() {
		return max_size;
	}
	
}

class InvalidNumberException extends Exception {
	
	String number;

	public InvalidNumberException(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}
	
}

class Contact implements Comparable<Contact> {
	
	final static int MAX_SIZE = 5; 
	String numbers[];
	int number_size;
	String name;
	
	public Contact(String name,String... numbers) throws InvalidNameException,InvalidNumberException,MaximumSizeExceddedException {
		if ( name.length() <= 4 || name.length() >= 10 ) throw new InvalidNameException(name,InvalidNameException.Reason.INVALID_SIZE);
		for ( int i = 0 ; i < name.length() ; ++i ) if ( ! Character.isLetterOrDigit((name.charAt(i))) ) throw new InvalidNameException(name,InvalidNameException.Reason.INVALID_CHARACTER);
		this.name = name;
		this.numbers = new String[MAX_SIZE];
		number_size = 0;
		for ( String number : numbers ) addNumber(number);
	}
	
	public void addNumber(String number) throws InvalidNumberException,MaximumSizeExceddedException {
        if ( ! isValidNumber(number) ) {
            throw new InvalidNumberException(number);
        }
		if ( number_size >= MAX_SIZE ) throw new MaximumSizeExceddedException(MAX_SIZE);
		numbers[number_size++] = number;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getNumbers() {
		String[] res = Arrays.copyOf(numbers, number_size);
		Arrays.sort(res);
		return res;
	}
	
	private static boolean isValidNumber(String number) {
		if ( number == null || number.length() != 9 ) return false;
		if ( ! ( number.startsWith("070") || number.startsWith("071") || number.startsWith("072") ||
				number.startsWith("075") || number.startsWith("076") ||
				number.startsWith("077") || number.startsWith("078") ) )
			return false;
		for ( int i = 3 ; i < number.length() ; ++i ) if ( ! Character.isDigit(number.charAt(i)) ) return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append('\n');
		sb.append(number_size).append('\n');
		for ( String number : getNumbers() ) 
			if ( number != null ) sb.append(number).append('\n');
		return sb.toString();
	}
	
	public static Contact valueOf(String s) throws InvalidFormatException {
		String lines[] = s.split("\n");
        try {
			return new Contact(lines[0],Arrays.copyOfRange(lines, 2, lines.length));
		}
		catch (InvalidNameException|InvalidNumberException|MaximumSizeExceddedException e) { 
			throw new InvalidFormatException();
		}
	}

	@Override
	public int compareTo(Contact o) {
		return name.compareTo(o.name);
	}

	public boolean hasPhoneNumberThatStartsWith(String number_start) {
		for ( int i = 0 ; i < number_size ; ++i )
			if ( numbers[i].startsWith(number_start) ) return true;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + number_size;
		result = prime * result + Arrays.hashCode(numbers);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number_size != other.number_size)
			return false;
		if (!Arrays.equals(numbers, other.numbers))
			return false;
		return true;
	}
	
	
	
}

class PhoneBook {
	
	final static int MAX_SIZE = 250;
	Contact[] contacts;
	int num_contacts;
	
	public PhoneBook() {
		contacts = new Contact[MAX_SIZE];
		num_contacts = 0;
	}
	
	public void addContact(Contact contact) throws InvalidNameException,MaximumSizeExceddedException {
		if ( getContactForName(contact.getName()) != null ) throw new InvalidNameException(contact.name, InvalidNameException.Reason.NOT_UNIQUE);
		if ( num_contacts >= MAX_SIZE ) throw new MaximumSizeExceddedException(MAX_SIZE);
		contacts[num_contacts++] = contact;
	}
	
	public Contact getContactForName(String name) {
		int idx = indexOfContact(name);
		return idx == -1?null:contacts[idx];
	}
	
	public Contact[] getContactsForNumber(String number_start) {
		Contact res[] = new Contact[numberOfContacts()];
		int k = 0;
		for ( int i = 0 ; i < num_contacts ; ++i ) {
			if ( contacts[i].hasPhoneNumberThatStartsWith(number_start) ) res[k++] = contacts[i];
		}
		res = Arrays.copyOf(res, k);
		return res;
	}
	
	private int indexOfContact(String name) {
		for ( int i = 0 ; i < num_contacts ; ++i ) 
			if ( contacts[i] != null ) 
				if ( contacts[i].getName().equals(name)) return i;
		return -1;
	}
	
	public boolean removeContact(String name) {
		int idx = indexOfContact(name);
		if ( idx != -1 ) {
			contacts[idx] = contacts[num_contacts-1];
			contacts[num_contacts--] = null;
		}
		return idx != -1;
	}
	
	public int numberOfContacts() {
		return num_contacts;
	}
	
	public Contact[] getContacts() {
		Contact[] res = Arrays.copyOf(contacts, num_contacts);
		Arrays.sort(res);
		return res;
	}
	
	public static boolean saveAsTextFile(PhoneBook phonebook, String path)  {
		try (PrintWriter out =
                new PrintWriter(new File(path))) {
			out.println(phonebook.toString());
		}
		catch ( IOException e ) {
			return false;
		}
		return true;
	}
	
	public static PhoneBook loadFromTextFile(String path) throws IOException,InvalidFormatException {
		try (Scanner jin  =
                new Scanner(new File(path))) {
			PhoneBook res = new PhoneBook();
			StringBuilder sb = new StringBuilder();
			while ( jin.hasNextLine() ) {
				String line = jin.nextLine();
				if ( line.length() == 0 ) {
                    if ( sb.toString().length() > 1 ) 
						res.addContact(Contact.valueOf(sb.toString()));
					sb = new StringBuilder();
				}
				else {
					sb.append(line).append('\n');
				}
				
			}
			return res;
		}
		catch (InvalidNameException|MaximumSizeExceddedException e) {
			throw new InvalidFormatException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        Contact[] to_print = getContacts();
		for ( int i = 0 ; i < num_contacts ; ++i ) sb.append(to_print[i]).append('\n');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(contacts);
		result = prime * result + num_contacts;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhoneBook other = (PhoneBook) obj;
		if (num_contacts != other.num_contacts)
			return false;
		return true;
	}	
	
}

public class PhonebookTester {

	public static void main(String[] args) throws Exception {
		Scanner jin = new Scanner(System.in);
		String line = jin.nextLine();
		switch( line ) {
			case "test_contact":
				testContact(jin);
				break;
			case "test_phonebook_exceptions":
				testPhonebookExceptions(jin);
				break;
			case "test_usage":
				testUsage(jin);
				break;
			case "test_file":
				testFile(jin);
				break;
		}
	}

	private static void testFile(Scanner jin) throws Exception {
		PhoneBook phonebook = new PhoneBook();
		while ( jin.hasNextLine() ) 
			phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
		String text_file = "phonebook.txt";
		PhoneBook.saveAsTextFile(phonebook,text_file);
		PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
	}

	private static void testUsage(Scanner jin) throws Exception {
		PhoneBook phonebook = new PhoneBook();
		while ( jin.hasNextLine() ) {
			String command = jin.nextLine();
			switch ( command ) {
				case "add":
					phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
					break;
				case "remove":
					phonebook.removeContact(jin.nextLine());
					break;
				case "print":
					System.out.println(phonebook.numberOfContacts());
					System.out.println(Arrays.toString(phonebook.getContacts()));
					System.out.println(phonebook.toString());
					break;
				case "get_name":
					System.out.println(phonebook.getContactForName(jin.nextLine()));
					break;
				case "get_number":
					System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
					break;
			}			
		}
	}

	private static void testPhonebookExceptions(Scanner jin) {
		PhoneBook phonebook = new PhoneBook();
		boolean exception_thrown = false;
		try {
			while ( jin.hasNextLine() ) {
				phonebook.addContact(new Contact(jin.nextLine()));
			}
		}
		catch ( InvalidNameException e ) {
			System.out.println(e.name);
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
	}

	private static void testContact(Scanner jin) throws Exception {		
		boolean exception_thrown = true;
		String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
		for ( String name : names_to_test ) {
			try {
				new Contact(name);
				exception_thrown = false;
			} catch (InvalidNameException e) {
				exception_thrown = true;
			} 
			if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
		}
		String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
		for ( String number : numbers_to_test ) {
			try {
				new Contact("Andrej",number);
				exception_thrown = false;
			} catch (InvalidNumberException e) {
				exception_thrown = true;
			} 
			if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
		}
		String nums[] = new String[10];
		for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
		try {
			new Contact("Andrej",nums);
			exception_thrown = false;
		} catch (MaximumSizeExceddedException e) {
			exception_thrown = true;
		} 
		if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
		Random rnd = new Random(5);
		Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
		System.out.println(contact.getName());
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
		contact.addNumber(getRandomLegitNumber(rnd));
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
		contact.addNumber(getRandomLegitNumber(rnd));
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
	}

	static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
	static Random rnd = new Random();
	
	private static String getRandomLegitNumber() {
		return getRandomLegitNumber(rnd);
	}
	
	private static String getRandomLegitNumber(Random rnd) {
		StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
		for ( int i = 3 ; i < 9 ; ++i ) 
			sb.append(rnd.nextInt(10));
		return sb.toString();
	}
	

}
