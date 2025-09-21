package chatot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
/**
 * Represents a deadline with a due date.
 */
class Deadline extends Task {
    protected LocalDate by;
    private static final int PREFIX_LENGTH = 4;

    /**
     * Creates a new deadline object/instance. Mainly used for all functions besides extracting saved data.
     * @param description the task description
     * @param by the string containing the due date with "/by " prefix
     */
    public Deadline(String description, String by) {
        super(description);
        int index = by.indexOf("/by ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String dateStr = by.substring(index + PREFIX_LENGTH);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.by = LocalDate.parse(dateStr, formatter);
                break;
            } catch (Exception e) {
                System.out.println("Could not parse date");
            }
        }
        assert this.by != null : "Deadline end date should not be null";
    }

    /**
     * Creates a deadline object. Mainly for loading saved data.
     * @param description the task description
     * @param by the string containing the due date with "by: " prefix
     * @param isDone whether the task is completed
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        int index = by.indexOf("by: ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String dateStr = by.substring(index + PREFIX_LENGTH);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.by = LocalDate.parse(dateStr, formatter);
                break;
            } catch (Exception e) {
                System.out.println("Could not parse date");
            }
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}