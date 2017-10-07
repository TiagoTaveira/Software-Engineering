package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MDFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserHasNoPermsException;

public class DeleteFileService extends MyDriveService {

    private static final String GUEST_USERNAME = "nobody";
    private static final String ROOT_USERNAME = "root";
    private String token;
    private String filename;

    public DeleteFileService(String token, String filename) {
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

	if (session.getUser().getUsername().equals(GUEST_USERNAME)) {
	    if (!mdfile.getCreator().getUsername().equals(GUEST_USERNAME)) {
		throw new UserHasNoPermsException(session.getUser().getName(), "delete", mdfile.getCompletePath());
	    }
	}
	if (!mdfile.checkForUserPerms("d", session.getUser())) {
	    if (!session.getUser().getUsername().equals(ROOT_USERNAME))
		throw new UserHasNoPermsException(session.getUser().getName(), "delete", mdfile.getCompletePath());
	}
	getMyDrive().removeMdfile(mdfile);
    }
}
