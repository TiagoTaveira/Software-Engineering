package pt.tecnico.mydrive.service;

import java.util.Set;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;

public class AddVariableService extends MyDriveService {

    private String token;
    private String variableName;
    private String value;
    private Set<EnvVar> currentVar;

    public AddVariableService(String token, String variableName, String value) {
	this.token = token;
	this.variableName = variableName;
	this.value = value;
    }

    @Override
    protected void dispatch() throws MyDriveException {
	Session session = getSession(token);
	if (!session.getIsValid()) {
	    throw new SessionIsNotValidException();
	}
	session.updateExpirationDate();
	session.addEnvVar(new EnvVar(session, variableName, value));
	this.currentVar = session.getEnvVarSet();	
    }

    public final Set<EnvVar> result() {
	return currentVar;
    }

}
