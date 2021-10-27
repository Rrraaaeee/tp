package cooper.ui;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Logger;

public class Ui {

    public static final String ANSI_BOLD = "\033[1m";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final String LOGO = ANSI_YELLOW + "            /$$$$$$   /$$$$$$  /$$$$$$$\n"
            +                          "           /$$__  $$ /$$__  $$| $$__  $$\n"
            +                          "  /$$$$$$$| $$  \\ $$| $$  \\ $$| $$  \\ $$ /$$$$$$   /$$$$$$\n"
            +                          " /$$_____/| $$  | $$| $$  | $$| $$$$$$$//$$__  $$ /$$__  $$\n"
            +                          "| $$      | $$  | $$| $$  | $$| $$____/| $$$$$$$$| $$  \\__/\n"
            +                          "| $$      | $$  | $$| $$  | $$| $$     | $$_____/| $$\n"
            +                          "|  $$$$$$$|  $$$$$$/|  $$$$$$/| $$     |  $$$$$$$| $$\n"
            +                          " \\_______/ \\______/  \\______/ |__/      \\_______/|__/"
            + ANSI_RESET;

    protected static final String LINE = "=========================================================================";

    protected static final String TABLE_TOP = "┌────────────────────────────────────────────────────────────────────┐";
    protected static final String TABLE_BOT = "└────────────────────────────────────────────────────────────────────┘";

    private static final String GREETING = "Hello I'm cOOPer! Nice to meet you!";


    private static final Scanner scanner = new Scanner(System.in);
    private static final PrintStream printStream = System.out;

    protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static String getEnter() {
        return scanner.nextLine();
    }

    public static String getInput() {
        showPrompt();
        return scanner.nextLine();
    }

    public static void showLogo() {
        show(LOGO);
    }

    public static void showIntroduction() {
        showGreetingMessage();
        showLoginRegisterMessage(true);
    }

    private static void showGreetingMessage() {
        show(LINE);
        show(GREETING);
        show(LINE);
    }

    public static void showLoginRegisterMessage(boolean isIntro) {
        if (isIntro) {
            show("Login or register to gain access to my features!");
        } else {
            show(LINE);
        }
        show("To login, enter \"login  [yourUsername] pw [password] as [yourRole]\"");
        show("To register, enter \"register [yourUsername] pw [password] as [yourRole]\"");
        show(LINE);
    }

    public static void showBye() {
        show(LINE);
        show("Bye, see you next time!");
        show(LINE);
    }

    private static void showPrompt() {
        show(">> ", false); // false: do not print newline
    }

    /**
     * Close streams properly.
     */
    public static void closeStreams() {
        scanner.close();
        printStream.close();
    }

    protected static void show(String printMessage) {
        printStream.println(printMessage);
    }

    protected static void show(String printMessage, boolean newline) {
        printStream.print(printMessage);

        if (newline) {
            printStream.println();
        }
    }

    public static void printBalanceSheet(ArrayList<Integer> balanceSheet) {
        show(LINE);
        show(FinanceUi.balanceOpening);
        int balance = 0;
        for (int i = 0; i < balanceSheet.size(); i++) {
            if (balanceSheet.get(i) >= 0) {
                show(i + 1 + ". inflow of: " + balanceSheet.get(i));
            } else {
                show(i + 1 + ". outflow of: " + balanceSheet.get(i));
            }
            balance += balanceSheet.get(i);
        }
        show("\n" + "Current balance: " + balance);
        if (balance != 0) {
            show(FinanceUi.accountMistake);
        } else {
            show(FinanceUi.accountCorrect);
        }
        show(LINE);
        LOGGER.info("The balance sheet is generated here");
    }

    public static void initiateCashFlowStatement() {
        show(FinanceUi.initiateCashFlow);
        show(FinanceUi.firstEntryCashFlow);
    }

    public static void printCashFlowStatement(ArrayList<Integer> cashFlowStatement) {
        show(LINE);
        show(FinanceUi.statementDescription);
        show(FinanceUi.headersUI[0]);
        int i;
        for (i = 0; i < cashFlowStatement.size(); i++) {
            switch (i) {
            case FinanceManager.endOfOA:
                show(FinanceUi.cashFlowUI[i] + cashFlowStatement.get(i));
                show(FinanceUi.netAmountsUI[0] + " "
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + FinanceManager.netOA
                        + Ui.ANSI_RESET);
                show(FinanceUi.headersUI[1]);
                break;
            case FinanceManager.endOfIA:
                show(FinanceUi.cashFlowUI[i] + cashFlowStatement.get(i));
                show(FinanceUi.netAmountsUI[1] + " " 
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + FinanceManager.netIA
                        + Ui.ANSI_RESET);
                show(FinanceUi.headersUI[2]);
                break;
            default:
                show(FinanceUi.cashFlowUI[i]
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + cashFlowStatement.get(i)
                        + Ui.ANSI_RESET);
                break;
            }
        }
        if (i == cashFlowStatement.size()) {
            show(FinanceUi.netAmountsUI[2] + " " + FinanceManager.netFA);
        }
        show(LINE);
    }

    public static void printCashFlowComplete() {
        show(FinanceUi.cashFlowComplete);
    }

    public static void printAddBalanceCommand(int amount, boolean isInflow) {
        show(LINE);
        show("Success!");
        show("Amount: " + (isInflow ? "+" : "-") + amount + " has been added to the Balance Sheet.");
        show(LINE);
    }

    public static void printAddCashFlowCommand(int amount, boolean isInflow, int cashFlowStage) {
        show(LINE);
        show("Success!");
        show((isInflow ? "+" : "-") + amount + " has been added as " + FinanceUi.cashFlowUI[cashFlowStage]);
        switch (cashFlowStage) {
        case FinanceManager.endOfOA:
            show(FinanceUi.netAmountsUI[0] + " " + FinanceManager.netOA);
            break;
        case FinanceManager.endOfIA:
            show(FinanceUi.netAmountsUI[1] + " " + FinanceManager.netIA);
            break;
        case FinanceManager.endOfFA:
            show(FinanceUi.netAmountsUI[2] + " " + FinanceManager.netFA);
            break;
        default:
            show("\n" + "next, please enter " + FinanceUi.cashFlowUI[cashFlowStage + 1]);
            break;
        }

        if (cashFlowStage == 8) {
            printCashFlowComplete();
        }
        show(LINE);
    }

    public static void printAvailableCommand(String time, String username) {
        show(LINE);
        show("Success!");
        show(username + "'s availability has been added to " + time);
        show(LINE);
    }

    public static void printSuccessfulScheduleCommand(String meetingName, String time, ArrayList<String> usernames) {
        show(LINE);
        show("Success!");
        show("You have scheduled a <<" + meetingName + ">> meeting at " + time + " with "
                + listOfAvailabilities(usernames));
        show(LINE);
    }

    public static void printAvailabilities(TreeMap<LocalTime, ArrayList<String>> availability) {
        printAvailabilityTableHeader();
        for (LocalTime timing: availability.keySet()) {
            Ui.showText("│ " + timing + " │ " + listOfAvailabilities(availability.get(timing)));
        }
        show(TABLE_BOT);
        show(LINE);
    }

    public static void printForumPosts(ArrayList<ForumPost> forumPosts) {
        show(LINE);
        show("Here is the list of forum posts:");
        show(TABLE_TOP);
        int cntPost = 1;
        for (var post : forumPosts) {
            show("|  " + cntPost + ". " + post.toString());
            int cntComment = 1;
            for (var comment : post.getComments()) {
                show("|    ∟  " + cntComment + ". " + comment.toString());
                cntComment++;
            }
            cntPost++;
        }
        show(TABLE_BOT);
        show(LINE);
    }

    public static void printForumPost(ArrayList<ForumPost> forumPosts, int postId) {
        show(LINE);
        show("Here is the forum post:");
        show(TABLE_TOP);
        show("|  " + forumPosts.get(postId).toString());

        int cntComment = 1;
        for (var comment : forumPosts.get(postId).getComments()) {
            show("|    ∟  " + cntComment + "." + comment.toString());
            cntComment++;
        }

        show(TABLE_BOT);
        show(LINE);
    }

    public static String listOfAvailabilities(ArrayList<String> availabilities) {
        StringBuilder listOfAvailabilities = new StringBuilder();
        for (String a : availabilities) {
            /* don't need comma for last attendee */
            int indexOfLastAttendee = availabilities.size() - 1;
            if (a.equals(availabilities.get(indexOfLastAttendee))) {
                listOfAvailabilities.append(a);
            } else {
                listOfAvailabilities.append(a).append(", ");
            }
        }
        return String.valueOf(listOfAvailabilities);
    }

    public static void printAvailabilityTableHeader() {
        show(LINE);
        show("These are the availabilities:");
        show(TABLE_TOP);
    }

    public static void printNewPostCommand(String username, String content) {
        show(LINE);
        show(username + " has just posted to forum:");
        show(TABLE_TOP);
        show("|  " + content);
        show(TABLE_BOT);
        show(LINE);
    }

    public static void printDeletePostCommand(String username, String content) {
        show(LINE);
        show(username + " has just deleted a  post from forum:");
        show(TABLE_TOP);
        show("|  " + content);
        show(TABLE_BOT);
        show(LINE);
    }

    public static void printCommentPostCommand(String username, String content, String comment) {
        show(LINE);
        show(username + " has just commented on a  post from forum:");
        show(TABLE_TOP);
        show("|  " + content);
        show("|    ∟  " + comment);
        show(TABLE_BOT);
        show(LINE);
    }

    public static void printAdminHelp() {
        show(LINE);
        show("Here are the commands available to an admin along with their formats:");
        show("bs            | bs");
        show("cf            | cf");
        show("add           | add [amount]");
        show("list          | list");
        show("generate      | generate [financialStatement]");
        show("schedule      | schedule [meetingName] with [username1], [username2] /at [meetingTime]");
    }

    public static void printEmployeeHelp() {
        show(LINE);
        show("Here are the commands available to an employee along with their formats:");
    }

    public static void printGeneralHelp() {
        show("post add      | post add [postContent]");
        show("post delete   | post delete [postId]");
        show("post comment  | post comment [commentContent] on [postId]");
        show("post list all | post list all / post list [postId]");
        show("available     | available [availableTime]");
        show("availability  | availability");
        show("meetings      | meetings");
        show("logout        | logout");
        show("exit          | exit");
        show(LINE);
    }
}
