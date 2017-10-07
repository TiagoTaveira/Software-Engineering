package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Session;

public class LoginUserService extends MyDriveService {
    private String username;
    private String password;
    private Session session;

    public LoginUserService(String username, String password) {
	this.username = username;
	this.password = password;
    }

    public final void dispatch() {
	session = new Session(getMyDrive(), username, password);
    }
    
    public String getUsername() {
	return username;
    }

    public final String result() {
	return session.getToken();
    }
}
