# Claude User Guide

Claude is a **command-line task manager chatbot** that helps you keep track of your todos, deadlines, and events.

## Quick Start

1. Ensure you have Java 17 installed.
2. Download the latest `claude.jar` from the [Releases](https://github.com/llk214/ip/releases) page.
3. Run the chatbot:
   ```
   java -jar claude.jar
   ```
4. Type commands and press Enter. Type `bye` to exit.

## Features

### Adding a todo: `todo`

Adds a task with no date attached.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task(s) in the list.
```

### Adding a deadline: `deadline`

Adds a task with a due date. If the date is in `yyyy-mm-dd` format, it will be stored as a proper date and displayed in a friendly format (e.g., `Dec 02 2025`).

Format: `deadline DESCRIPTION /by DATE`

Example: `deadline return book /by 2025-12-02`

```
Got it. I've added this task:
  [D][ ] return book (by: Dec 02 2025)
Now you have 2 task(s) in the list.
```

### Adding an event: `event`

Adds a task with a start and end time.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 task(s) in the list.
```

### Listing all tasks: `list`

Shows all tasks in the list.

Format: `list`

```
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Dec 02 2025)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### Marking a task as done: `mark`

Marks a task as completed.

Format: `mark INDEX`

Example: `mark 1`

```
Nice! I've marked this task as done:
  [T][X] read book
```

### Unmarking a task: `unmark`

Marks a task as not done.

Format: `unmark INDEX`

Example: `unmark 1`

```
OK, I've marked this task as not done yet:
  [T][ ] read book
```

### Deleting a task: `delete`

Removes a task from the list.

Format: `delete INDEX`

Example: `delete 3`

```
Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 task(s) in the list.
```

### Finding tasks by keyword: `find`

Finds tasks whose descriptions contain the given keyword.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Dec 02 2025)
```

### Listing deadlines due on a date/month/range: `due`

Shows deadlines that fall on a specific date, within a month, or within a date range.

Format:
- `due yyyy-mm-dd` — deadlines on a specific date
- `due yyyy-mm` — deadlines in a specific month
- `due yyyy-mm-dd yyyy-mm-dd` — deadlines in a date range

Example: `due 2025-12`

```
Here are the deadlines due from Dec 01 2025 to Dec 31 2025:
1.[D][ ] return book (by: Dec 02 2025)
```

### Exiting the program: `bye`

Exits Claude.

Format: `bye`

## Saving data

Tasks are automatically saved to `data/claude.txt` after every command that modifies the list. There is no need to save manually.

## Command Summary

| Command | Format |
|---------|--------|
| Todo | `todo DESCRIPTION` |
| Deadline | `deadline DESCRIPTION /by DATE` |
| Event | `event DESCRIPTION /from START /to END` |
| List | `list` |
| Mark | `mark INDEX` |
| Unmark | `unmark INDEX` |
| Delete | `delete INDEX` |
| Find | `find KEYWORD` |
| Due | `due DATE` |
| Exit | `bye` |
