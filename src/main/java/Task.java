class Task {
    private String description;
    private boolean isDone;

    // For add
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    // For mark/unmark
    public void switchDone() {
        this.isDone = !this.isDone;
    }

    public String getDescription() {
        return description;
    }

    public boolean getDone() {
        return isDone;
    }

    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}