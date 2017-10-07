package pt.tecnico.mydrive.exception;

public class ImportUserAlreadyExistsException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportUserAlreadyExistsException() {
		super("Error importing user from XML. The user already exists");
	}


}
