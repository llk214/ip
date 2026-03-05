package claude;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

/**
 * Deals with interactions with the user, including reading input and printing output.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Creates a new Ui and initializes the input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the welcome message with the Claude ASCII logo.
     */
    public void showWelcome() {
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
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Prints the divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints an error message with a friendly prefix.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println("I'd love to help, but -- " + message);
    }

    /**
     * Prints a loading error message when the save file cannot be loaded.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks. Starting with an empty task list.");
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the task list.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.size() == 0) {
            System.out.println("It seems you do not have any tasks yet.");
        } else if (tasks.size() == 1) {
            System.out.println("Here is the task in your list:");
            System.out.println("1." + tasks.get(0));
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
    }

    /**
     * Prints a message confirming a task has been added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " "
                + (totalTasks == 1 ? "task" : "tasks") + " in the list.");
    }

    /**
     * Prints a message confirming a task has been deleted.
     *
     * @param task The task that was removed.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        if (totalTasks == 0) {
            System.out.println("You have no tasks in the list now.");
        } else {
            System.out.println("Now you have " + totalTasks + " "
                    + (totalTasks == 1 ? "task" : "tasks") + " in the list.");
        }
    }

    /**
     * Prints a message confirming a task has been marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Prints a message confirming a task has been unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Prints deadlines due within a date range.
     *
     * @param matching The task list of matching deadlines.
     * @param start The start date of the range.
     * @param end The end date of the range.
     */
    public void showDueList(TaskList matching, LocalDate start, LocalDate end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
        String label = matching.size() == 1 ? "Deadline" : "Deadlines";
        if (start.equals(end)) {
            System.out.println(label + " due on "
                    + start.format(formatter).replace("Sep ", "Sept ") + ":");
        } else {
            System.out.println(label + " due from "
                    + start.format(formatter).replace("Sep ", "Sept ")
                    + " to " + end.format(formatter).replace("Sep ", "Sept ") + ":");
        }
        if (matching.size() == 0) {
            System.out.println("No matching deadlines found.");
        } else {
            for (int i = 0; i < matching.size(); i++) {
                System.out.println((i + 1) + "." + matching.get(i));
            }
        }
    }

    /**
     * Prints the results of a find command.
     *
     * @param matching The task list of matching tasks.
     */
    public void showFindResults(TaskList matching) {
        if (matching.size() == 0) {
            System.out.println("No matching tasks found.");
        } else if (matching.size() == 1) {
            System.out.println("Here is the matching task in your list:");
            System.out.println("1." + matching.get(0));
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matching.size(); i++) {
                System.out.println((i + 1) + "." + matching.get(i));
            }
        }
    }

    /**
     * Prints a message to the user.
     *
     * @param message The message to print.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }
}
