package chatot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Event extends Task {
    protected LocalDate start;
    protected LocalDate end;

    public Event(String description, String details) {
        super(description);
        int startIndex = details.indexOf("/from ");
        int endIndex = details.indexOf("/to ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.start = LocalDate.parse(details.substring(startIndex + 6, endIndex - 1), formatter);
                this.end = LocalDate.parse(details.substring((endIndex + 4)), formatter);
            } catch (Exception e) {}
        }
    }

    public Event(String description, String details, boolean isDone) {
        super(description, isDone);
        int startIndex = details.indexOf("from: ");
        int endIndex = details.indexOf("to: ");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                this.start = LocalDate.parse(details.substring(startIndex + 6, endIndex - 1), formatter);
                this.end = LocalDate.parse(details.substring((endIndex + 4), details.length() - 1), formatter);
            } catch (Exception e) {}
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " to: " + end.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}