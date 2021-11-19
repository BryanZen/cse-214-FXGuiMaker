/**
 * @Author Bryan Zen 113252725
 * @version 1.0
 * @since 2021-11-03
 */

import java.util.Objects;

/**
 *Write a fully documented class called FXTreeNode which holds the type of
 * component being represented, an array of children (null if this will be a
 * Control), and string for the text (null if this is a Container). Be sure to
 * include all getters and setters. You may find it helpful to write helper
 * methods, especially to check input before adding a child to the tree.
 * Defining custom exceptions for actions like trying to add too many children
 * to a node, or adding a child in an invalid position may also be desirable.
 */
public class FXTreeNode {
    private String text;
    private ComponentType type;
    private FXTreeNode parent;
    final int maxChildren = 10;
    private FXTreeNode[] children =  new FXTreeNode[maxChildren];
    private int childCount;
    private int depth;
    private String codeName;
    private int leadingZeros;
    private int nodeNumber;
    private int nodePriority;

    /**
     *
     * @param text The text of the node
     * @param type the component type of the node
     * @param parent the parent of the node
     * @param depth the depth of the node on the tree
     * @param codeName the name of the node, it is used in sorting and ordering
     */
    public FXTreeNode(String text, ComponentType type, FXTreeNode parent,
                      int depth, String codeName){
        this.text = text;
        this.type = type;
        this.parent = parent;
        this.depth = depth;
        childCount = 0;
        this.codeName = codeName;
        this.setNodeNumber(codeName);
        this.setLeadingZeros(codeName);
        this.setNodePriority();
    }

    /**
     *
     * @return text
     */
    public String getText(){
        return text;
    }

    /**
     *
     * @return type as componentType
     */
    public ComponentType getType(){
        return type;
    }

    /**
     *
     * @return the parent of the node
     */
    public FXTreeNode getParent(){
        return parent;
    }

    /**
     *
     * @return the children array of the node
     */
    public FXTreeNode[] getChildren(){
        return children;
    }

    /**
     *
     * @return the number of children the node has
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     *
     * @return the depth of the node on the tree
     */
    public int getDepth() {
        return depth;
    }

    /**
     *
     * @return the code name of node
     */
    public String getCodeName() {
        return codeName;
    }

    /**
     *
     * @param text is the new text
     */
    public void setText(String text){
        this.text = text;
    }

    /**
     *
     * @param type is the new type
     */
    public void setType(ComponentType type){
        this.type = type;
    }

    /**
     *
     * @param parent is the new parent
     */
    public void setParent(FXTreeNode parent){
        this.parent = parent;
    }

    /**
     *
     * @param children is the new children array
     */
    public void setChildren(FXTreeNode[] children){
        this.children = children;
    }

    /**
     *
     * @param childCount the new amount of children
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    /**
     *
     * @param depth the new depth of the node
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     *
     * @param codeName the new codename of the node
     */
    public void setCodeName(String codeName) {
        this.codeName = codeName;
        this.setLeadingZeros(codeName);
        this.setNodeNumber(codeName);
        this.setNodePriority();
    }

    /**
     *
     * @return the printed our format of the node
     */
    public String toString(){
        String x = "";
        if (this.type != ComponentType.AnchorPane &&
                this.type != ComponentType.HBox &&
                this.type != ComponentType.VBox){
            x = String.format("%s: %s\n", this.type, this.text);
        } else{
            x = String.format("%s\n", this.type);
        }
        return x;
    }

    /**
     *
     * @return the leading zeros in the code name
     */
    public int getLeadingZeros() {
        return leadingZeros;
    }

    /**
     *
     * @param codeName computes the new leading zeros from the codename
     */
    public void setLeadingZeros(String codeName) {
        String[] treeBuild = codeName.split("");
        int zeroCount = 0;
        for (int i = 0; i < treeBuild.length; i++) {
            if (!Objects.equals(treeBuild[i], "0") ||
                    i == treeBuild.length - 1) {
                break;
            } else{
                zeroCount++;
            }
        }
        this.leadingZeros = zeroCount;
    }

    /**
     *
     * @return the number of the node, the codename without the leading zeros
     */
    public int getNodeNumber() {
        return nodeNumber;
    }

    /**
     *
     * @param codeName sets the number of the node, the codename without the leading zeros
     */
    public void setNodeNumber(String codeName) {
        String[] treeBuild = codeName.split("");
        boolean leading = true;
        StringBuilder nodeNum = new StringBuilder();
        for (String s : treeBuild) {
            if (!Objects.equals(s, "0") ||
                    Objects.equals(s, treeBuild[treeBuild.length - 1])) {
                leading = false;
            }
            if (!leading) {
                nodeNum.append(String.format("%s", s));
            }
        }
        this.nodeNumber = Integer.parseInt(String.valueOf(nodeNum));
    }

    /**
     *
     * @return the priority of the node used in sorting/print
     */
    public int getNodePriority() {
        return nodePriority;
    }

    /**
     * sets the node priority used in sorting using the code name values
     */
    public void setNodePriority() {
        this.nodePriority = (leadingZeros * -10) + nodeNumber;;
    }

    /**
     *
     * @return the code name as a string interleaved with dashes
     */
    public String getCodeListDash(){
        String[] code = this.codeName.split("");
        StringBuilder codeListDash = new StringBuilder();
        for (int i = 0; i < code.length; i++){
            if (i != code.length - 1){
                codeListDash.append(code[i]);
                codeListDash.append("-");
            } else{
                codeListDash.append(code[i]);
            }
        }
        return String.valueOf(codeListDash);
    }
}
