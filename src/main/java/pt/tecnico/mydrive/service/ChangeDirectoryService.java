package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.ChangeDirectoryInvalidPathException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;

public class ChangeDirectoryService extends MyDriveService {
    private String token;
    private String path;

    public ChangeDirectoryService(String token, String path) {

	this.token = token;
	this.path = path;
    }

    public final void dispatch() {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	try {
	    getMyDrive().getMDFileFromPath(path);
	    session.setCurrentDirectory(path);
	} catch (Exception e) {
	    throw new ChangeDirectoryInvalidPathException(path);
	}
    }

    public final String result() {
	return path;
    }

}
