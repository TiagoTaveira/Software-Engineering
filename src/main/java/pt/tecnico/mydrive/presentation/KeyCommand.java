package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.exception.UserNotInSessionException;

public class KeyCommand extends MyDriveCommand {

    private MyDriveShell mdShell;

    public KeyCommand(MyDriveShell mdShell) {
	super(mdShell, "token", "allows for multiple active sessions");
	this.mdShell = mdShell;
    }

    public void execute(String[] args) {
	if (args.length == 0)
	    System.out.println("token: " + mdShell.getLastToken() + " | user: " + mdShell.getLastUser());
	else {
	    if (args.length == 1) {
		String sessionToken = mdShell.getSession(args[0]);
		if (sessionToken != null) {
		    mdShell.setLastUser(args[0]);
		    mdShell.setLastToken(sessionToken);
		    System.out.println("token: " + mdShell.getLastToken());
		}
		throw new UserNotInSessionException(args[0]);
	    } else
		throw new RuntimeException("USAGE: " + name() + " [<username>]");
	}

    }
}