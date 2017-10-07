package pt.tecnico.mydrive.exception;

public class AddUserUserAlreadyExistsException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public AddUserUserAlreadyExistsException(String username) {
	super("Error adding user. User with username: '" + username + "' already exists.");
    }

}
