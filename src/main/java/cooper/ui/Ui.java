package cooper.ui;

import cooper.finance.FinanceManager;
import cooper.verification.UserRole;
import cooper.forum.ForumPost;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;


@SuppressWarnings("checkstyle:LineLength")
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

    private static final String LINE = "=========================================================================";

    private static final String TABLE_TOP = "┌────────────────────────────────────────────────────────────────────┐";
    private static final String TABLE_BOT = "└────────────────────────────────────────────────────────────────────┘";

    private static final String GREETING = "Hello I'm cOOPer! Nice to meet you!";


    private static final Scanner scanner = new Scanner(System.in);
    private static final PrintStream printStream = System.out;

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

    public static void showPleaseRegisterMessage() {
        show(LINE);
        show("Your username does not exist, please register!");
        show(LINE);
    }

    public static void showPleaseLoginMessage() {
        show(LINE);
        show("Your username already exists, please login!");
        show(LINE);
    }

    public static void showRegisteredSuccessfullyMessage(String username, UserRole userRole) {
        String userRoleAsString = (userRole == UserRole.ADMIN) ? "admin" : "employee";
        show(LINE);
        show(username + " is now successfully registered as an " + userRoleAsString + "!");
        show(LINE);
    }

    public static void showLoggedInSuccessfullyMessage(String username) {
        show(LINE);
        show("You are now logged in successfully as " + username + "!");
        show(LINE);
    }

    public static void showIncorrectRoleMessage() {
        show(LINE);
        show("You are logging in with an incorrect role! Please try again.");
        show(LINE);
    }

    public static void showInvalidUserRoleError() {
        show(LINE);
        show("Invalid role entered! Role can only be admin or employee.");
        show(LINE);
    }

    public static void showIncorrectPasswordError() {
        show(LINE);
        show("Incorrect password entered! Please try again.");
        show(LINE);
    }

    public static void showText(String text) {
        show(text);
    }

    public static void showFileWriteError(IOException e) {
        show(LINE);
        show("Error writing to file ", false);
        show(e.getMessage(), true);
        show(LINE);
    }

    /**
     * Exception message to show file path error.
     **/
    public static void showFileCreationError(IOException e) {
        show(LINE);
        show("Error creating storage file: ", false);
        show(e.getMessage(), true);
        show(LINE);
    }

    public static void showInvalidFileDataError() {
        show(LINE);
        show("Invalid file data in storage files!");
        show(LINE);
    }

    /**
     * Exception message to show invalid command error.
     **/
    public static void showUnrecognisedCommandError(boolean isSignIn) {
        show(LINE);
        show("I don't recognise the command you entered.");

        if (isSignIn) {
            show("To login, enter \"login  [yourUsername] pw [password] as [yourRole]\"");
            show("To register, enter \"register [yourUsername] pw [password] as [yourRole]\"");
        } else {
            show("Enter 'help' to view the format of each command.");
        }

        show(LINE);
    }

    /**
     * Exception message to show invalid command argument error.
     **/
    public static void showInvalidCommandFormatError() {
        show(LINE);
        show("The command you entered is of the wrong format!");
        show("Enter 'help' to view the format of each command.");
        show(LINE);
    }

    /**
     * Exception message to show a non-integral value has been input where an integer value
     * is expected.
     **/
    public static void showInvalidNumberError() {
        show(LINE);
        show("Please enter a number for the amount.");
        show(LINE);
    }

    public static void showInvalidScheduleCommandException() {
        show(LINE);
        show("Oops, please enter more than one username!");
        show(LINE);
    }

    public static void showInvalidTimeException() {
        show(LINE);
        show("The time format you entered is not accepted! Please enter again.");
        show(LINE);
    }

    public static void showDuplicateUsernameException() {
        show(LINE);
        show("The username has already been entered under that timeslot.");
        show(LINE);
    }

    public static void showCannotScheduleMeetingException() {
        show(LINE);
        show("Oops, no meeting can be scheduled!");
        show(LINE);
    }

    public static void showDuplicateMeetingException() {
        show(LINE);
        show("You have already scheduled a meeting at that time!");
        show(LINE);
    }

    public static void showBye() {
        show(LINE);
        show("Bye, see you next time! :D");
        show(LINE);
    }

    public static void showPrompt() {
        show(">> ", false); // false: do not print newline
    }

    /**
     * Close streams properly.
     */
    public static void closeStreams() {
        scanner.close();
        printStream.close();
    }

    private static void show(String printMessage) {
        printStream.println(printMessage);
    }

    private static void show(String printMessage, boolean newline) {
        printStream.print(printMessage);

        if (newline) {
            printStream.println();
        }
    }

    public static void printBalanceSheet(ArrayList<Integer> balanceSheet) {
        show(LINE);
        show(FinanceUI.balanceOpening);
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
            show(FinanceUI.accountMistake);
        } else {
            show(FinanceUI.accountCorrect);
        }
        show(LINE);
        LOGGER.info("The balance sheet is generated here");
    }

    public static void initiateCashFlowStatement() {
        show(FinanceUI.initiateCashFlow);
        show(FinanceUI.firstEntryCashFlow);
    }

    public static void printCashFlowStatement(ArrayList<Integer> cashFlowStatement) {
        show(LINE);
        show(FinanceUI.statementDescription);
        show(FinanceUI.headersUI[0]);
        int i;
        for (i = 0; i < cashFlowStatement.size(); i++) {
            switch (i) {
            case FinanceManager.endOfOA:
                show(FinanceUI.cashFlowUI[i] + cashFlowStatement.get(i));
                show(FinanceUI.netAmountsUI[0] + " "
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + FinanceManager.netOA
                        + Ui.ANSI_RESET);
                show(FinanceUI.headersUI[1]);
                break;
            case FinanceManager.endOfIA:
                show(FinanceUI.cashFlowUI[i] + cashFlowStatement.get(i));
                show(FinanceUI.netAmountsUI[1] + " " 
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + FinanceManager.netIA
                        + Ui.ANSI_RESET);
                show(FinanceUI.headersUI[2]);
                break;
            default:
                show(FinanceUI.cashFlowUI[i]
                        + Ui.ANSI_YELLOW + Ui.ANSI_BOLD 
                        + cashFlowStatement.get(i)
                        + Ui.ANSI_RESET);
                break;
            }
        }
        if (i == cashFlowStatement.size()) {
            show(FinanceUI.netAmountsUI[2] + " " + FinanceManager.netFA);
        }
        show(LINE);
    }

    public static void printCashFlowComplete() {
        show(FinanceUI.cashFlowComplete);
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
        show((isInflow ? "+" : "-") + amount + " has been added as " + FinanceUI.cashFlowUI[cashFlowStage]);
        switch (cashFlowStage) {
        case FinanceManager.endOfOA:
            show(FinanceUI.netAmountsUI[0] + " " + FinanceManager.netOA);
            break;
        case FinanceManager.endOfIA:
            show(FinanceUI.netAmountsUI[1] + " " + FinanceManager.netIA);
            break;
        case FinanceManager.endOfFA:
            show(FinanceUI.netAmountsUI[2] + " " + FinanceManager.netFA);
            break;
        default:
            show("\n" + "next, please enter " + FinanceUI.cashFlowUI[cashFlowStage + 1]);
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
        show("add       | add [amount]");
        show("list      | list");
        show("schedule  | schedule [username1], [username2] at [meetingTime]");
    }

    public static void printEmployeeHelp() {
        show(LINE);
        show("Here are the commands available to an employee along with their formats:");
    }

    public static void printGeneralHelp() {
        show("post add      | post add [postContent]");
        show("post delete   | post delete [postId]");
        show("post comment  | post comment [commentContent] on [postId]");
        show("post list all | post list all/[postId]");
        show("available     | available [availableTime]");
        show("availability  | availability");
        show("meetings      | meetings");
        show(LINE);
    }

    public static void printNoAccessError() {
        show("You do not have access to this command.");
    }

    public static void printInvalidForumPostIndexError() {
        show("The forum index you just keyed in is outside the valid range.");
    }

    public static void printInvalidForumDeleteByNonOwnerError() {
        show("You cannot delete a forum post that is not owned by you!.");
    }
}
