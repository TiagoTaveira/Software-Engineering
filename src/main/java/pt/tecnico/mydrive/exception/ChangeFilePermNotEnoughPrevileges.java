package pt.tecnico.mydrive.exception;

public class ChangeFilePermNotEnoughPrevileges extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ChangeFilePermNotEnoughPrevileges() {
		super("Error changing file permitions. Not enough previleges.");
	}

}
