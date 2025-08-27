package chatot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Represents an event with start and end dates.
 */
class Event extends Task {
    protected LocalDate start;
    protected LocalDate end;

    /**
     * Creates a new event with description and date range. Main constructor used by all functions except loading saved data
     * @param description the task description
     * @param details string containing start and end dates with "/from " and "/to " prefixes
     */
    public Event(String description, String details) {
        super(description);
        int startIndex = details.indexOf("/from ");
        int endIndex = details.indexOf("/to ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String startDateStr = details.substring(startIndex + 6, endIndex - 1);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.start = LocalDate.parse(startDateStr, formatter);
                break;
            } catch (Exception e) {}
        }

        String endDateStr = details.substring((endIndex + 4));
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.end = LocalDate.parse(endDateStr, formatter);
                break;
            } catch (Exception e) {}
        }
    }

    /**
     * Creates an event object from saved data.
     * @param description the task description
     * @param details string containing dates with "from: " and "to: " prefixes
     * @param isDone whether the task is completed
     */
    public Event(String description, String details, boolean isDone) {
        super(description, isDone);
        int startIndex = details.indexOf("from: ");
        int endIndex = details.indexOf("to: ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String startDateStr = details.substring(startIndex + 6, endIndex - 1);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.start = LocalDate.parse(startDateStr, formatter);
                break;
            } catch (Exception e) {}
        }

        String endDateStr = details.substring((endIndex + 4), details.length() - 1);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.end = LocalDate.parse(endDateStr, formatter);
                break;
            } catch (Exception e) {}
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " to: " + end.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}