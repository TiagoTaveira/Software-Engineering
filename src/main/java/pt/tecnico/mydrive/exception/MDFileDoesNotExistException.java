package pt.tecnico.mydrive.exception;

public class MDFileDoesNotExistException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public MDFileDoesNotExistException() {
		super("Error mdfile does not exists.");
	}

}
