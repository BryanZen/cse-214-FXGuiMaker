/**
 * @Author Bryan Zen 113252725
 * @version 1.0
 * @since 2021-11-03
 */

public class ElementNotFoundException extends Exception {
    /**
     * Throws exception when a selected queue is empty.
     * @param message Extends Exception class, returns a string output.
     */
    public ElementNotFoundException(String message) {
        super(message);
    }
}
