package chatot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        int index = by.indexOf("/by ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String dateStr = by.substring(index + 4);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.by = LocalDate.parse(dateStr, formatter);
                break;
            } catch (Exception e) {}
        }
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        int index = by.indexOf("by: ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };

        String dateStr = by.substring(index + 4);
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.by = LocalDate.parse(dateStr, formatter);
                break;
            } catch (Exception e) {}
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}