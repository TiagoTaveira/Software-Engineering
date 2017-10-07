package pt.tecnico.mydrive.exception;

public class ChangeFileCreatorNotEnoughPrevileges extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public ChangeFileCreatorNotEnoughPrevileges() {
		super("Error changing file creator. Not enough previleges.");
	}


}
