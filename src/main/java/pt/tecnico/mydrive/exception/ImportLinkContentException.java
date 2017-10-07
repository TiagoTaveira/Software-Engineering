package pt.tecnico.mydrive.exception;

public class ImportLinkContentException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ImportLinkContentException() {
		super("Error importing link content from XML");
	}

}
