package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LoginUserService;

public class LoginUserCommand extends MyDriveCommand {

    private static final String GUEST_USERNAME = "nobody";
    private MyDriveShell mdShell;

    public LoginUserCommand(MyDriveShell mdShell) {
	super(mdShell, "login", "log user to system");
	this.mdShell = mdShell;
    }

    public void execute(String[] args) {
	if (args.length == 1) {
	    LoginUserService service = new LoginUserService(args[0], "");
	    service.execute();
	    String token = service.result();
	    mdShell.setLastToken(token);
	    mdShell.setLastUser(service.getUsername());
	} else {
	    if (args.length == 2) {
		LoginUserService service = new LoginUserService(args[0], args[1]);
		service.execute();
		String token = service.result();
		mdShell.addSession(service.getUsername(), token);
		mdShell.setLastToken(token);
		mdShell.setLastUser(service.getUsername());
		if (!args[0].equals(GUEST_USERNAME))
		    mdShell.removeGuest();
	    } else
		throw new RuntimeException("USAGE: " + name() + " <username> <password>");
	}

    }
}