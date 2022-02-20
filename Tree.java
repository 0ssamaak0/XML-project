import java.util.ArrayList;

public class Tree {
    Node root = new Node();

    public Tree() {

    }

    public Tree(String tag) {
        root.tag = tag;
        root.depth = -1;
        root.children = new ArrayList<Node>();

    }

    void add_node(Node parent, Node child) {
        child.depth = parent.depth + 1;
        child.parent = parent;
        parent.children.add(child);
    }

    void preorder_traversal(Node node) {

        System.out.print(node.tag + " ");
        if (node.value != "") {
            System.out.print("(" + node.value + ") ");
        }

        for (int i = 0; i < node.children.size(); i++) {
            preorder_traversal(node.children.get(i));

        }
    }
}

class Node {
    String tag;
    String value = "";

    String attributeName = "";
    String attributeValue = "";

    Node parent;
    ArrayList<Node> children = new ArrayList<Node>();
    ArrayList<Node> brothers = new ArrayList<Node>();
    int depth;

    boolean comment = false;

    public Node() {

    }

    public Node(String tag) {
        this.tag = tag;
    }

    public void get_brothers() {
        for (int i = 0; i < parent.children.size(); i++) {
            brothers.add(parent.children.get(i));
        }
    }
}
