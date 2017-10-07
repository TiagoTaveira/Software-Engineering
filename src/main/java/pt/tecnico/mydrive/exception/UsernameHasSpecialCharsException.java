package pt.tecnico.mydrive.exception;

public class UsernameHasSpecialCharsException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public UsernameHasSpecialCharsException(String username) {
		super("Error username '"+username+"' has special chars or not enough lenght.");
	}

}
