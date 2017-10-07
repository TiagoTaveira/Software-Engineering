package pt.tecnico.mydrive.exception;

public class ImportFilePermUsersException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportFilePermUsersException() {
		super("Error importing file permitions for user from XML");
	}


}
