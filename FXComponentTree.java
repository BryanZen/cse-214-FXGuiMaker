/**
 * @Author Bryan Zen 113252725
 * @version 1.0
 * @since 2021-11-03
 */

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

/**
 * Write a fully documented class called FXComponentTree which will serve as
 * the tree manager for your FXComponentTree. This must hold references into
 * a tree (the root and cursor), as well as be able to generate and save the
 * tree to and from a file. Again, defining some custom exceptions may be
 * helpful for code readability.
 */
public class FXComponentTree {
    private final FXTreeNode root;
    private FXTreeNode cursor;
    private int totalNodes;
    private FXTreeNode[] nodeList;
    private int maxDepth = 0;

    /**
     * The constructor fot the component tree, initializes with an Anchorpane
     */
    public FXComponentTree(){
        root = new FXTreeNode("", ComponentType.AnchorPane,
                null, 0, "0");
        cursor = root;
        totalNodes = 1;
        nodeList = new FXTreeNode[1];
        this.nodeList[0] = root;
    }

    /**
     * Sets the cursor to the root of the FXComponentTree
     */
    public void cursorToRoot(){
        cursor = root;
    }

    /**
     *
     * @return the current cursor node
     */
    public FXTreeNode getCursor(){
        return cursor;
    }

    /**
     *
     * @return the root node
     */
    public FXTreeNode getRoot(){
        return root;
    }

    /**
     *
     * @return the integer of total nodes
     */
    public int getTotalNodes(){
        return totalNodes;
    }

    /**
     *
     * @param totalNodes sets new value for total nodes
     */
    public void setTotalNodes(int totalNodes){
        this.totalNodes = totalNodes;
    }

    /**
     *
     * @param index the index of the cursor that needs to be deleted
     * @throws ElementNotFoundException thrown when there is no index
     */
    public void deleteChild(int index) throws ElementNotFoundException {
        FXTreeNode deleted = this.cursor.getChildren()[index-1];
        FXTreeNode[] cutList = new FXTreeNode[nodeList.length];
        System.arraycopy(nodeList, 0, cutList, 0,
                nodeList.length);
        if (deleted == null){
            throw new ElementNotFoundException("Child not found. ");
        } else{
            for (FXTreeNode fxTreeNode : nodeList){
                if (!Objects.equals(fxTreeNode.getCodeName(), "0")){
                    if (fxTreeNode.getParent() == deleted){
                        cutList = removeNodeFromList(fxTreeNode, cutList);
                    }
                }
            }
            this.nodeList = cutList;
            this.cursor.setChildren(removeNodeFromList(deleted,
                    this.cursor.getChildren()));
            this.cursor.setChildCount(this.cursor.getChildCount() - 1);
            this.nodeList = removeNodeFromList(deleted, nodeList);
        }
        resetCodeNames(nodeList);
    }

    /**
     * Resets the code names after deletion or insertion
     * @param nodeList is the node list used for correcting
     */
    public void resetCodeNames(FXTreeNode[] nodeList){
        for (FXTreeNode fxTreeNodes : nodeList){
            for (int i = 0; i < fxTreeNodes.getChildren().length;i++){
                FXTreeNode child = fxTreeNodes.getChildren()[i];
                char s = child.getCodeName().charAt(child.getCodeName()
                        .length() - 1);
                int x = Integer.parseInt(String.valueOf(s));
                if (x != i){
                    String[] childCode = child.getCodeName().split("");
                    String fix = String.valueOf(i);
                    StringBuilder sb = new StringBuilder();
                    for(int j = 0; j < childCode.length; j++){
                        if (j != childCode.length - 1){
                            sb.append(childCode[j]);
                        } else{
                            sb.append(fix);
                        }
                    }
                    String str = String.valueOf(sb);
                    child.setCodeName(str);
                    FXTreeNode parent;
                    FXTreeNode ptr = child;
                    while (ptr.getChildren().length != 0){
                        int n = 0;
                        for (FXTreeNode fxTreeNode : ptr.getChildren()){
                            if (fxTreeNode.getChildren().length != 0){
                                n = Integer.parseInt(String.valueOf
                                        (fxTreeNode.getCodeName().charAt
                                                (fxTreeNode.getCodeName()
                                                        .length() - 1)));
                            }
                        }
                        ptr = ptr.getChildren()[n];
                        parent = ptr.getParent();
                        for (FXTreeNode fxTreeNode : parent.getChildren()){
                            String parentCode = parent.getCodeName();
                            String[] nodeCode = fxTreeNode.getCodeName()
                                    .split("");
                            StringBuilder sNew = new StringBuilder();
                            for (int j = 0; j < nodeCode.length; j++){
                                if (j != nodeCode.length - 1){
                                    nodeCode[j] = String.valueOf(parentCode
                                            .charAt(j));
                                }
                                sNew.append(nodeCode[j]);
                            }
                            String newCode = String.valueOf(sNew);
                            fxTreeNode.setCodeName(newCode);
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds the given node to the corresponding index of the children array.
     * @param node is the node being inserted
     */
    public void addChild(FXTreeNode node){
        FXTreeNode[] children = new FXTreeNode[this.cursor.getChildren()
                .length + 1];
        boolean inserted = false;
        for (int i = 0; i < children.length; i++){
            if (inserted){
                children[i] = this.cursor.getChildren()[i - 1];
            } else{
                if (!Objects.equals(this.cursor.getChildren()[i]
                        .getCodeName(), node.getCodeName())){
                    children[i] = this.cursor.getChildren()[i];
                }else{
                    children[i] = node;
                    inserted = true;
                }
            }
        }
        this.cursor.setChildren(children);
        for (int i = 0; i < this.cursor.getChildren().length;i++){
            FXTreeNode child = this.cursor.getChildren()[i];
            char s = child.getCodeName().charAt(child.getCodeName()
                    .length() - 1);
            int x = Integer.parseInt(String.valueOf(s));
            if (x != i){
                String[] childCode = child.getCodeName().split("");
                String fix = String.valueOf(i);
                StringBuilder sb = new StringBuilder();
                for(int j = 0; j < childCode.length; j++){
                    if (j != childCode.length - 1){
                        sb.append(childCode[j]);
                    } else{
                        sb.append(fix);
                    }
                }
                String str = String.valueOf(sb);
                child.setCodeName(str);
            }
        }
        addToNodeList(node, nodeList);
        sortNodeList();
    }

    /**
     * Sets the current node’s text to the specified text.
     * @param text the new text
     */
    public void setTextAtCursor(String text){
        this.cursor.setText(text);
    }

    /**
     * Moves the cursor to the child node of the cursor corresponding
     * to the specified index.
     * @param index the new index
     */
    public void cursorToChild(int index){
        this.cursor = cursor.getChildren()[index - 1];
    }

    /**
     * Moves the cursor to the parent of the current node.
     */
    public void cursorToParent(){
        this.cursor = getCursor().getParent();
    }

    /**
     * Generates the FXComponentTree based on the file name that is passed in.
     * @param filename the name of the file being read
     * @return the tree created
     */
    public FXComponentTree readFromFile(String filename){
        File myObj;
        FXComponentTree tree = new FXComponentTree();
        try {
            myObj = new File("/Users/bryanzen/IdeaProjects/" +
                    "cse214_hw5/" + filename);
            Scanner sc = new Scanner(myObj);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] line = data.split(" ", 3);
                String[] treeBuild = line[0].split("-");
                StringBuilder code = new StringBuilder();
                FXTreeNode newNode;
                for (String s : treeBuild) {
                    code.append(String.format("%s", s));
                }
                String codeName = String.valueOf(code);
                String type = line[1];
                String text = "";
                try{
                    if (line[2] != null){
                        text = line[2];
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    text = "";
                }
                if(!Objects.equals(codeName, "0")){
                    newNode = new FXTreeNode(text, ComponentType.valueOf(type),
                            null, treeBuild.length - 1, codeName);
                    tree.setNodeList(tree.addToNodeList(newNode,
                            tree.getNodeList()));
                    tree.setMaxDepth(newNode.getDepth());
                    this.totalNodes = totalNodes + 1;
                }
            }
            tree.setParents();
            tree.setChildrenArrays();
        } catch (FileNotFoundException e) {
            System.out.printf("%s not found.", filename);
            e.printStackTrace();
        }
        return tree;
    }

    /**
     * sets the parent of the node using the code names
     */
    public void setParents(){
        for (FXTreeNode fxTreeNode : nodeList){
            if (Objects.equals(fxTreeNode.getCodeName(), "0")){
                fxTreeNode.setParent(null);
            } else{
                fxTreeNode.setParent(parentSearch(fxTreeNode.getCodeName()));
            }
        }
    }

    /**
     * Searches for the parent of the current node
     * @param code the code name of the node
     * @return the parent node
     */
    public FXTreeNode parentSearch(String code){
        StringBuilder pSearch = new StringBuilder();
        for (int i = 0; i < code.length() - 1; i++){
            pSearch.append(code.charAt(i));
        }
        String parentCode = String.valueOf(pSearch);
        FXTreeNode parent = null;
        boolean parentFound = false;
        while (!parentFound){
            for (FXTreeNode fxTreeNode : nodeList) {
                if (Objects.equals(fxTreeNode.getCodeName(), parentCode)) {
                    parent = fxTreeNode;
                    parentFound = true;
                    break;
                }
            }
        }
        return parent;
    }

    /**
     * Sets the new childrens array
     */
    public void setChildrenArrays(){
        for (FXTreeNode fxTreeNode : nodeList){
            int count = 0;
            for (FXTreeNode fxTreeNode1 : nodeList){
                if (fxTreeNode1 != nodeList[0]){
                    if (Objects.equals(fxTreeNode1.getParent().getCodeName(),
                            fxTreeNode.getCodeName())){
                        count++;
                    }
                }
            }
            fxTreeNode.setChildCount(count);
        }
        for (FXTreeNode fxTreeNode : nodeList){
            FXTreeNode[] children = new FXTreeNode[fxTreeNode.getChildCount()];
            int count = 0;
            for (FXTreeNode fxTreeNode1 : nodeList){
                if (fxTreeNode1 != nodeList[0]){
                    if (Objects.equals(fxTreeNode1.getParent().getCodeName(),
                            fxTreeNode.getCodeName())){
                        children[count++] = fxTreeNode1;
                    }
                }
            }
            fxTreeNode.setChildren(children);
        }
    }

    /**
     *
     * @param nodeList the new node list
     */
    public void setNodeList(FXTreeNode[] nodeList){
        this.nodeList = nodeList;
    }

    /**
     *
     * @return the node list for the current tree
     */
    public FXTreeNode[] getNodeList(){
        return nodeList;
    }

    /**
     *
     * @param node is the node being removed
     * @param nodeList the current list of nodes
     * @return the new list after removal
     */
    public FXTreeNode[] removeNodeFromList(FXTreeNode node,
                                           FXTreeNode[] nodeList){
        FXTreeNode[] newList = new FXTreeNode[nodeList.length - 1];
        int count = 0;
        for (FXTreeNode fxTreeNode : nodeList) {
            if (fxTreeNode != node) {
                newList[count++] = fxTreeNode;
            }
        }
        return newList;
    }

    /**
     *
     * @param node the node being added
     * @param oldList the list that needs to be added to
     * @return the new list
     */
    public FXTreeNode[] addToNodeList(FXTreeNode node, FXTreeNode[] oldList){
        FXTreeNode[] newNodeList = new FXTreeNode[oldList.length + 1];
        System.arraycopy(oldList, 0, newNodeList, 0,
                oldList.length);
        newNodeList[oldList.length] = node;
        this.nodeList = newNodeList;
        return newNodeList;
    }

    /**
     *
     * @return the node list sorted by priority
     */
    public FXTreeNode[] sortNodeList(){
        FXTreeNode[] cutList = new FXTreeNode[nodeList.length];
        System.arraycopy(nodeList, 0, cutList, 0,
                nodeList.length);
        FXTreeNode[] sortedList = new FXTreeNode[nodeList.length];
        String leadZero = "0";
        int sorted = 0;
        int maxZeros = getMaxZeros();
        int count = 0;
        while(count != maxZeros){
            for (FXTreeNode fxTreeNode : nodeList) {
                if (fxTreeNode.getCodeName().equals(leadZero)) {
                    sortedList[sorted++] = fxTreeNode;
                    cutList = removeNodeFromList(fxTreeNode, cutList);
                    count++;
                    leadZero += "0";
                    if (count == maxZeros){
                        break;
                    }
                }
            }
        }
        while (cutList.length > 0){
            boolean nextFound = false;
            FXTreeNode next = cutList[0];
            while (!nextFound){
                for (FXTreeNode fxTreeNode : cutList) {
                    if (fxTreeNode.getNodePriority() < next.getNodePriority()) {
                        next = fxTreeNode;
                    }
                }
                sortedList[sorted++] = next;
                cutList = removeNodeFromList(next, cutList);
                nextFound = true;
            }
        }
        setNodeList(sortedList);
        return nodeList;
    }

    /**
     * Generates a text file that reflects the structure of the FXComponentTree.
     * The format of the tree of the file should match the format of the
     * input file.
     * @param fileName the filename of the new file
     * @throws IOException caught and thrown by file writer
     */
    public void writeToFile(String fileName)throws IOException {
        String str = "";
        FileWriter myFile = new FileWriter(fileName);
        myFile.write(str);
        try {
            for (FXTreeNode fxTreeNode : nodeList){
                myFile.write(fxTreeNode.getCodeListDash());
                myFile.write(" ");
                myFile.write(String.valueOf(fxTreeNode.getType()));
                myFile.write(" ");
                if (!Objects.equals(fxTreeNode.getText(), "")){
                    myFile.write(fxTreeNode.getText());
                }
                myFile.write("\n");
            }
            myFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     *Postconditions:
     * A valid FXML file that can be opened in SceneBuilder has been created.
     * The dimensions do not matter too much, we will check by looking at the
     * tree view in the bottom right.
     * @param tree the tree being exported
     * @param filename the name of the file
     */
    public void exportToFXML(FXComponentTree tree, String filename){
    }

    /**
     *
     * @return the max depth of the tree
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     *
     * @param depth is compared to the previous max depth
     */
    public void setMaxDepth(int depth) {
        if (this.maxDepth < depth){
            this.maxDepth = depth;
        }
    }

    /**
     *
     * @return
     */
    public int getMaxZeros(){
        int count = 0;
        for (FXTreeNode fxTreeNode : this.nodeList){
            String[] code = fxTreeNode.getCodeName().split("");
            boolean x = true;
            for (int i = 0; i < code.length; i++){
                if (!Objects.equals(code[i], "0")) {
                    x = false;
                    break;
                }
                if (i == code.length - 1){
                    count++;
                }
            }
        }
        return count;
    }
}
