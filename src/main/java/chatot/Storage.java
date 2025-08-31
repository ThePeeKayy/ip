package chatot;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Handles text file interaction for basic storage.
 */
class Storage {
    private static final int TASK_DONE_POSITION = 4;
    private static final int TASK_TYPE_POSITION = 1;
    private static final int TASK_DESC_POSITION = 7;
    private static final int TASK_TIME_WHITESPACE_LENGTH = 2;
    private String filePath;

    /**
     * Creates a storage handler with the specified file path.
     * @param filePath path to the storage file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from toString() tasks.
     * @return list of task objects
     * @throws Exception if file reading fails
     */
    public ArrayList<chatot.Task> load() throws Exception {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        ArrayList<chatot.Task> taskList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            boolean isDone = (line.charAt(TASK_DONE_POSITION) == 'X');
            switch (line.charAt(TASK_TYPE_POSITION)) {
                case 'E':
                    int startIndex = line.indexOf("from: ");
                    taskList.add(new Event(line.substring(TASK_DESC_POSITION, startIndex - TASK_TIME_WHITESPACE_LENGTH), line.substring(startIndex - 1), isDone));
                    break;
                case 'D':
                    int timeIndex = line.indexOf("by: ");
                    taskList.add(new Deadline(line.substring(TASK_DESC_POSITION, timeIndex - TASK_TIME_WHITESPACE_LENGTH), line.substring(timeIndex + 1, line.length() - 1), isDone));
                    break;
                case 'T':
                    taskList.add(new Todo(line.substring(TASK_DESC_POSITION), isDone));
                    break;
                default:
                    break;
            }
        }
        scanner.close();
        return taskList;
    }

    /**
     * Saves the task list as a textfile.
     * @param tasks list of existing tasks
     */
    public void save(ArrayList<chatot.Task> tasks) {
        File dataDir = new File("./data");

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (chatot.Task task : tasks) {
                writer.write(task.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}