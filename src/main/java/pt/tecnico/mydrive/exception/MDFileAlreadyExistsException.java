package pt.tecnico.mydrive.exception;

public class MDFileAlreadyExistsException extends MyDriveException{

    /**
     * 
     */
    private static final long serialVersionUID = 6266163911402739733L;

    public MDFileAlreadyExistsException(String filename, String path) {
	super("Error: file '" + filename + "' already exists on "+path);
    }

}
