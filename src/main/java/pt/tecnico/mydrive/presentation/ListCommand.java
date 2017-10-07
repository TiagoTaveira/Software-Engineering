package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ListDirectoryService;

public class ListCommand extends MyDriveCommand {

	private MyDriveShell mdShell;

	public ListCommand(MyDriveShell mdShell) {
		super(mdShell, "ls", "List a specific directory");
		this.mdShell = mdShell;
	}

	@Override
	void execute(String[] args) {
		if (args.length == 0) {
			String token = mdShell.getLastToken();
			new ListDirectoryService(token);
		} else {
			if (args.length == 1) {
				String path = args[0];
				String token = mdShell.getLastToken();
				new ListDirectoryService(path,token);
				
			} else
				throw new RuntimeException("USAGE: " + name() + " [<path>]");

		}

	}
}