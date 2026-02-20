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
     * If some lines are corrupted, asks the user whether to recover valid tasks or discard all.
     * If all lines are corrupted, informs the user and starts fresh.
     *
     * @param tasks The array to load tasks into.
     * @return The number of tasks loaded.
     */
    public static int load(Task[] tasks) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 0;
        }

        Task[] tempTasks = new Task[tasks.length];
        int validCount = 0;
        int totalLines = 0;
        int corruptedCount = 0;

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                totalLines++;
                try {
                    tempTasks[validCount] = parseTask(line);
                    validCount++;
                } catch (ClaudeException e) {
                    corruptedCount++;
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            return 0;
        }

        if (totalLines == 0) {
            return 0;
        }

        if (corruptedCount == 0) {
            for (int i = 0; i < validCount; i++) {
                tasks[i] = tempTasks[i];
            }
            return validCount;
        }

        if (corruptedCount == totalLines) {
            System.out.println("The save file is fully corrupted. Starting with an empty task list.");
            return 0;
        }

        System.out.println("Some data in the save file is corrupted (" + corruptedCount
                + " out of " + totalLines + " entries).");
        System.out.println("Would you like to recover the " + validCount + " valid task(s)?");
        System.out.println("Enter 'yes' to recover, or 'no' to start fresh:");

        Scanner inputScanner = new Scanner(System.in);
        String response = inputScanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            for (int i = 0; i < validCount; i++) {
                tasks[i] = tempTasks[i];
            }
            save(tasks, validCount);
            System.out.println("Recovered " + validCount + " task(s).");
            return validCount;
        } else {
            save(tasks, 0);
            System.out.println("Save file cleared. Starting with an empty task list.");
            return 0;
        }
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
