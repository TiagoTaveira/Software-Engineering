package pt.tecnico.mydrive.exception;

public class ImportFileFilenameException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportFileFilenameException() {
		super("Error importing filename from XML");
	}

}
