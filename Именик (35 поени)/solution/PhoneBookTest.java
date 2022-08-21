import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PhoneBookTest {

	public static void main(String[] args) {
		PhoneBook phoneBook = new PhoneBook();
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();
		for (int i = 0; i < n; ++i) {
			String line = scanner.nextLine();
			String[] parts = line.split(":");
			try {
				phoneBook.addContact(parts[0], parts[1]);
			} catch (DuplicateNumberException e) {
				System.out.println(e.getMessage());
			}
		}
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
            System.out.println(line);
			String[] parts = line.split(":");
			if (parts[0].equals("NUM")) {
				phoneBook.contactsByNumber(parts[1]);
			} else {
				phoneBook.contactsByName(parts[1]);
			}
		}
	}

}

class PhoneBook {
	TreeMap<String, Set<Contact>> contactsNumber;
	HashMap<String, Set<Contact>> contactsName;
	HashSet<String> numbers;

	public PhoneBook() {
		contactsNumber = new TreeMap<String, Set<Contact>>();
		contactsName = new HashMap<String, Set<Contact>>();
		numbers = new HashSet<String>();
	}

	public void addContact(String name, String number)
			throws DuplicateNumberException {
		if (numbers.contains(number)) {
			throw new DuplicateNumberException(number);
		}
		numbers.add(number);
		Contact contact = new Contact(name, number);
		List<String> keys = contact.getPhoneKeys();
		for (String key : keys) {
			Set<Contact> contacts = contactsNumber.get(key);
			if (contacts == null) {
				contacts = new TreeSet<Contact>();
				contactsNumber.put(key, contacts);
			}
			contacts.add(contact);
		}
		Set<Contact> nameContacts = contactsName.get(name);
		if (nameContacts == null) {
			nameContacts = new TreeSet<Contact>();
			contactsName.put(name, nameContacts);
		}
		nameContacts.add(contact);
	}

	public void contactsByNumber(String number) {
		Set<Contact> contacts = contactsNumber.get(number);
        if(contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
		for (Contact contact : contacts) {
			System.out.println(contact);
		}
	}

	public void contactsByName(String name) {
		Set<Contact> contacts = contactsName.get(name);
        if(contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
		for (Contact contact : contacts) {
			System.out.println(contact);
		}
	}
}

class Contact implements Comparable<Contact> {
	String name;
	String number;

	public Contact(String name, String number) {
		this.name = name;
		this.number = number;
	}

	@Override
	public String toString() {
		return String.format("%s %s", name, number);
	}

	public List<String> getPhoneKeys() {
		List<String> keys = new ArrayList<String>();
		int len = number.length();
		for (int i = 0; i <= len - 3; ++i) {
			for (int j = i + 3; j <= len; ++j) {
				String key = number.substring(i, j);
				keys.add(key);
			}
		}
		return keys;
	}

	@Override
	public int compareTo(Contact o) {
		if (name.equals(o.name)) {
			return number.compareTo(o.number);
		}
		return name.compareTo(o.name);
	}

}

class DuplicateNumberException extends Exception {
	public DuplicateNumberException(String number) {
		super(String.format("Duplicate number: %s", number));
	}
}
