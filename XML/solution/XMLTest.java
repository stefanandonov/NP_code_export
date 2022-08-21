import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class IndentUtil {

    public static String createIndent (int indent){
        return IntStream.range(0,indent)
                .mapToObj(i -> "\t")
                .collect(Collectors.joining());
    }

}
interface XMLComponent {

    String toString (int indent);

    void addAttribute (String name, String value);
}


abstract class XML implements XMLComponent {

    Map<String, String> attributes;
    String tag;

    public XML(String tag) {
        this.tag = tag;
        attributes = new LinkedHashMap<>();
    }

    public void addAttribute (String name, String value) {
        attributes.put(name, value);
    }
    private String createStringForAttributes(Map.Entry<String, String> entry) {
        return String.format("%s=\"%s\"", entry.getKey(), entry.getValue());
    }

    public String createStartTag (int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(IndentUtil.createIndent(indent));
        sb.append("<").append(tag);
        if (!attributes.isEmpty()) {
            sb.append(" ");
            sb.append(attributes.entrySet().stream()
                    .map(this::createStringForAttributes)
                    .collect(Collectors.joining(" ")));
        }
        sb.append(">");
        return sb.toString();
    }
}

class XMLLeaf extends XML {

    String element;

    public XMLLeaf(String tag, String element) {
        super(tag);
        this.element = element;
    }



    @Override
    public String toString(int indent) {
        return createStartTag(indent) +
                element +
                "</" + tag + ">\n";
    }
}

class XMLComposite extends XML {
    

    List<XMLComponent> componentList;

    public XMLComposite(String tag) {
        super(tag);
        componentList = new ArrayList<>();
    }

    public void addComponent (XMLComponent xmlComponent){
        componentList.add(xmlComponent);
    }

    @Override
    public String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(createStartTag(indent));
        sb.append("\n");
        componentList.forEach(c -> sb.append(c.toString(indent+1)));
        sb.append(IndentUtil.createIndent(indent)).append("</").append(tag).append(">\n");
        return sb.toString();
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            System.out.println(component.toString(0));
        } else if(testCase==2) {
            System.out.println(composite.toString(0));
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            System.out.println(main.toString(0));
        }
    }
}
