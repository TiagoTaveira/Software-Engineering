package pt.tecnico.mydrive.exception;

public class UserDoesNotExistException extends MyDriveException {

    /**
     * 
     */
    private static final long serialVersionUID = 6321377900322405868L;

    public UserDoesNotExistException(String username) {
		super("Error: user '" + username + "' does not exisit.");
    }

}
