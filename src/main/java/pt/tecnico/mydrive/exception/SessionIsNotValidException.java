package pt.tecnico.mydrive.exception;

public class SessionIsNotValidException extends MyDriveException {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String getMessage(){
	return "Session is not valid. Please login again";
    }

}
