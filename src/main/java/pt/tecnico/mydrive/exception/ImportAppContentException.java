package pt.tecnico.mydrive.exception;

public class ImportAppContentException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportAppContentException() {
		super("Error importing app content from XML");
	}


}
