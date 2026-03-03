package claude;

/**
 * Main class for the Claude chatbot application.
 * Manages task creation, listing, and marking through a command-line interface.
 */
public class Claude {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a new Claude chatbot with the given file path for storage.
     *
     * @param filePath The path to the data file.
     */
    public Claude(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ClaudeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main command loop of the chatbot.
     */
    public void run() {
        ui.showWelcome();
        String fullCommand = ui.readCommand();
        while (!fullCommand.equals("bye")) {
            ui.showLine();
            try {
                Parser.parseAndExecute(fullCommand, tasks, ui, storage);
            } catch (ClaudeException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
            fullCommand = ui.readCommand();
        }
        ui.showGoodbye();
    }

    /**
     * Starts the Claude chatbot.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Claude("data/claude.txt").run();
    }
}
