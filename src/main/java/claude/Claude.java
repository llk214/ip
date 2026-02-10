package claude;

import java.util.Scanner;

/**
 * Main class for the Claude chatbot application.
 * Manages task creation, listing, and marking through a command-line interface.
 */
public class Claude {
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________";

    private static Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    /**
     * Starts the Claude chatbot and processes user commands in a loop.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        printWelcome();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            handleCommand(input);
            input = scanner.nextLine();
        }

        printGoodbye();
        scanner.close();
    }

    /**
     * Prints the welcome message with the Claude ASCII logo.
     */
    private static void printWelcome() {
        String logo = "  ____ _                 _      \n"
                + " / ___| | __ _ _   _  __| | ___ \n"
                + "| |   | |/ _` | | | |/ _` |/ _ \\\n"
                + "| |___| | (_| | |_| | (_| |  __/\n"
                + " \\____|_|\\__,_|\\__,_|\\__,_|\\___|\n";

        System.out.println(LINE);
        System.out.println("Hello from\n" + logo);
        System.out.println("Hello! I'm Claude, your personal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the goodbye message.
     */
    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Routes the user input to the appropriate command handler.
     * Catches and displays any ClaudeException errors.
     *
     * @param input The raw user input string.
     */
    private static void handleCommand(String input) {
        System.out.println(LINE);
        try {
            if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark ")) {
                markTask(input);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input);
            } else if (input.startsWith("todo")) {
                addTodo(input);
            } else if (input.startsWith("deadline")) {
                addDeadline(input);
            } else if (input.startsWith("event")) {
                addEvent(input);
            } else {
                throw new ClaudeException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (ClaudeException e) {
            System.out.println("I'd love to help, but -- " + e.getMessage());
        }
        System.out.println(LINE);
    }

    /**
     * Prints all tasks in the task list.
     */
    private static void listTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks a task as done based on the user input.
     *
     * @param input The user input containing the task number to mark.
     * @throws ClaudeException If the task number is invalid or out of range.
     */
    private static void markTask(String input) throws ClaudeException {
        int taskIndex = parseTaskIndex(input, 5);
        tasks[taskIndex].markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks[taskIndex]);
    }

    /**
     * Marks a task as not done based on the user input.
     *
     * @param input The user input containing the task number to unmark.
     * @throws ClaudeException If the task number is invalid or out of range.
     */
    private static void unmarkTask(String input) throws ClaudeException {
        int taskIndex = parseTaskIndex(input, 7);
        tasks[taskIndex].markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks[taskIndex]);
    }

    /**
     * Parses and validates a task index from user input.
     *
     * @param input The user input containing the task number.
     * @param prefixLength The length of the command prefix to skip.
     * @return The zero-based task index.
     * @throws ClaudeException If the task number is not a valid integer or out of range.
     */
    private static int parseTaskIndex(String input, int prefixLength) throws ClaudeException {
        String indexStr = input.substring(prefixLength).trim();
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new ClaudeException("Please provide a valid task number.");
        }
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new ClaudeException("Task number " + (taskIndex + 1) + " is out of range. "
                    + "You have " + taskCount + " task(s).");
        }
        return taskIndex;
    }

    /**
     * Parses user input and adds a new Todo task.
     *
     * @param input The user input containing the todo description.
     * @throws ClaudeException If the todo description is empty.
     */
    private static void addTodo(String input) throws ClaudeException {
        if (input.trim().equals("todo")) {
            throw new ClaudeException("The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new ClaudeException("The description of a todo cannot be empty.");
        }
        addTask(new Todo(description));
    }

    /**
     * Parses user input and adds a new Deadline task.
     *
     * @param input The user input containing the deadline description and due date.
     * @throws ClaudeException If the description is empty or the /by clause is missing.
     */
    private static void addDeadline(String input) throws ClaudeException {
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
        addTask(new Deadline(description, by));
    }

    /**
     * Parses user input and adds a new Event task.
     *
     * @param input The user input containing the event description, start and end times.
     * @throws ClaudeException If the description is empty or /from and /to clauses are missing.
     */
    private static void addEvent(String input) throws ClaudeException {
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
        addTask(new Event(description, from, to));
    }

    /**
     * Adds a task to the task list and prints a confirmation message.
     *
     * @param task The task to add.
     * @throws ClaudeException If the task list is full.
     */
    private static void addTask(Task task) throws ClaudeException {
        if (taskCount >= MAX_TASKS) {
            throw new ClaudeException("You've reached the Claude free tier limit! "
                    + "Subscribe to Claude Pro to unlock unlimited tasks ;-)");
        }
        tasks[taskCount] = task;
        taskCount++;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}