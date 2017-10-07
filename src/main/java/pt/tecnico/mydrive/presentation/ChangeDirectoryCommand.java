package pt.tecnico.mydrive.presentation;

import java.util.Arrays;

import pt.tecnico.mydrive.service.ChangeDirectoryService;

public class ChangeDirectoryCommand extends MyDriveCommand {

    private MyDriveShell mdShell;

    public ChangeDirectoryCommand(MyDriveShell mdShell) {
	super(mdShell, "cwd", "change working directory");
	this.mdShell = mdShell;
    }

    @Override
    void execute(String[] args) {
	if (args.length == 1) {
	    String path = args[0];
	    String token = mdShell.getLastToken();

	    new ChangeDirectoryService(token, path);
	    System.out.println(path);
	} else
	    throw new RuntimeException("USAGE: " + name() + " <path>");

    }

}
