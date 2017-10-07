package pt.tecnico.mydrive.presentation;

import java.util.Arrays;

import pt.tecnico.mydrive.service.ExecuteFileService;

public class ExecuteCommand extends MyDriveCommand {

    private MyDriveShell mdShell;

    public ExecuteCommand(MyDriveShell mdShell) {
	super(mdShell, "do", "execute a specific file");
	this.mdShell = mdShell;
    }

    public void execute(String[] args) {
	// NOT CARING ABOUT ARGS; EVER
	if (args.length == 1) {
	    String path = args[0];
	    String token = mdShell.getLastToken();
	    String filename = mdShell.getLastUser();

	    new ExecuteFileService(token, filename);
	} else {
	    if (args.length > 1) {
		String path = args[0];
		String[] execArgs = Arrays.copyOfRange(args, 1, args.length);
		String token = mdShell.getLastToken();
		String filename = mdShell.getLastUser();
		new ExecuteFileService(token, filename, execArgs);
	    } else
		throw new RuntimeException("USAGE: " + name() + " <path> [<args>]");
	}

    }
}