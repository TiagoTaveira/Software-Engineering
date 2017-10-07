package pt.tecnico.mydrive.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Command {
    protected static final Logger log = LogManager.getRootLogger();
    private String commandName, commandHelp;
    private Shell mdShell;

    public Command(Shell mdShell, String commandName) {
	this(mdShell, commandName, "<no help>");
    }

    public Command(Shell mdShell, String commandName, String commandHelp) {
	this.commandName = commandName;
	this.commandHelp = commandHelp;
	(this.mdShell = mdShell).add(this);
    }

    /* package */
    void help(String commandHelp) {
	this.commandHelp = commandHelp;
    }

    public String name() {
	return commandName;
    }

    public String help() {
	return commandHelp;
    }

    public Shell mdShell() {
	return mdShell;
    }

    abstract void execute(String[] args);

    public void print(String string) {
	mdShell.print(string);
    }

    public void println(String string) {
	mdShell.println(string);
    }

    public void flush() {
	mdShell.flush();
    }
}