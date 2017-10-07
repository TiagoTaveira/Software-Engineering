package pt.tecnico.mydrive.exception;

public class CreateFileCreatorDoesNotExistException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public CreateFileCreatorDoesNotExistException() {
		super("Error file creator does not exists.");
	}

}
