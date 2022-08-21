//package mk.ukim.finki.konsultacii;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
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

    public void archiveItem(Archive item, LocalDate date) {
        item.archive(date);
        items.add(item);
        log.append(String.format("Item %d archived at %s\n",
                item.getId(), date.toString()));
    }

    public Archive openItem(int id, LocalDate date) throws NonExistingItemException {
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
    protected LocalDate dateArchived;

    public void archive(LocalDate date) {
        this.dateArchived = date;
    }

    public abstract LocalDate open(LocalDate date) throws InvalidArchiveOpenException;

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
    private LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        this.id = id;
        this.dateToOpen = dateToOpen;
    }

    @Override
    public LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
        if (date.isBefore(dateToOpen))
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
    public LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
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
