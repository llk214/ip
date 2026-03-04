package claude;

/**
 * Represents a todo task without any date constraints.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the given description.
     *
     * @param description The description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * {@inheritDoc}
     * Includes the todo type prefix.
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}