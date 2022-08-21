import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Collectors;

abstract class Contact implements Comparable<Contact> {
    protected String date;

    public Contact(String date) {
        this.date = date;
    }

    public boolean isNewerThan(Contact contact) {
        return this.date.compareTo(contact.date) > 0;
    }

    public abstract String getType();

    public abstract String quoted();

    @Override
    public int compareTo(Contact o) {
        return o.date.compareTo(this.date);
    }
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String quoted() {
        return String.format("\"%s\"", email);
    }
}

enum Operator {
    VIP, ONE, TMOBILE
}

class PhoneContact extends Contact {
    private String phone;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String quoted() {
        return String.format("\"%s\"", phone);
    }

    public Operator getOperator() {
        String prefix = phone.substring(0, 3);
        if ("070".equals(prefix) || "071".equals(prefix) || "072".equals(prefix)) {
            return Operator.TMOBILE;
        } else if ("075".equals(prefix) || "076".equals(prefix)) {
            return Operator.ONE;
        } else if ("077".equals(prefix) || "078".equals(prefix)) {
            return Operator.VIP;
        }
        return null;
    }

}

class Student {
    private Contact[] contacts;

    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[0];
    }

    public int getContactsSize() {
        return contacts.length;
    }

    public void addEmailContact(String date, String email) {
        addContact(new EmailContact(date, email));
    }

    public void addPhoneContact(String date, String phone) {
        addContact(new PhoneContact(date, phone));
    }

    public Contact[] getEmailContacts() {
        return getFiltered("Email");
    }

    public Contact[] getPhoneContacts() {
        return getFiltered("Phone");
    }

    Contact[] getFiltered(String type) {
        return Arrays.stream(contacts)
                .filter(each -> each.getType().equals(type))
                .toArray(Contact[]::new);
    }

    void addContact(Contact contact) {
        this.contacts = Arrays.copyOf(this.contacts, this.contacts.length + 1);
        this.contacts[this.contacts.length - 1] = contact;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        return Arrays.stream(contacts)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    static String keyValue(String key, String value) {
        return String.format("\"%s\":\"%s\"", key, value);
    }

    static String keyValueNoQuotes(String key, String value) {
        return String.format("\"%s\":%s", key, value);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        joiner.add(keyValue("ime", firstName));
        joiner.add(keyValue("prezime", lastName));
        joiner.add(keyValueNoQuotes("vozrast", String.valueOf(age)));
        joiner.add(keyValue("grad", city));
        joiner.add(keyValueNoQuotes("indeks", String.valueOf(index)));
        String contactsString = Arrays.stream(getPhoneContacts())
                .map(Contact::quoted)
                .collect(Collectors.joining(", ", "[", "]"));
        joiner.add(keyValueNoQuotes("telefonskiKontakti", contactsString));
        contactsString = Arrays.stream(getEmailContacts())
                .map(Contact::quoted)
                .collect(Collectors.joining(", ", "[", "]"));
        joiner.add(keyValueNoQuotes("emailKontakti", contactsString));
        return joiner.toString();
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students, students.length);
    }

    public int countStudentsFromCity(String cityName) {
        return ((int) Arrays.stream(students)
                .filter(each -> each.getCity().equals(cityName))
                .count());
    }

    public Student getStudent(long index) {
        return Arrays.stream(students)
                .filter(each -> each.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public double getAverageNumberOfContacts() {
        return Arrays.stream(students)
                .mapToInt(Student::getContactsSize)
                .average().orElse(0.0);

    }

    public Student getStudentWithMostContacts() {
        return Arrays.stream(students)
                .sorted(Comparator.comparing(Student::getContactsSize).reversed()
                        .thenComparing(Comparator.comparing(Student::getIndex).reversed()))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        joiner.add(Student.keyValue("fakultet", name));
        String studentsString = Arrays.stream(students)
                .map(Student::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        joiner.add(Student.keyValueNoQuotes("studenti", studentsString));
        return joiner.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0&&faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;
                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
