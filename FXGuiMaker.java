/**
 * @Author Bryan Zen 113252725
 * @version 1.0
 * @since 2021-11-03
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Write a fully-documented class named FXGuiMaker that takes in a text file,
 * generates a FXComponentTree and provides an interface for a user to
 * manipulate the tree. Please see the UI required functions for the
 * required functionality
 */
public class FXGuiMaker {
    /**
     * The main method runs a menu-driven application which first creates an
     * FXComponentTree based on the passed in file and then prompts the user
     * for a menu command selecting the operation. The required information is
     * then requested from the user based on the selected operation. You can
     * find the list of menu options in the UI required functions section.
     * @param args
     * @throws ElementNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws ElementNotFoundException,
            IOException {
        Scanner sc = new Scanner(System.in);
        FXComponentTree tree = new FXComponentTree();

        System.out.println("Welcome to counterfeit SceneBuilder.");
        System.out.println();
        System.out.println("""
                    Menu:
                    L) Load from file
                    P) Print tree
                    C) Move cursor to a child node
                    R) Move cursor to root
                    A) Add a child
                    U) Cursor up (to parent)
                    E) Edit text of cursor
                    D) Delete child
                    S) Save to file
                    X) Export FXML
                    Q) Quit""");
        boolean x = true;

        while(x){
            System.out.println("Please select an option:");
            char choice = sc.next().charAt(0);
            try{
                if (choice >= '0' && choice <= '9'){
                    throw new Exception("Bad input! ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            choice = Character.toUpperCase(choice);
            switch(choice){
                case 'L' -> {
                    System.out.println("Please enter filename: ");
                    sc.nextLine();
                    String file = sc.nextLine();
                    tree = tree.readFromFile(file);
                    System.out.printf("%s loaded", file);
                    System.out.println();
                }
                case 'P' -> {
                    print(tree);
                }
                case 'C' -> {
                    System.out.println("Please enter number of child " +
                            "(starting with 1): ");
                    int index = sc.nextInt();
                    String type = "";
                    type = String.valueOf(tree.getCursor().getChildren()
                            [index - 1].getType());
                    tree.cursorToChild(index);
                    System.out.printf("Cursor Moved to %s", type);
                    System.out.println();
                }
                case 'R' -> {
                    tree.cursorToRoot();
                    System.out.println("Cursor is at root.");
                }
                case 'A' -> {
                    System.out.println("Select component type (H - HBox, " +
                            "V - VBox, T - TextArea, B - Button, L - Label): ");
                    char choiceA = sc.next().charAt(0);
                    try{
                        if (choiceA >= '0' && choiceA <= '9'){
                            throw new Exception("Bad input! ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    String type = "";
                    choiceA = Character.toUpperCase(choiceA);
                    switch (choiceA){
                        case 'H' -> {
                            type = "HBox";
                        }
                        case 'V' -> {
                            type = "VBox";
                        }
                        case 'T' -> {
                            type = "TextArea";
                        }
                        case 'B' -> {
                            type = "Button";
                        }
                        case 'L' -> {
                            type = "Label";
                        }
                    }
                    ComponentType typeA = ComponentType.valueOf(type);
                    System.out.println("Please enter text: ");
                    sc.nextLine();
                    String text = sc.nextLine();
                    System.out.println("Please enter an index: ");
                    int index = sc.nextInt();

                    FXTreeNode newChild = new FXTreeNode(text, typeA,
                            tree.getCursor(), tree.getCursor().getDepth()
                            + 1, tree.getCursor().getChildren()[index - 1]
                            .getCodeName());
                    tree.addChild(newChild);
                }
                case 'U' -> {
                    tree.cursorToParent();
                    String type = "";
                    type = String.valueOf(tree.getCursor().getType());
                    System.out.printf("Cursor Moved to %s", type);
                    System.out.println();
                }
                case 'E' -> {
                    System.out.println("Please enter new text: ");
                    sc.nextLine();
                    String text = sc.nextLine();
                    tree.getCursor().setText(text);
                    System.out.println("Text Edited.");
                }
                case 'D' -> {
                    String type = "";
                    System.out.println("Please enter number of child " +
                            "(starting with 1): ");
                    int index = sc.nextInt();
                    type = String.valueOf(tree.getCursor().getChildren()
                            [index - 1].getType());
                    tree.deleteChild(index);
                    System.out.printf("%s removed.", type);
                    System.out.println();
                }
                case 'S' -> {
                    System.out.println("Please enter a filename: ");
                    sc.nextLine();
                    String fileName = sc.nextLine();
                    File myObj = new File("/Users/bryanzen/" +
                            "IdeaProjects/cse214_hw5/" + fileName);
                    tree.writeToFile(fileName);
                    System.out.println("File saved.");
                }
                case 'X' -> {
                }
                case 'Q' -> {
                    System.out.println("Make like a tree and leave!");
                    x = false;
                }
            }
        }
    }

    /**
     * The printing method, uses the sort node list function
     * @param tree is the tree that we are printing
     */
    public static void print(FXComponentTree tree){
        FXTreeNode[] sortedList = tree.sortNodeList();
        for (FXTreeNode fxTreeNode : sortedList){
            for (int i = 0; i < fxTreeNode.getDepth(); i++){
                System.out.print("   ");
            }
            if (fxTreeNode.equals(tree.getCursor())){
                System.out.print("==>");
            } else if (fxTreeNode.getDepth() == 1){
                System.out.print("--+");
            } else {
                System.out.print("+--");
            }
            System.out.print(fxTreeNode);
        }
    }
}
