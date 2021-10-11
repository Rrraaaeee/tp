package cooper.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;

import cooper.meetings.MeetingManager;
import cooper.ui.Ui;
import cooper.finance.FinanceManager;
import cooper.verification.SignInDetails;


/**
 * The child class of Command that handles the 'Compile' command specifically.
 */
public class CompileCommand extends Command {

    public CompileCommand() {
        super();
    }

    /**
     * The override function for executing the 'compile' command, calls for 'compile' 
     * and subsequently printing the status
     * to the command line.
     */
    @Override
    public void execute(SignInDetails signInDetails, FinanceManager financeManager, MeetingManager meetingManager) {
        financeManager.createInvoice();
        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "/output.pdf"));
        } catch (IOException e) {
            Ui.showText("No output file detected!");
        } catch (IllegalArgumentException e) {
            Ui.showText("No output file detected!");
        }
    }
}


