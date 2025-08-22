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
        LocalDate startDate = LocalDate.parse(details.substring(startIndex + 6, endIndex - 1));
        LocalDate endDate = LocalDate.parse(details.substring((endIndex + 4)));
        this.start = startDate;
        this.end = endDate;
    }

    public Event(String description, String details, boolean isDone) {
        super(description, isDone);
        int startIndex = details.indexOf("from: ");
        int endIndex = details.indexOf("to: ");
        LocalDate startDate = LocalDate.parse(details.substring(startIndex + 6, endIndex - 1));
        LocalDate endDate = LocalDate.parse(details.substring((endIndex + 4), details.length() - 1));
        this.start = startDate;
        this.end = endDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }
}