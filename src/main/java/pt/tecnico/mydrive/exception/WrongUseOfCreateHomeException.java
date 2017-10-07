package pt.tecnico.mydrive.exception;

public class WrongUseOfCreateHomeException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public WrongUseOfCreateHomeException() {
		super("Constructor only available for creating home directory.");
	}

}
