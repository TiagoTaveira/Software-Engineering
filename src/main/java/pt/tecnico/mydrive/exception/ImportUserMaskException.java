package pt.tecnico.mydrive.exception;

public class ImportUserMaskException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportUserMaskException() {
		super("Error importing user mask from XML");
	}

}
