package claude;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file on the hard disk.
 */
public class Storage {
    private static final String FILE_PATH = "data" + File.separator + "claude.txt";

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks The list of tasks to save.
     */
    public static void save(ArrayList<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file into the given list.
     * If some lines are corrupted, asks the user whether to recover valid tasks or discard all.
     * If all lines are corrupted, informs the user and starts fresh.
     *
     * @param tasks The list to load tasks into.
     */
    public static void load(ArrayList<Task> tasks) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        ArrayList<Task> validTasks = new ArrayList<>();
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
                    validTasks.add(parseTask(line));
                } catch (ClaudeException e) {
                    corruptedCount++;
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            return;
        }

        if (totalLines == 0) {
            return;
        }

        if (corruptedCount == 0) {
            tasks.addAll(validTasks);
            return;
        }

        if (corruptedCount == totalLines) {
            System.out.println("The save file is fully corrupted. Starting with an empty task list.");
            return;
        }

        System.out.println("Some data in the save file is corrupted (" + corruptedCount
                + " out of " + totalLines + " entries).");
        System.out.println("Would you like to recover the " + validTasks.size() + " valid task(s)?");
        System.out.println("Enter 'yes' to recover, or 'no' to start fresh:");

        Scanner inputScanner = new Scanner(System.in);
        String response = inputScanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            tasks.addAll(validTasks);
            save(tasks);
            System.out.println("Recovered " + validTasks.size() + " task(s).");
        } else {
            save(tasks);
            System.out.println("Save file cleared. Starting with an empty task list.");
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
