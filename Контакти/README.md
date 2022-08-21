Се со цел да се подобри комуникацијата на факултетот потребно е да се направи систем за чување на контакти за секој студент.

Да се креира класа `Contact`. За потребите на оваа класа да се дефинираат следниве методи:

- `Contact(String date)` - конструктор каде што `date` е датумот кога е креиран контактот даден во следниов формат `YYYY-MM-DD`
- `isNewerThan(Contact c):boolean` - метод кој враќа `true` доколку контактот е креиран подоцна од контактот `c` и обратно
- `getType():String` - метод кој враќа вредност "Email" или "Phone" во зависност од типот на контактот

Од класата `Contact` не треба да може директно да се инстанцира објект.

Од оваа класа се изведуваат класите `EmailContact` и `PhoneContact`.

За класата `EmailContact` дополнително се чува e-маил кој што е од типот `String`. Да се дефинираат следниве методи:

- `EmailContact(String date, String email)` - конструктор
- `getEmail():String` - метод кој што го враќа е-маилот
- `getType():String`- имплементација на методот од класата Contact

За класата `PhoneContact` дополнително се чува телефонски број кој што е од типот `String` и оператор кој што е енумерација и се дефинира на следниов начин `enum Operator { VIP, ONE, TMOBILE }`. За оваа класа да се дефинираат следниве методи:

- `PhoneContact(String date, String phone)` - конструктор
- `getPhone():String` - метод кој што го враќа телефонскиот број
- `getOperator():Operator` - метод кој што го враќа операторот (070, 071, 072 – TMOBILE, 075,076 – ONE, 077, 078 – VIP)
- `getType():String`- имплементација на методот од класата Contact

*Забелешка: Сите телефонски броеви се во формат `07X/YYY-ZZZ` каде што `X` има вредност `{0,1,2,5,6,7,8}`

Потоа да се дефинира класата `Student` каде што се чува низа на контакти за секој студент

- `Student(String firstName, String lastName, String city, int age, long index)` – конструктор
-  `addEmailContact(String date, String email):void` – метод што додава е-маил контакт во низата на контакти
-  `addPhoneContact(String date, String phone):void` – метод што додава телефонски контакт во низата на контакти
- `getEmailContacts():Contact[]` – враќа низа на `email` контактите на студентот
- `getPhoneContacts():Contact[]` – враќа низа на `phone` контактите на студентот
- `getCity():String` - метод кој го враќа градот
- `getFullName():String` - метод кој го враќа целосното име на студентот во формат `IME PREZIME`
- `getIndex():long` - метод кој го враќа индексот на студентот
- `getLatestContact():Contact` – го враќа најновиот контакт (според датум) од студентот
- `toString()` – претставува `JSON` репрезентација на класата студент пр. `{"ime":"Jovan", "prezime":"Jovanov", "vozrast":20, "grad":"Skopje", "indeks":101010, ` `"telefonskiKontakti":["077/777-777", "078/888-888"], "emailKontakti":["jovan.jovanov@example.com", "jovanov@jovan.com", "jovan@jovanov.com"]}`

*Забелешка: Во класата `Student` да се чува само една низа од контакти `Contact[]`, а не две низи одделно (`PhoneContact[]` и `EmailContact[]`)

*Напомена да не се користи `instanceOf` или `getClass` при имплементација на овие методи

Дополнително да се дефинира класа `Faculty`. За оваа класа да се дефинираат следниве методи:

- `Faculty(String name, Student [] students)` – конструктор
- `countStudentsFromCity(String cityName):int` – враќа колку студенти има од даден град
- `getStudent(long index):Student` – метод кој го враќа студентот кој го има дадениот индекс
- `getAverageNumberOfContacts():double` – враќа просечен број на контакти по студент
- `getStudentWithMostContacts():Student` – метод кој го враќа студентот со најмногу контакти (доколку има повеќе студенти со ист број на контакти да го врати студентот со најголем индекс)
- `toString()` – претставува `JSON` репрезентација на класата `Faculty` пример: `{"fakultet":"FINKI", "studenti":[STUDENT1, STUDENT2, ...]}`  каде што треба да има целосни информации за секој студент.