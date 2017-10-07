package pt.tecnico.mydrive.exception;

public class RemoveUserUserDoesNotExistException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public RemoveUserUserDoesNotExistException() {
		super("Error removing user. User does not exists.");
	}

}
