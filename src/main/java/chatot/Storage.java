package chatot;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Handles text file interaction for basic storage.
 */
public class Storage {
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
    public ArrayList<Task> load() throws Exception {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        ArrayList<Task> taskList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            boolean isDone = (line.charAt(4) == 'X');
            switch (line.charAt(1)) {
                case 'E':
                    int startIndex = line.indexOf("from: ");
                    taskList.add(new Event(line.substring(7, startIndex - 2), line.substring(startIndex - 1), isDone));
                    break;
                case 'D':
                    int timeIndex = line.indexOf("by: ");
                    taskList.add(new Deadline(line.substring(7, timeIndex - 2), line.substring(timeIndex + 1, line.length() - 1), isDone));
                    break;
                case 'T':
                    taskList.add(new Todo(line.substring(7), isDone));
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
    public void save(ArrayList<Task> tasks) {
        File dataDir = new File("./data");

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Task task : tasks) {
                writer.write(task.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}