package pt.tecnico.mydrive.exception;

public class ImportPlainFileTextException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportPlainFileTextException() {
		super("Error importing plain file content from XML");
	}


}
