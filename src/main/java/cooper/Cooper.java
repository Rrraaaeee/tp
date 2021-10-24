package cooper;

import java.lang.Thread;
import java.util.NoSuchElementException;

import cooper.command.Command;
import cooper.exceptions.InvalidAccessException;
import cooper.exceptions.InvalidCommandFormatException;
import cooper.exceptions.LogoutException;
import cooper.log.CooperLogger;
import cooper.storage.StorageManager;
import cooper.ui.Ui;
import cooper.exceptions.UnrecognisedCommandException;
import cooper.parser.CommandParser;
import cooper.verification.SignInDetails;
import cooper.verification.Verifier;
import cooper.resources.ResourcesManager;

public class Cooper {

    private final ResourcesManager cooperResourcesManager;
    private final CommandEmulator emulator;

    public Cooper() {
        cooperResourcesManager = new ResourcesManager();
        emulator = new CommandEmulator();
        CooperLogger.setupLogger();
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        Cooper cooper = new Cooper();
        cooper.run();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        setUp();
        while (true) {
            SignInDetails signInDetails = verifyUser();
            runLoopUntilExitCommand(signInDetails);
        }
    }

    private void setUp() {
        Ui.showLogo();
        Ui.showIntroduction();
    }

    private SignInDetails verifyUser() {
        Verifier cooperVerifier = cooperResourcesManager.getVerifier();
        StorageManager cooperStorageManager = cooperResourcesManager.getStorageManager();
        assert cooperVerifier != null;
        assert cooperStorageManager != null;
        SignInDetails successfulSignInDetails = null;
        String input;
        while (!cooperVerifier.isSuccessfullySignedIn()) {
            input = emulator.typeNext();
            if (input.length() == 0) {
                input = Ui.getInput();
            }
            successfulSignInDetails = cooperVerifier.verify(input);
        }
        assert successfulSignInDetails != null;
        cooperStorageManager.saveSignInDetails(cooperVerifier);
        return successfulSignInDetails;
    }

    private void runLoopUntilExitCommand(SignInDetails signInDetails) {
        while (true) {
            try {
                String input = emulator.typeNext();
                if (input.length() == 0) {
                    input = Ui.getInput();
                }
                Command command = CommandParser.parse(input);
                assert command != null;
                command.execute(signInDetails, cooperResourcesManager);
            } catch (NoSuchElementException | InvalidCommandFormatException e) {
                Ui.showInvalidCommandFormatError();
            } catch (NumberFormatException e) {
                Ui.showInvalidNumberError();
            } catch (UnrecognisedCommandException e) {
                Ui.showUnrecognisedCommandError(false);
            } catch (InvalidAccessException e) {
                Ui.printNoAccessError();
            } catch (LogoutException e) {
                cooperResourcesManager.getVerifier().setSuccessfullySignedIn(false);
                Ui.showLoginRegisterMessage(false);
                break;
            }
        }
    }
}


class CommandEmulator {
    private final String[] commandSequence = {
        "login me pw 12345 as admin",
        "add 500",
        "add (1200)",
        "list",
        "clear", // special instruction for emulator to clear terminal screen
        "post add Who wants dinner today at Utown?!",
        "post add How do I reverse a linked list?",
        "post add How to make my car go fast?",
        "post list all",
        "post comment I want to come!! on 1",
        "post list all"
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
            Ui.getInput(); // dummy line to only emulate after "enter"
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
