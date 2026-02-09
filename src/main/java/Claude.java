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
     *
     * @param input The raw user input string.
     */
    private static void handleCommand(String input) {
        System.out.println(LINE);

        if (input.equals("list")) {
            listTasks();
        } else if (input.startsWith("mark ")) {
            markTask(input);
        } else if (input.startsWith("unmark ")) {
            unmarkTask(input);
        } else if (input.startsWith("todo ")) {
            addTodo(input);
        } else if (input.startsWith("deadline ")) {
            addDeadline(input);
        } else if (input.startsWith("event ")) {
            addEvent(input);
        } else {
            addGenericTask(input);
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
     */
    private static void markTask(String input) {
        int taskIndex = Integer.parseInt(input.substring(5)) - 1;
        tasks[taskIndex].markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks[taskIndex]);
    }

    /**
     * Marks a task as not done based on the user input.
     *
     * @param input The user input containing the task number to unmark.
     */
    private static void unmarkTask(String input) {
        int taskIndex = Integer.parseInt(input.substring(7)) - 1;
        tasks[taskIndex].markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks[taskIndex]);
    }

    /**
     * Parses user input and adds a new Todo task.
     *
     * @param input The user input containing the todo description.
     */
    private static void addTodo(String input) {
        String description = input.substring(5);
        addTask(new Todo(description));
    }

    /**
     * Parses user input and adds a new Deadline task.
     *
     * @param input The user input containing the deadline description and due date.
     */
    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split(" /by ");
        String description = parts[0];
        String by = parts[1];
        addTask(new Deadline(description, by));
    }

    /**
     * Parses user input and adds a new Event task.
     *
     * @param input The user input containing the event description, start and end times.
     */
    private static void addEvent(String input) {
        String[] parts = input.substring(6).split(" /from | /to ");
        String description = parts[0];
        String from = parts[1];
        String to = parts[2];
        addTask(new Event(description, from, to));
    }

    /**
     * Adds an unrecognized input as a generic Todo task.
     *
     * @param input The unrecognized user input to add as a task.
     */
    private static void addGenericTask(String input) {
        tasks[taskCount] = new Todo(input);
        taskCount++;
        System.out.println("added: " + input);
    }

    /**
     * Adds a task to the task list and prints a confirmation message.
     *
     * @param task The task to add.
     */
    private static void addTask(Task task) {
        tasks[taskCount] = task;
        taskCount++;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}