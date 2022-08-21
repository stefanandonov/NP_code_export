//package mk.ukim.finki.primeri;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.Entry.comparingByValue;

interface ILocation {
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

class LocationUtils {
    public static double distanceBetween(ILocation location1, ILocation location2) {
        return Math.sqrt(Math.pow(location1.getLatitude() - location2.getLatitude(), 2)
                + Math.pow(location1.getLongitude() - location2.getLongitude(), 2));
    }

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static boolean isDanger(ILocation location1, ILocation location2) {
        return distanceBetween(location1, location2) <= 2.0&&timeBetweenInSeconds(location1, location2) <= 300;
    }

    public static int dangerContactsBetween(User u1, User u2) {
        int counter = 0;
        for (ILocation iLocation : u1.locations) {
            for (ILocation iLocation1 : u2.locations) {
                if (isDanger(iLocation, iLocation1))
                    ++counter;
            }
        }

        return counter;
    }
}

class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String id) {
        super(String.format("User with id %s already exists", id));
    }
}

class User {
    String name;
    String id;
    List<ILocation> locations;
    LocalDateTime timeInfected;
    boolean infected;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        locations = new ArrayList<>();
        infected = false;
    }

    public void addLocations(List<ILocation> iLocations) {
        locations.addAll(iLocations);
    }

    public void setTimeInfected(LocalDateTime timeInfected) {
        this.timeInfected = timeInfected;
    }

    public LocalDateTime getTimeInfected() {
        return timeInfected != null ? timeInfected : LocalDateTime.MAX;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }


    String userComplete() {
        return String.format("%s %s %s", name, id, timeInfected);
    }

    String userHidden() {
        return String.format("%s %s***", name, id.substring(0, 4));
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public boolean isInfected() {
        return infected;
    }
}


class StopCoronaApp {
    Map<String, User> usersByIdMap;
    Map<User, Map<User, Integer>> countingMapForNearContacts;


    public StopCoronaApp() {
        usersByIdMap = new HashMap<>();
        countingMapForNearContacts = new TreeMap<>(Comparator.comparing(User::getTimeInfected).thenComparing(us -> us.id));
    }

    public void addUser(String name, String id) throws UserAlreadyExistException {
        if (usersByIdMap.containsKey(id))
            throw new UserAlreadyExistException(id);
        usersByIdMap.put(id, new User(name, id));
    }

    public void addLocations(String id, List<ILocation> locations) {
        usersByIdMap.get(id).addLocations(locations);
    }

    public void detectNewCase(String id, LocalDateTime timestamp) {
        User infectedUser = usersByIdMap.get(id);
        infectedUser.setTimeInfected(timestamp);
        infectedUser.setInfected(true);

    }

    public void createReport() {

        for (User u : usersByIdMap.values()) {
            for (User u1 : usersByIdMap.values()) {
                if (!u.equals(u1)) {
                    countingMapForNearContacts.putIfAbsent(u, new TreeMap<>(Comparator.comparing(User::getTimeInfected).thenComparing(us -> us.id)));
                    countingMapForNearContacts.computeIfPresent(u, (k, v) -> {
                        v.putIfAbsent(u1, 0);
                        v.computeIfPresent(u1, (k1, v1) -> {
                            v1 += LocationUtils.dangerContactsBetween(u, u1);
                            return v1;
                        });
                        return v;
                    });
                }
            }
        }

        List<Integer> directContactsCounts = new ArrayList<>();
        List<Integer> indirectContactsCounts = new ArrayList<>();

        for (User u1 : countingMapForNearContacts.keySet()) {
            if (u1.isInfected()) {
                System.out.println(u1.userComplete());
                //direktni kontakti
                System.out.println("Direct contacts:");
                Map<User, Integer> directContact = getDirectContacts(u1);
                directContact.entrySet().stream()
                        .sorted(comparingByValue(Comparator.reverseOrder()))
                        .forEach(entry -> System.out.println(String.format("%s %s", entry.getKey().userHidden(), entry.getValue())));
                int count = directContact.values().stream().mapToInt(i -> i).sum();
                System.out.println(String.format("Count of direct contacts: %d", count));
                directContactsCounts.add(count);


                Collection<User> indirectContacts = getIndirectContacts(u1);
                System.out.println("Indirect contacts: ");
                indirectContacts.forEach(user -> System.out.println(user.userHidden()));
                System.out.println(String.format("Count of indirect contacts: %d", indirectContacts.size()));
                indirectContactsCounts.add(indirectContacts.size());
            }
        }

        System.out.printf("Average direct contacts: %.4f\n", directContactsCounts.stream().mapToInt(i -> i).average().getAsDouble());
        System.out.printf("Average indirect contacts: %.4f", indirectContactsCounts.stream().mapToInt(i -> i).average().getAsDouble());
    }

    private Map<User, Integer> getDirectContacts(User u) {
        return countingMapForNearContacts.get(u)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Collection<User> getIndirectContacts(User u) {
        Set<User> indirectContacts = new TreeSet<>(Comparator.comparing(User::getName).thenComparing(User::getId));
        Map<User, Integer> directContact = getDirectContacts(u);
        directContact.keySet().stream()
                .flatMap(user -> getDirectContacts(user).keySet().stream())
                .filter(user -> !indirectContacts.contains(user) && !directContact.containsKey(user) && !user.equals(u))
                .forEach(indirectContacts::add);
        return indirectContacts;
    }


}

public class StopCoronaTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
