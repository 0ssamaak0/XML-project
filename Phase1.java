import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Stack;


public class Phase1 {
    // Data members

    // xml Declaration (if any)
    public String xmlDeclaration = "";

    // formatted XML
    public String formatted = "";

    // Json text
    public String JSONified = "";

    // ArrayList of all errors
    private ArrayList<String> errors = new ArrayList<String>();

    // Stack for validation
    Stack<String> stack = new Stack<String>();

    String allerrors = "";
    
    boolean valid = true;

    Parsing parser;

    // Constructor
    public Phase1(BufferedReader xml_file) {
        parser = new Parsing(xml_file);

        // Part1
        xml_validator();

        //Hint: only for valid XML file

        parser.tree_creator();

        // Part2
        formatting_maker();

        // Part3
        JSONIfy();

        // Part4: Compression is in a separated file
    }

    // Validating XML, gettint the errors and filling xml_text
    public String xml_validator() {
        // Checking if there's (any) error
        boolean error = false;

        // iterating the ArrayList of the parser.lines by index (by index to indicate the parser.lines
        // with errors)
        for (int lineindx = 0; lineindx < parser.lines.size(); lineindx++) {

            // ignoring spaces
            try {
                while (parser.lines.get(lineindx).charAt(0) == ' ') {
                    parser.lines.set(lineindx, parser.lines.get(lineindx).substring(1));
                }
            } catch (Exception e) {
                System.out.print("");
            }

            // Neglect the declaration
            try {
                if (parser.lines.get(lineindx).substring(0, 2).compareTo("<?") == 0) {
                    System.out.println("Version");
                    allerrors += "Version\n";
                }

                // Neglect the comments
                else if (parser.lines.get(lineindx).substring(0, 2).compareTo("<!") == 0) {
                    System.out.println("Comment");
                    allerrors += "Comment\n";
                }

                else {
                    // By default: the text is not (opening nor closing) tag
                    boolean readingopen = false;
                    boolean readingclose = false;
                    boolean attribute = false;

                    // initializing the tag with empty value
                    String tag = "";

                    // iterating through the chars of a single line
                    for (int i = 0; i < parser.lines.get(lineindx).length(); i++) {

                        // checking if it's a/n (oepning or closing) tag
                        if (parser.lines.get(lineindx).charAt(i) == '<') {

                            // check if it's a closing tag
                            if (parser.lines.get(lineindx).charAt(i + 1) == '/') {
                                i++;
                                readingclose = true;
                                readingopen = false;

                                // resetting the value to empty

                                tag = "";
                            }

                            // check if it's an opening tag
                            else {
                                readingopen = true;
                                readingclose = false;

                                tag = "";
                            }
                            i++;
                        }
                        // check the end of a tag (opening or closing)
                        else if (parser.lines.get(lineindx).charAt(i) == '>') {

                            // if it's the end of an opening tag: push
                            if (readingopen) {
                                stack.push(tag);
                                // xml_text += ("<" + tag + ">");
                                System.out.println(tag + " is pushed!");
                                allerrors += tag + " is pushed! \n";
                            }

                            // if it's the end of a closing tag: pop
                            else if (readingclose) {

                                // Checking of the stack is empty: ERROR!
                                if (stack.empty()) {
                                    errors.add("line: " + lineindx + ": stack is empty! the tag " + tag
                                            + " has no opening tag!");
                                    error = true;

                                }
                                // Checking if the stack has the correspoding opening tag : no error
                                else if (stack.peek().compareTo(tag) == 0) {
                                    stack.pop();
                                    System.out.println(tag + " is popped!");
                                    allerrors += tag + " is popped! \n";
                                }
                                // Checking if the stack doesn't have to correspoding opening tag: ERROR!
                                else {
                                    errors.add("line " + lineindx + ": " + tag
                                            + " is here but stack.top == " + stack.peek());
                                    error = true;
                                }
                            }
                            readingopen = false;
                            readingclose = false;
                            attribute = false;
                        }
                        // Check for attribute
                        if (attribute) {
                            while (parser.lines.get(lineindx).charAt(i) != '>') {
                                i++;
                            }
                            i--;
                        }
                        // If it's a tag: write into tag
                        if ((readingopen || readingclose) && (!attribute)) {

                            if (parser.lines.get(lineindx).charAt(i) != ' ') {
                                tag += parser.lines.get(lineindx).charAt(i);
                            }

                            else {
                                attribute = true;
                            }
                        }
                    }
                }
            }

            catch (Exception e) {
                System.out.print("");
            }
        }
        // If the stack isn't empty: print the tag that hasn't been closed
        if (!stack.empty()) {
            System.out.println("The tag " + stack.peek() + " hasn't been closed");
            allerrors += "The tag " + stack.peek() + " hasn't been closed\n";
        }
        // If there're any errors: print them
        else if (error) {

            for (String errorinerrors : errors) {
                System.out.println(errorinerrors);
                allerrors += errorinerrors;
            }

        }
        // If not
        else {
            System.out.println("No Errors 👍");
            allerrors += "No Errors 👍";
        }
        return allerrors;
    }

    // calling the recursive function
    public void formatting_maker() {
        // checking for declaration, if there's add it first
        if (xmlDeclaration.compareTo("") != 0) {
            formatted += xmlDeclaration + "\n";
        }
        formatted_traversal(parser.xmlTree.root);

    }

    // the formatting recursive function, uses preorder traversal
    public void formatted_traversal(Node node) {
        // indenting with tag's depth
        for (int i = 0; i < node.depth; i++) {
            formatted += "\t";
        }
        // if the node is comment, add it in a new line
        if (node.tag.charAt(0) == '<' && node.tag.charAt(1) == '!') {
            formatted += node.tag + "\n";
        }
        // adding the tag, new line of it's a parent
        else {
            formatted += "<" + node.tag;
            if (node.attributeName.compareTo("") != 0) {
                formatted += " " + node.attributeName + "=\"" + node.attributeValue + "\"";
            }
            if (node.children.size() != 0) {
                formatted += ">\n";
            }
            // adding the tag in a single line if there's a leaf
            else {
                formatted += ">";
            }
        }
        // adding the value, if there's
        if (node.value != "") {
            formatted += node.value;
        }
        for (int i = 0; i < node.children.size(); i++) {
            formatted_traversal(node.children.get(i));
        }
        if (node.children.size() != 0) {
            for (int i = 0; i < node.depth; i++) {
                formatted += "\t";
            }
        }
        // if the node is comment, don't add it's closing!
        if (node.tag.charAt(0) == '<' && node.tag.charAt(1) == '!') {
            formatted += "";
        } else {
            formatted += "</" + node.tag + ">\n";
        }

    }

    public void JSONIfy() {
        Node lastNode;
        boolean last = false;

        // first node (root)
        JSONified += "{\n";
        JSONified += "\t\"" + parser.xmlTree.root.tag + "\": {\n";

        if (parser.xmlTree.root.value.compareTo("") != 0) {
            JSONified += "\"" + parser.xmlTree.root.value + "\"";
        }

        for (int i = 0; i < parser.xmlTree.root.children.size(); i++) {
            // Making last node is the parent for the first node, else the lastest brother
            if (i == 0) {
                lastNode = parser.xmlTree.root;
            } else {
                lastNode = parser.xmlTree.root.children.get(i - 1);
            }
            // flag the last node for ","
            if (i == parser.xmlTree.root.children.size() - 1) {
                last = true;
            }
            JSONify_traversal(parser.xmlTree.root.children.get(i), lastNode, last, i);
        }
        JSONified += "\t}\n";
        JSONified += "\n}";
    }

    public void JSONify_traversal(Node node, Node lastNode, boolean last, int order) {
        boolean square = false;

        // If comment, neglect it
        if (node.tag.charAt(0) == '<' && node.tag.charAt(1) == '!') {
            // JSONified += "//" + node.tag.substring(2, node.tag.length() - 2) + "\n";
            return;
        }

        // if the node's tag equals lastest brother's tag (twin)
        if (node.tag.compareTo(lastNode.tag) == 0) {
            // if it has children
            if (node.children.size() > 0) {
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "{\n";
                for (int i = 0; i < node.children.size(); i++) {
                    if (i == 0) {
                        lastNode = node;
                    } else {
                        lastNode = node.children.get(i - 1);
                    }
                    if (i == node.children.size() - 1) {
                        last = true;
                    } else {
                        last = false;
                    }
                    JSONify_traversal(node.children.get(i), lastNode, last, i);
                }
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "}";
            }
            // if it's a leaf
            else {
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "\"" + node.value + "\"";
            }
            JSONified += '\n';
            Node parent = node.parent;
            if (order == parent.children.size() - 1) {
                // indentation
                for (int i = 0; i < node.depth + 1; i++) {
                    JSONified += "\t";
                }
                JSONified += "]";

                if (!last) {
                    JSONified += ",";
                }
                JSONified += '\n';
            }
            boolean lastone = false;
            try {
                parent.children.get(order + 1);
            } catch (Exception e) {
                lastone = true;
            }
            if (!lastone) {
                if (parent.children.get(order + 1).tag.compareTo(node.tag) != 0) {
                    // indentation
                    for (int i = 0; i < node.depth + 1; i++) {
                        JSONified += "\t";
                    }
                    JSONified += "]";

                    if (!last) {
                        JSONified += ",";
                    }
                    JSONified += '\n';
                }
            }
            return;

        }

        // First child: check if there're twin brothers
        node.get_brothers();
        for (int i = 0; i < node.brothers.size(); i++) {
            if (node.brothers.get(i).tag.compareTo(node.tag) == 0 && i != order) {
                square = true;
                break;
            }
        }
        if (square) {
            // indentation
            for (int i = 0; i < node.depth + 1; i++) {
                JSONified += "\t";
            }
            JSONified += "\"" + node.tag + "\": [\n";
            // check if it has children
            if (node.children.size() > 0) {
                // indentation
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                // adding the "{", calling the children
                JSONified += "{\n";
                for (int i = 0; i < node.children.size(); i++) {
                    if (i == 0) {
                        lastNode = node;
                    } else {
                        lastNode = node.children.get(i - 1);
                    }
                    if (i == node.children.size() - 1) {
                        last = true;
                    } else {
                        last = false;
                    }

                    JSONify_traversal(node.children.get(i), lastNode, last, i);
                }
                // closing
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "},";
            } else {
                // indentation
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "\"" + node.value + "\"";
            }
            if (!last) {
                JSONified += ",";
            }
            JSONified += '\n';
            square = false;
            return;
        }

        // no twins
        else {
            // check if it has children
            if (node.children.size() > 0) {
                // indentation
                for (int i = 0; i < node.depth + 1; i++) {
                    JSONified += "\t";
                }
                // adding the "{", calling the children
                JSONified += "\"" + node.tag + "\": {\n";
                for (int i = 0; i < node.children.size(); i++) {
                    if (i == 0) {
                        lastNode = node;
                    } else {
                        lastNode = node.children.get(i - 1);
                    }
                    if (i == node.children.size() - 1) {
                        last = true;
                    } else {
                        last = false;
                    }
                    JSONify_traversal(node.children.get(i), lastNode, last, i);
                }
                // closing
                for (int i = 0; i < node.depth + 2; i++) {
                    JSONified += "\t";
                }
                JSONified += "}";
            } else {
                // indentation
                for (int i = 0; i < node.depth + 1; i++) {
                    JSONified += "\t";
                }
                JSONified += "\"" + node.tag + "\": ";
                JSONified += "\"" + node.value + "\"";
            }
            if (!last) {
                JSONified += ",";
            }
            JSONified += '\n';
            return;
        }

    }

}
