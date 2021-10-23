package cooper.command;

import cooper.exceptions.InvalidAccessException;
import cooper.storage.StorageManager;
import cooper.verification.SignInDetails;
import cooper.resources.ResourcesManager;

public abstract class Command {

    /**
     * Child classes are required to implement how to execute on itself.
     */
    public abstract void execute(SignInDetails signInDetails, 
            ResourcesManager resourcesManager, StorageManager storageManager)
                                 throws InvalidAccessException;
}
