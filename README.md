# XML-Project
```python
stack.push("CSE CSE331s | Data Structures and Algorithms")
stack.push("Final Project")
stack.push("Phase 1")
```
## Background
In the first phase of this project, we are going to develop a tool that allows the user to check if the XML file is valid, and if it is. He will be able to format it to increase the readability and to be more informative. Furthermore, many languages do not have XML parsers as perfect as JSON parsers, so the user is able to convert the XML file to a JSON file.

For more complex and bigger relationships. Many XML files may be used, and the files size may increase too, which consumes data and time. So, we have provided the user with the ability to compress the file and reduce its size.

## Implementation Details
### 1. Reading and Parcing
What happens under the hood? Once the file path is written. It will be stored into a **Buffered Reader** object, which is passed immediately to [fileReader](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L67) function, which converts the buffered reader into an **ArrayList** of strings, with each element corresponds to a line in the XML file.

In order to increase the performance of the operations and simplify the next functions. we stored the contents of the **ArrayList** into a single String, with no spaces, tabs nor new lines using [xml_parser](https://github.com/0ssamaak0/XML-Project/blob/main/Phase1_1.java#L80).

### 2.	The XML Tree
Having the XML file in string format, now we convert it to a **Tree** to make the best use of it.

Our approach was to generalize the tree node as far as we can, to handle any unpredicted cases. Make more flexibility in the following functions, even adding more features to it.
In terms of data members each node is as shown in the figure below:

![Tree node](https://github.com/0ssamaak0/XML-Project/blob/main/Tree_node.png)

The tree is also provided with method to add a new node as a child to a specific parent. And since we have way to access the siblings directly, we made a function that goes back to the parent, and get that parent’s children, including of course the node itself

### 3. Validation
Since we always need to check that every tag must match the last opening tag, we have chosen to use a **Stack**. Where the top of the stack is the most recent opening tag. If we find its closing tag. We pop it and proceed to the next one .

If the closing tag doesn’t match the current top of the stack. We add the error in detail to the **Errors ArrayList** with the index of the line caused the problem.

After the parsing is completed, we check some cases, like if the stack is still not empty if no errors and none of these cases occurred. We inform the user that the file is a valid XML file. And he is able to perform the next operations

![Validating the XML file](https://github.com/0ssamaak0/XML-Project/blob/main/Validating_the_XML_file.png)

### 4. Formatting
The idea behind the formatting is quite similar to the idea behind the validation. since every node when constructed is taking its parent’s (depth + 1). We used this depth to iterate through a loop and indent every tag. 

We have preferred using a behavior similar to the **PreOrderTraversal** to add all the nodes to the **Formatted** Srting, and to achieve more abstraction in our code, we have made a function that handles the XML declaration if any and call our traversal function on the root of our tree.

### 5. Convering to JSON
Converting the XML file to JSON is somehow easy process in most cases. We replace the tag with double quotes, and we add the column and the brackets. But the main issue is if we had a JSON lists? How can we detect if the current node is the beginning of a series of similar nodes?

This led us to start modifying or Formatting traversal function. And reusing it with many details and parameters. In this function we really started using the Tree data members which may seemed to be trivial in the first look.


```Java
// Pseudocode to check the 3 cases for each node
if this node has siblings with similar names:
    if this is the first child of the parent:
        print(tag + [:) // e.g., "user:["
    else:
    "don't print the tag name"
else:
    print(tag + {}:) // e.g., "user:{"
```

and for every node, we check also if it’s a leaf or not, to determine whether we just print it with its value or open a new curly bracket.
Lastly, we checked if this node is the last sibling. To avoid adding the comma “,” after it.

## Known Issues
~~1- Adding an attribute to a tag corrupts the validation process. This can be solved by adding a condition to completely ignore the attribution and push only the tag name to the stack.~~ (Fixed ✅)

2- Adding a comment corrupts the converting to JSON process, since the comment is considered as a node. So, it adds 1 to the number of children. This can be solved by checking if there’s a comment, we can add an integer that indicates the number of comments under a given node.
