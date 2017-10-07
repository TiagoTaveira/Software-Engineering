package pt.tecnico.mydrive.presentation;

import java.util.Set;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.service.AddVariableService;

public class EnvCommand extends MyDriveCommand {

    private MyDriveShell mdShell;

    public EnvCommand(MyDriveShell mdShell) {
	super(mdShell, "env", "lists or creates environment variables");
	this.mdShell = mdShell;
    }

    public void execute(String[] args) {
	String token = mdShell.getLastToken();
	if (args.length == 0) {
	    AddVariableService service = new AddVariableService(token, "rng", "619");
	    Set<EnvVar> list = service.getSession(token).getEnvVarSet();
	    for (EnvVar envVar : list) {
		System.out.println(envVar.getName() + "=" + envVar.getValue());
	    }
	} else {
	    if (args.length == 1) {
		AddVariableService service = new AddVariableService(token, args[0], "619");
		Set<EnvVar> list = service.getSession(token).getEnvVarSet();
		for (EnvVar envVar : list) {
		    if (envVar.getName().equals(args[0]))
			System.out.println(envVar.getName() + "=" + envVar.getValue());
		}
	    } else {
		if (args.length == 2) {
		    AddVariableService service = new AddVariableService(token, args[0], args[1]);
		    service.execute();
		} else
		    throw new RuntimeException("USAGE: " + name() + " [<name> [<value>]]");
	    }
	}

    }

}
