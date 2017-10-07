package pt.tecnico.mydrive.exception;

public class ImportUserNameException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportUserNameException() {
		super("Error importing username from XML");
	}


}
