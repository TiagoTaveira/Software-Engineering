package pt.tecnico.mydrive.exception;

public class CreateFileInvalidFilenameException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public CreateFileInvalidFilenameException(String filename) {
	super("Error creating file. '" + filename + "' is invalid.");
	}

}
