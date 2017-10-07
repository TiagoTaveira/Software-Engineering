package pt.tecnico.mydrive.exception;

public class GetFileListPathIsNotDirectoryException extends MyDriveException {
	private static final long serialVersionUID = 1L;

	public GetFileListPathIsNotDirectoryException(String completePath) {
		super("Error: '"+completePath+"' is not directory.");
	}

}
