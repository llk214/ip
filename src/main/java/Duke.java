import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String logo = "  ____ _                 _      \n"
                + " / ___| | __ _ _   _  __| | ___ \n"
                + "| |   | |/ _` | | | |/ _` |/ _ \\\n"
                + "| |___| | (_| | |_| | (_| |  __/\n"
                + " \\____|_|\\__,_|\\__,_|\\__,_|\\___|\n";

        // Array to store Task objects (max 100)
        Task[] tasks = new Task[100];
        int taskCount = 0;

        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println("Hello from\n" + logo);
        System.out.println("Hello! I'm Claude, your personal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            System.out.println(line);

            if (input.equals("list")) {
                // Display all tasks
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                // Mark task as done
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                // Mark task as not done
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else {
                // Add the task
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }

            System.out.println(line);
            input = scanner.nextLine();
        }

        System.out.println(line);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);
        scanner.close();
    }
}