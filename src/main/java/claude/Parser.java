package claude;

/**
 * Deals with making sense of the user command.
 * Parses user input and executes the corresponding operation on the task list.
 */
public class Parser {

    /**
     * Parses and executes the user command.
     *
     * @param input The raw user input string.
     * @param tasks The task list to operate on.
     * @param ui The UI to display results.
     * @param storage The storage to save changes.
     * @throws ClaudeException If the command is invalid or has invalid arguments.
     */
    public static void parseAndExecute(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        if (input.equals("list")) {
            ui.showTaskList(tasks);
        } else if (input.startsWith("mark ")) {
            executeMark(input, tasks, ui, storage);
        } else if (input.startsWith("unmark ")) {
            executeUnmark(input, tasks, ui, storage);
        } else if (input.startsWith("todo")) {
            executeTodo(input, tasks, ui, storage);
        } else if (input.startsWith("deadline")) {
            executeDeadline(input, tasks, ui, storage);
        } else if (input.startsWith("event")) {
            executeEvent(input, tasks, ui, storage);
        } else if (input.startsWith("delete ")) {
            executeDelete(input, tasks, ui, storage);
        } else {
            throw new ClaudeException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static int parseTaskIndex(String input, int prefixLength,
            TaskList tasks) throws ClaudeException {
        String indexStr = input.substring(prefixLength).trim();
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new ClaudeException("Please provide a valid task number.");
        }
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new ClaudeException("Task number " + (taskIndex + 1) + " is out of range. "
                    + "You have " + tasks.size() + " task(s).");
        }
        return taskIndex;
    }

    private static void executeMark(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        int taskIndex = parseTaskIndex(input, 5, tasks);
        tasks.get(taskIndex).markAsDone();
        storage.save(tasks);
        ui.showTaskMarked(tasks.get(taskIndex));
    }

    private static void executeUnmark(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        int taskIndex = parseTaskIndex(input, 7, tasks);
        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks);
        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    private static void executeTodo(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        if (input.trim().equals("todo")) {
            throw new ClaudeException("The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new ClaudeException("The description of a todo cannot be empty.");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void executeDeadline(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        if (input.trim().equals("deadline")) {
            throw new ClaudeException("The description of a deadline cannot be empty.");
        }
        String content = input.substring(9).trim();
        if (content.isEmpty()) {
            throw new ClaudeException("The description of a deadline cannot be empty.");
        }
        if (!content.contains(" /by ")) {
            throw new ClaudeException("A deadline needs a /by clause. "
                    + "Usage: deadline <description> /by <date>");
        }
        String[] parts = content.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty()) {
            throw new ClaudeException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new ClaudeException("The /by date of a deadline cannot be empty.");
        }
        Task task = new Deadline(description, by);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void executeEvent(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        if (input.trim().equals("event")) {
            throw new ClaudeException("The description of an event cannot be empty.");
        }
        String content = input.substring(6).trim();
        if (content.isEmpty()) {
            throw new ClaudeException("The description of an event cannot be empty.");
        }
        if (!content.contains(" /from ")) {
            throw new ClaudeException("An event needs a /from clause. "
                    + "Usage: event <description> /from <start> /to <end>");
        }
        if (!content.contains(" /to ")) {
            throw new ClaudeException("An event needs a /to clause. "
                    + "Usage: event <description> /from <start> /to <end>");
        }
        String[] parts = content.split(" /from | /to ", 3);
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        if (description.isEmpty()) {
            throw new ClaudeException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new ClaudeException("The /from time of an event cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new ClaudeException("The /to time of an event cannot be empty.");
        }
        Task task = new Event(description, from, to);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void executeDelete(String input, TaskList tasks, Ui ui,
            Storage storage) throws ClaudeException {
        int taskIndex = parseTaskIndex(input, 7, tasks);
        Task removed = tasks.delete(taskIndex);
        storage.save(tasks);
        ui.showTaskDeleted(removed, tasks.size());
    }
}
