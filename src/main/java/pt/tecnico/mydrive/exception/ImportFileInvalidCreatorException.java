package pt.tecnico.mydrive.exception;

public class ImportFileInvalidCreatorException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportFileInvalidCreatorException() {
		super("Error importing file creator from XML");
	}


}
