package pt.tecnico.mydrive.service;

import java.util.Set;

import pt.tecnico.mydrive.domain.Dir;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;

public class ListDirectoryService extends MyDriveService {
    private String token;
    private Set<String> filelist;
	private String path;

    public ListDirectoryService(String token) {
	this.token = token;
    }
    public ListDirectoryService(String token, String path) {
    	this.token = token;
    	this.path = path;
        }

    public void dispatch() {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	Dir currentdir = session.getCurrentDirectory();
	filelist = session.getMydrive().getFileListFromDir(currentdir);
    }

    public final Set<String> result() {
	return filelist;
    }

}
