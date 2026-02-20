package claude;

/**
 * Represents a task that spans a time period with a start and end time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new Event task with the given description, start and end times.
     *
     * @param description The description of the event.
     * @param from The start time of the event.
     * @param to The end time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from + "-" + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}