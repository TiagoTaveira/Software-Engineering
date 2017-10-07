package pt.tecnico.mydrive.exception;

public class RemoveMDFileCannotRemoveHomeException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public RemoveMDFileCannotRemoveHomeException() {
		super("Error removing mdfile. Cannot remove home.");
	}

}
