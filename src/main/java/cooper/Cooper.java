package cooper;

import java.lang.Thread;
import java.util.NoSuchElementException;

import cooper.command.Command;
import cooper.exceptions.EmptyFinancialStatementException;
import cooper.exceptions.InvalidAccessException;
import cooper.exceptions.InvalidCommandFormatException;
import cooper.exceptions.LogoutException;
import cooper.log.CooperLogger;
import cooper.storage.StorageManager;
import cooper.ui.FinanceUi;
import cooper.ui.ParserUi;
import cooper.ui.Ui;
import cooper.exceptions.UnrecognisedCommandException;
import cooper.parser.CommandParser;
import cooper.ui.VerificationUi;
import cooper.verification.SignInDetails;
import cooper.verification.Verifier;
import cooper.resources.ResourcesManager;

public class Cooper {

    private final Verifier cooperVerifier;
    private final ResourcesManager cooperResourcesManager;
    private final CommandEmulator emulator;
    private final StorageManager cooperStorageManager;

    public Cooper() {
        cooperVerifier = new Verifier();
        cooperResourcesManager = new ResourcesManager();
        emulator = new CommandEmulator();
        cooperStorageManager = new StorageManager();
        CooperLogger.setupLogger();
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Cooper cooper = new Cooper();
        cooper.run();
    }


    public void run() {
        setUp();
        runLoopUntilExitCommand();
    }

    private void setUp() {
        Ui.showLogo();
        Ui.showIntroduction();

        // Load data from storage
        cooperStorageManager.loadAllData(cooperVerifier,
                cooperResourcesManager.getFinanceManager(),
                cooperResourcesManager.getMeetingManager(),
                cooperResourcesManager.getForumManager());
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void runLoopUntilExitCommand() {
        while (true) {
            SignInDetails signInDetails = verifyUser();
            runLoopUntilLogoutCommand(signInDetails);
        }
    }

    private SignInDetails verifyUser() {
        SignInDetails successfulSignInDetails = null;
        while (!cooperVerifier.isSuccessfullySignedIn()) {
            String input = emulator.typeNext();
            if (input.length() == 0) {
                input = Ui.getInput();
            }
            successfulSignInDetails = cooperVerifier.verify(input);
        }
        assert successfulSignInDetails != null;
        cooperStorageManager.saveSignInDetails(cooperVerifier);
        return successfulSignInDetails;
    }

    private void runLoopUntilLogoutCommand(SignInDetails signInDetails) {
        while (true) {
            try {
                String input = emulator.typeNext();
                if (input.length() == 0) {
                    input = Ui.getInput();
                }
                Command command = CommandParser.parse(input);
                assert command != null;
                command.execute(signInDetails, cooperResourcesManager, cooperStorageManager);
            } catch (NoSuchElementException | InvalidCommandFormatException e) {
                ParserUi.showInvalidCommandFormatError();
            } catch (NumberFormatException e) {
                ParserUi.showInvalidNumberError();
            } catch (UnrecognisedCommandException e) {
                ParserUi.showUnrecognisedCommandError(false);
            } catch (InvalidAccessException e) {
                VerificationUi.printNoAccessError();
            } catch (EmptyFinancialStatementException e) {
                FinanceUi.showEmptyFinancialStatementException();
            } catch (LogoutException e) {
                cooperVerifier.setSuccessfullySignedIn(false);
                Ui.showLoginRegisterMessage(false);
                break;
            }
        }
    }
}


class CommandEmulator {
    private final String[] commandSequence = {
        // "register shixi pw ******** as admin",
        "login shixi pw ******** as admin",
        "cf",
        "add 500",
        "add 120",
        "add 170",
        "add 325",
        "add 2400",
        "add 1200",
        "add 35",
        "add 1300",
        "add 2987",
        "list",
        "generate cf"
        /*
        "clear", // special instruction for emulator to clear terminal screen
        "post add Who wants dinner today at Utown?!",
        "post add How do I reverse a linked list?",
        "post add How to make my car go fast?",
        "post list all",
        "post comment I want to come!! on 1",
        "post list all"
        */
    };
    private int counter;

    public CommandEmulator() {
        counter = 0;
    }

    public String typeNext() {
        if (counter < 0 || counter >= commandSequence.length) {
            return "";
        }
        try {
            // dummy line to only emulate after "enter"
            Ui.getEnter();
            Ui.showPrompt();
            String input = commandSequence[counter];
            if (input.equals("clear")) {
                // special emulator command
                clearScreen();
                counter++;
                return typeNext();
            } else {
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    System.out.print(c);
                    Thread.sleep(50); 
                }
                System.out.println();
                counter++;
                return input;
            }
        } catch (InterruptedException e) {
            // unforseen interrupt error, return empty string
            return "";
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
