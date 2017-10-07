package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Dir;
import pt.tecnico.mydrive.domain.MDFile;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.CannotWriteDirectoryException;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserHasNoPermsException;

public class WriteFileService extends MyDriveService {

    private static final String GUEST_USERNAME = "nobody";
    private static final String ROOT_USERNAME = "root";
    private String token;
    private String filename;
    private String content;

    public WriteFileService(String token, String filename, String content) {
	this.token = token;
	this.filename = filename;
	this.content = content;
    }

    protected void dispatch() throws MyDriveException {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	MDFile mdfile = getMyDrive()
		.getMDFileFromPath((session.getCurrentDirectory().getCompletePath() + "/" + filename));
	if (mdfile instanceof Dir) {
	    throw new CannotWriteDirectoryException(); // TODO: confirmar isto
	}
	if (session.getUser().getUsername().equals(GUEST_USERNAME)) {
	    if (!mdfile.getCreator().getUsername().equals(GUEST_USERNAME)) {
		throw new UserHasNoPermsException(session.getUser().getName(), "write", mdfile.getCompletePath());
	    }
	}
	if (!mdfile.checkForUserPerms("d", session.getUser())) {
	    if (!session.getUser().getUsername().equals(ROOT_USERNAME))
		throw new UserHasNoPermsException(session.getUser().getName(), "write", mdfile.getCompletePath());
	}
	PlainFile plainfile = (PlainFile) mdfile;
	plainfile.setContent(content);

    }
}
