package claude;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    protected String by;
    protected LocalDate byDate;

    /**
     * Creates a new Deadline task with the given description and due date.
     * If the due date is in yyyy-mm-dd format, it is stored as a LocalDate.
     *
     * @param description The description of the deadline task.
     * @param by The due date or time for this task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        try {
            this.byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            this.byDate = null;
        }
    }

    /**
     * Returns the parsed due date, or null if the date is not in yyyy-mm-dd format.
     *
     * @return The due date as a LocalDate, or null.
     */
    public LocalDate getByDate() {
        return byDate;
    }

    /**
     * {@inheritDoc}
     * Includes the deadline type prefix and due date.
     */
    @Override
    public String toFileString() {
        if (byDate != null) {
            return "D | " + super.toFileString() + " | " + byDate;
        }
        return "D | " + super.toFileString() + " | " + by;
    }

    /**
     * {@inheritDoc}
     * Formats the due date using "MMM d yyyy" if parseable, otherwise uses the raw string.
     */
    @Override
    public String toString() {
        if (byDate != null) {
            return "[D]" + super.toString() + " (by: "
                    + byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH))
                            .replace("Sep ", "Sept ") + ")";
        }
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
