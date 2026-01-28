import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String logo = "  ____ _                 _      \n"
                + " / ___| | __ _ _   _  __| | ___ \n"
                + "| |   | |/ _` | | | |/ _` |/ _ \\\n"
                + "| |___| | (_| | |_| | (_| |  __/\n"
                + " \\____|_|\\__,_|\\__,_|\\__,_|\\___|\n";

        // Array to store tasks (max 100)
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                // Add the task
                tasks[taskCount] = input;
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