package pt.tecnico.mydrive.presentation;

import java.util.Arrays;

import pt.tecnico.mydrive.service.WriteFileService;

public class WriteCommand extends MyDriveCommand {

    private MyDriveShell mdShell;

    public WriteCommand(MyDriveShell mdShell) {
	super(mdShell, "update", "writes on a specific file");
	this.mdShell = mdShell;
    }

    @Override
    void execute(String[] args) {
	if (args.length > 1) {
	    String path = args[0];
	    String[] text = Arrays.copyOfRange(args, 1, args.length);
	    String content = "";
	    for (String string : text) {
		content = content + string + " ";
	    }
	    String token = mdShell.getLastToken();
	    String filename = mdShell.getLastUser()+"file";
	    new WriteFileService(token, filename, content);
	} else
	    throw new RuntimeException("USAGE: " + name() + " <path> <text>");

    }

}
