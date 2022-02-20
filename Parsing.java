import java.io.BufferedReader;
import java.util.ArrayList;

public class Parsing {

    // Data members

    // the XML file itself as a BufferedReader
    private BufferedReader xml_file;

    // the XML file as a single string with no spaces
    public String xml_text = "";

    // ArrayList of the XML file line by line
    public ArrayList<String> lines = new ArrayList<String>();

    // xml Declaration (if any)
    public String xmlDeclaration = "";

    // Tree that contains the XML file, the tree is initialized with a trivial node
    // (to be escapped later)
    public Tree xmlTree = new Tree("HEAD");

    // Constructor
    public Parsing(BufferedReader xml_file) {
        this.xml_file = xml_file;
        fileReader();
        xml_parser();

        // Hint: only for valid XML files
        // tree_creator();
    }

    // Reading the xml_file line by line into the ArrayList (lines)
    public void fileReader() {
        try {
            String line;
            while ((line = xml_file.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error in file reading");
            return;
        }
    }

    // Saving the XML file as a single string with no spaces
    public void xml_parser() {

        // checking if we're reading a value
        boolean value = false;
        // checking if we're reading an tag (used once for attributes)
        boolean tag = false;
        // iterating through the xml file line by line

        for (int lineindx = 0; lineindx < lines.size(); lineindx++) {
            // iterating through each line
            for (int i = 0; i < lines.get(lineindx).length(); i++) {
                // checking if we're in a tag (opening or closing)
                if (lines.get(lineindx).charAt(i) == '<') {
                    value = false;
                    tag = true;
                }

                // check if the tag is closed.
                else if (lines.get(lineindx).charAt(i) == '>') {
                    tag = false;
                    try {
                        // checking if there's a value
                        if (lines.get(lineindx).charAt(i + 1) != ' ') {
                            value = true;
                        }
                    } catch (Exception e) {
                        System.out.print("");
                    }
                }

                if (tag) {
                    if (lines.get(lineindx).charAt(i) == ' ') {
                        xml_text += ' ';
                        // setting the tag to false to neglect extra unnecessary spaces before the
                        // attribute
                        tag = false;
                    }
                }
                // remove the sapces if we're reading a tag
                if (!value && lines.get(lineindx).charAt(i) != ' ') {
                    xml_text += lines.get(lineindx).charAt(i);
                }

                // add the spaces to the tag
                else if (value) {
                    xml_text += lines.get(lineindx).charAt(i);
                }
            }
        }
    }

   public void tree_creator() {
        // initializing empty tag and value
        String tag_buffer = "";
        String value_buffer = "";

        // initalizing nodes
        Node lastnode = xmlTree.root;
        Node current_node = xmlTree.root;

        // Initilazing attribute data
        String attributeName_buffer = "";
        String attributeValue_buffer = "";

        boolean attribute = false;

        // iterating through the xml_text (no spaces)
        for (int i = 0; i < xml_text.length(); i++) {

            // Checking the XML declaration
            if (xml_text.charAt(i) == '<' && xml_text.charAt(i + 1) == '?') {
                while (xml_text.charAt(i) != '>') {
                    xmlDeclaration += xml_text.charAt(i);
                    i++;
                }
                xmlDeclaration += '>';
            }

            // checking comments
            else if (xml_text.charAt(i) == '<' && xml_text.charAt(i + 1) == '!') {
                String comment_buffer = "";
                while (xml_text.charAt(i) != '>') {
                    comment_buffer += xml_text.charAt(i);
                    i++;
                }
                comment_buffer += '>';
                // adding the comment as a tag
                xmlTree.add_node(current_node, new Node(comment_buffer));
            }

            // checking closing tags
            else if (xml_text.charAt(i) == '<' && xml_text.charAt(i + 1) == '/') {
                // saving the lastest value into the current node's value
                current_node.value = value_buffer;

                // incrementing with 2 to escape "</"
                i += 2;

                // saving the current tag's name into the tag buffer
                while (xml_text.charAt(i) != '>') {
                    tag_buffer += xml_text.charAt(i);
                    i++;
                }
                // if the tag is equal to the current node's tag, return to the parent
                if (tag_buffer.compareTo(current_node.tag) == 0) {
                    current_node = current_node.parent;
                }

            }
            // checking opening tags
            else if (xml_text.charAt(i) == '<') {

                // making a new child and setting it into the current node
                lastnode = current_node;
                current_node = new Node();
                xmlTree.add_node(lastnode, current_node);

                // restting the buffers
                tag_buffer = "";
                attributeName_buffer = "";
                attributeValue_buffer = "";
                attribute = false;

                // escaping the "<"
                i++;

                while (xml_text.charAt(i) != '>' && xml_text.charAt(i) != ' ') {
                    // saving the tag's name
                    tag_buffer += xml_text.charAt(i);
                    i++;
                }
                // Checking if there's an attribute, this occurs after saving the tag into tag
                // buffer
                if (xml_text.charAt(i) == ' ') {
                    attribute = true;

                    // saving the attribute name

                    // escaping the space
                    i++;
                    while (xml_text.charAt(i) != '=' && attribute) {
                        attributeName_buffer += xml_text.charAt(i);
                        i++;
                    }
                    // escaping the "=""
                    i += 2;
                    // saving the attribute value
                    while (xml_text.charAt(i) != '\"') {
                        attributeValue_buffer += xml_text.charAt(i);
                        i++;
                    }
                    attribute = false;
                    // escaping the ">"
                    i++;
                }
                // saving the values into the current node
                current_node.tag = tag_buffer;
                current_node.attributeName = attributeName_buffer;
                current_node.attributeValue = attributeValue_buffer;
            }
            // Checking value
            else {
                value_buffer += xml_text.charAt(i);
            }
        }
        // since we've started with a trivial root, escape it to the real first node
        xmlTree.root = xmlTree.root.children.get(0);
        // xmlTree.root = xmlTree.root.parent;
    }
}