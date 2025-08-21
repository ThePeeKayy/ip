class Event extends Task {
    protected String start;
    protected String end;

    public Event(String description, String details) {
        super(description);
        int startIndex = details.indexOf("/from ");
        int endIndex = details.indexOf("/to ");
        this.start = details.substring(startIndex + 6, endIndex);
        this.end = details.substring(endIndex + 4);
    }

    public Event(String description, String details, boolean isDone) {
        super(description, isDone);
        int startIndex = details.indexOf("/from ");
        int endIndex = details.indexOf("/to ");
        this.start = details.substring(startIndex + 6, endIndex);
        this.end = details.substring(endIndex + 4);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + "to: " + end + ")";
    }
}