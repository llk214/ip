/**
 * Represents an exception specific to the Claude chatbot.
 */
public class ClaudeException extends Exception {

    /**
     * Creates a new ClaudeException with the given error message.
     *
     * @param message The error message describing what went wrong.
     */
    public ClaudeException(String message) {
        super(message);
    }
}
