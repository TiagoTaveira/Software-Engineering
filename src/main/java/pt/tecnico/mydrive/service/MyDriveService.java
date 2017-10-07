package pt.tecnico.mydrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.MyDriveException;

public abstract class MyDriveService {
    protected static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
	dispatch();
    }

    static MyDrive getMyDrive() {
	return MyDrive.getInstance();
    }

    protected abstract void dispatch() throws MyDriveException;

    public static Session getSession(String token) {
	return getMyDrive().getSessionByToken(token);
    }

    public static User getUser(String username) {
	return getMyDrive().getUserByUsername(username);
    }
}