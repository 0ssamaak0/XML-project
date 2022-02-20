import java.io.BufferedReader;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("../test.xml"));
        Phase1 p1 = new Phase1(br);
        br.close();


        System.out.println(p1.formatted);
        // System.out.println(p1.parser.xmlTree.root.tag);
        // System.out.println(p1.xmlTree.root.tag);
        // System.out.println(p1.formatted);
        // System.out.println(p1.JSONified);
        // p1.xmlTree.preorder_traversal(p1.xmlTree.root);
        // System.out.println(p1.formatted);
        // p1.xml_validator();
        // System.out.println(p1.xml_text);
        // System.out.println(p1.formatted);
        // System.out.println(p1.JSONified);

        // System.out.println(p1.formatting());
        // p1.Jsonifier();

    }

}
