package pt.tecnico.mydrive.exception;

public class CannotRemoveUserException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public CannotRemoveUserException(String username) {
		super("Error removing user. User "+username+" cannot be removed.");
	}

}
