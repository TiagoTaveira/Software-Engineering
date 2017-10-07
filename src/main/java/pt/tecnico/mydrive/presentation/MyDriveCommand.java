package pt.tecnico.mydrive.presentation;

public abstract class MyDriveCommand extends Command {
  public MyDriveCommand(Shell mdShell, String commandName) { super(mdShell, commandName); }
  public MyDriveCommand(Shell mdShell, String commandName, String commandHelp) { super(mdShell, commandName, commandHelp); }
}