package pt.tecnico.mydrive.exception;

public class ImportUserPasswordException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportUserPasswordException() {
		super("Error importing user password from XML");
	}

}
