package pt.tecnico.mydrive.exception;

public class GetMDFileFromPathFileDoesNotExistException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public GetMDFileFromPathFileDoesNotExistException(String completePath) {
	super("Error geting file from path. '" + completePath + "' File does not exist.");
    }

}
