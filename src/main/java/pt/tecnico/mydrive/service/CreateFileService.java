package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Dir;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.CreateDirMustNotReceiveContentException;
import pt.tecnico.mydrive.exception.CreateLinkMustReceivePathException;
import pt.tecnico.mydrive.exception.CreateFileInvalidTypeException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;

public class CreateFileService extends MyDriveService {
    private String token;
    private String filename;
    private String mdfileType;
    private String content;

    public CreateFileService(String token, String filename, String mdfileType) {
	if (mdfileType.equals("Link")) {
	    throw new CreateLinkMustReceivePathException();
	}
	if (mdfileType.equals("Dir") || mdfileType.equals("App") || mdfileType.equals("PlainFile")) {
	    this.token = token;
	    this.filename = filename;
	    this.mdfileType = mdfileType;
	    this.content = "";
	} else
	    throw new CreateFileInvalidTypeException(mdfileType);
    }

    public CreateFileService(String token, String filename, String mdfileType, String content) {
	if (mdfileType.equals("Dir")) {
	    throw new CreateDirMustNotReceiveContentException();
	}
	if (mdfileType.equals("Link") || mdfileType.equals("App") || mdfileType.equals("PlainFile")) {
	    this.token = token;
	    this.filename = filename;
	    this.mdfileType = mdfileType;
	    this.content = content;
	} else
	    throw new CreateFileInvalidTypeException(mdfileType);

    }

    public final void dispatch() {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	if (mdfileType.equals("Dir")) {
	    new Dir(getMyDrive(), filename, getSession(token).getCurrentDirectory().getCompletePath(),
		    getSession(token).getUser());
	    return;
	}
	if (mdfileType.equals("Link")) {
	    new Link(getMyDrive(), filename, getSession(token).getCurrentDirectory().getCompletePath(),
		    getSession(token).getUser(), content);
	}
	if (mdfileType.equals("App")) {
	    new App(getMyDrive(), filename, getSession(token).getCurrentDirectory().getCompletePath(),
		    getSession(token).getUser(), content);
	}
	if (mdfileType.equals("PlainFile")) {
	    new PlainFile(getMyDrive(), filename, getSession(token).getCurrentDirectory().getCompletePath(),
		    getSession(token).getUser(), content);
	}
    }

}
