package pt.tecnico.mydrive.exception;

public class ImportFilePathException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportFilePathException() {
		super("Error importing file path from XML");
	}

}
