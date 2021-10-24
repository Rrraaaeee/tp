package cooper;

import java.lang.Thread;
import java.util.NoSuchElementException;

import cooper.command.Command;
import cooper.exceptions.InvalidAccessException;
import cooper.exceptions.InvalidCommandFormatException;
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

    public void run() {
        setUp();
        SignInDetails signInDetails = verifyUser();
        runLoopUntilExitCommand(signInDetails);
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

    @SuppressWarnings("InfiniteLoopStatement")
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
                Ui.showUnrecognisedCommandError();
            } catch (InvalidAccessException e) {
                Ui.printNoAccessError();
            }
        }
    }
}


class CommandEmulator {
    private final String[] commandSequence = {
        "login me pw 12345 as admin",
        "add 500",
        "add (1200)",
        "list"
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
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                System.out.print(c);
                Thread.sleep(50);
            }
            counter++;
            return input;
        } catch (InterruptedException e) {
            // unforseen interrupt error, return empty string
            return "";
        }
    }
}
