package pt.tecnico.mydrive.exception;

public class RemoveMDFileDirectoryNotEmptyException extends MyDriveException {
	 private static final long serialVersionUID = 1L;

    public RemoveMDFileDirectoryNotEmptyException() {
        super("Error deleting file directory");
    }

}
