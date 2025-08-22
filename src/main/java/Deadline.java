import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        int index = by.indexOf("/by ");
        this.by = LocalDate.parse(by.substring(index + 4));
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        int index = by.indexOf("by: ");
        this.by = LocalDate.parse(by.substring(index + 4));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}