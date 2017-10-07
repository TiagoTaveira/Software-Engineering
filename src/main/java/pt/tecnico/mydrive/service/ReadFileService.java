package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Dir;
import pt.tecnico.mydrive.domain.MDFile;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.CannotReadDirectoryException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserHasNoPermsException;

public class ReadFileService extends MyDriveService {
    private static final String ROOT_USERNAME = "root";
    private String token;
    private String filename;
    private String content;

    public ReadFileService(String token, String filename) {
	this.token = token;
	this.filename = filename;
    }

    public final void dispatch() {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	MDFile mdfile = getMyDrive()
		.getMDFileFromPath((session.getCurrentDirectory().getCompletePath() + "/" + filename));
	if (mdfile instanceof Dir) {
	    throw new CannotReadDirectoryException(); // TODO: Confirmar isto
	}
	if (!mdfile.checkForUserPerms("r", session.getUser())) {
	    if (!session.getUser().getUsername().equals(ROOT_USERNAME))
		throw new UserHasNoPermsException(session.getUser().getName(), "read", mdfile.getCompletePath());
	}
	content = ((PlainFile) mdfile).getContent();

    }

    public final String result() {
	return content;
    }

}