class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        int index = by.indexOf("/by ");
        this.by = by.substring(index + 4);
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        int index = by.indexOf("/by ");
        this.by = by.substring(index + 4);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}