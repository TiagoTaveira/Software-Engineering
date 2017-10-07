package pt.tecnico.mydrive.presentation;

import java.util.TreeMap;

public class MyDriveShell extends Shell {
    
    private static final String GUEST_USERNAME = "nobody";
    private String lastToken;
    private String lastUser;
    private TreeMap<String, String> sessionList;

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public static void main(String[] args) throws Exception {
	MyDriveShell mdShell = new MyDriveShell();
	mdShell.execute();
    }

    public MyDriveShell() { // add commands here
	super("MyDrive");
	new LoginUserCommand(this);
	new KeyCommand(this);
	new ExecuteCommand(this);
	new ListCommand(this);
	new WriteCommand(this);
	new EnvCommand(this);
	new ChangeDirectoryCommand(this);
    }
    
    public String getLastToken(){
	return lastToken;	
    }
    
    
    
    public void addSession(String username, String token){
	sessionList.put(username, token);
    }
    
    public void setLastToken(String token){
	lastToken = token; 
    }
    
    public void removeGuest(){
	sessionList.remove(GUEST_USERNAME);
    }

    public String getSession(String username) {
	return sessionList.get(username);
    }
}