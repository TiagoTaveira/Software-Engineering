package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MDFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserHasNoPermsException;

public class ExecuteFileService extends MyDriveService {
    private static final String ROOT_USERNAME = "root";
    private String token;
    private String filename;
    private String[] args;

    public ExecuteFileService(String token, String filename, String[] args) {
	this.token = token;
	this.filename = filename;
	this.args = args;
    }
    public ExecuteFileService(String token, String filename) {
	this.token = token;
	this.filename = filename;
    }

    @Override
    protected void dispatch() throws MyDriveException {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	MDFile mdfile = getMyDrive()
		.getMDFileFromPath((session.getCurrentDirectory().getCompletePath() + "/" + filename));
	if (!mdfile.checkForUserPerms("x", session.getUser())) {
	    if (!session.getUser().getUsername().equals(ROOT_USERNAME))
		throw new UserHasNoPermsException(session.getUser().getName(), "execute", mdfile.getCompletePath());
	}
	getMyDrive().executeMdfile(mdfile);
	//TODO: wrong way!
    }

}
