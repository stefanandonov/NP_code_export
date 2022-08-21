import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;

public class ArchiveStoreTest {
	public static void main(String[] args) {
		ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
		Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
		int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
		int i;
		for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
			long days = scanner.nextLong();
			Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
					* 60 * 1000));
			LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
			store.archiveItem(lockedArchive, date);
		}
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
		for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
			int maxOpen = scanner.nextInt();
			SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
		}
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
			int open = scanner.nextInt();
            try {
            	store.openItem(open, date);
            } catch(NonExistingItemException e) {
            	System.out.println(e.getMessage());
            }
        }
		System.out.println(store.getLog());
	}
}

class ArchiveStore {
	private ArrayList<Archive> items;
	private StringBuilder log;

	public ArchiveStore() {
		items = new ArrayList<Archive>();
		log = new StringBuilder();
	}

	public void archiveItem(Archive item, Date date) {
		item.archive(date);
		items.add(item);
		log.append(String.format("Item %d archived at %s\n", item.getId(), date));
	}

	public Archive openItem(int id, Date date) throws NonExistingItemException {
		for (Archive item : items) {
			if (item.getId() == id) {
				try {
					item.open(date);
				} catch (InvalidArchiveOpenException e) {
					log.append(e.getMessage());
					log.append("\n");
                    return item;
				}
				log.append(String.format("Item %d opened at %s\n",
						item.getId(), date));
				return item;
			}
		}
        throw new NonExistingItemException(id);
	}

	public String getLog() {
		return log.toString();
	}
}

class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
    	super(String.format("Item with id %d doesn't exist", id));
    }
}

abstract class Archive {
	protected int id;
	protected Date dateArchived;

	public void archive(Date date) {
		this.dateArchived = date;
	}

	public abstract Date open(Date date) throws InvalidArchiveOpenException;

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		Archive archive = (Archive) obj;
		return this.id == archive.id;
	}
}

class LockedArchive extends Archive {
	private Date dateToOpen;

	public LockedArchive(int id, Date dateToOpen) {
		this.id = id;
		this.dateToOpen = dateToOpen;
	}

	@Override
	public Date open(Date date) throws InvalidArchiveOpenException {
		if (date.before(dateToOpen))
			throw new InvalidArchiveOpenException(String.format(
					"Item %d cannot be opened before %s", id, dateToOpen));
		return date;

	}
}

class SpecialArchive extends Archive {
	private int countOpened;
	private int maxOpen;

	public SpecialArchive(int id, int maxOpen) {
		this.id = id;
		countOpened = 0;
		this.maxOpen = maxOpen;
	}

	@Override
	public Date open(Date date) throws InvalidArchiveOpenException {
		if (countOpened >= maxOpen)
			throw new InvalidArchiveOpenException(String.format(
					"Item %d cannot be opened more than %d times", id, maxOpen));
		++countOpened;
		return date;
	}

}

class InvalidArchiveOpenException extends Exception {
	public InvalidArchiveOpenException(String message) {
		super(message);
	}
}
