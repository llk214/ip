package claude;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file on the hard disk.
 */
public class Storage {
    private static final String FILE_PATH = "data" + File.separator + "claude.txt";

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks The array of tasks to save.
     * @param taskCount The number of tasks in the array.
     */
    public static void save(Task[] tasks, int taskCount) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < taskCount; i++) {
                writer.write(tasks[i].toFileString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file into the given array.
     *
     * @param tasks The array to load tasks into.
     * @return The number of tasks loaded.
     */
    public static int load(Task[] tasks) {
        int count = 0;
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 0;
        }
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    tasks[count] = parseTask(line);
                    count++;
                } catch (ClaudeException e) {
                    System.out.println("Skipping corrupted line: " + line);
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return count;
    }

    /**
     * Parses a single line from the data file into a Task object.
     *
     * @param line The line to parse.
     * @return The parsed Task.
     * @throws ClaudeException If the line format is invalid.
     */
    private static Task parseTask(String line) throws ClaudeException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new ClaudeException("Invalid format");
        }
        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();
        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new ClaudeException("Invalid deadline format");
            }
            task = new Deadline(description, parts[3].trim());
            break;
        case "E":
            if (parts.length < 4) {
                throw new ClaudeException("Invalid event format");
            }
            String[] times = parts[3].trim().split("-", 2);
            if (times.length < 2) {
                throw new ClaudeException("Invalid event time format");
            }
            task = new Event(description, times[0].trim(), times[1].trim());
            break;
        default:
            throw new ClaudeException("Unknown task type: " + type);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
