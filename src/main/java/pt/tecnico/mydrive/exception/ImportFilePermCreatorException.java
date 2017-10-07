package pt.tecnico.mydrive.exception;

public class ImportFilePermCreatorException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportFilePermCreatorException() {
		super("Error in importing file permitions for creator from XML");
	}

}
