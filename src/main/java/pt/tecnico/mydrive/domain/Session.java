package pt.tecnico.mydrive.domain;

import java.math.BigInteger;
import java.util.Random;

import org.joda.time.DateTime;

import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.tecnico.mydrive.exception.OperationNotAllowedException;

public class Session extends Session_Base {

    private static final int MIN_PASSWORD_LENGHT = 8;
    private static final String GUEST_USERNAME = "nobody";
    private static final String ROOT_USERNAME = "root";
    private static final int STANDARD_SESSION_DURATION = 2;
    private static final int ROOT_SESSION_DURATION = 10;
    private static final int GUEST_SESSION_DURATION = 69;

    public Session(MyDrive md, String username, String password) {
	User user = md.getUserByUsername(username);
	if (!passwordValidator(user, password))
	    super.setIsValid(false);
	else
	    super.setIsValid(true);
	super.setToken(null);
	setMyDriveProt(md);
	super.setUser(user);
	super.setToken(generateToken());
	super.setCurrentDirectory(user.getHome());
	if (username.equals(ROOT_USERNAME))
	    super.setExpireDate((new DateTime()).plusMinutes(ROOT_SESSION_DURATION));
	else if (username.equals(GUEST_USERNAME))
	    super.setExpireDate((new DateTime()).plusYears(GUEST_SESSION_DURATION));
	else
	    super.setExpireDate((new DateTime()).plusHours(STANDARD_SESSION_DURATION));
	md.refreshSessionList();
    }

    private void setMyDriveProt(MyDrive md) {
	if (md == null)
	    super.setMydrive(null);
	else
	    md.addSession(this);
    }

    @Override
    public void setMydrive(MyDrive md) {
	throw new OperationNotAllowedException();
    }

    private String generateToken() {
	Long tokenLong = (new BigInteger(64, new Random()).longValue());
	String tokenString = tokenLong.toString();
	if (getMydrive().checkToken(tokenString))
	    return generateToken();

	return tokenString;

    }

    public Boolean getIsValid() {
	DateTime expireDate = getExpireDate();
	if (getUser().getUsername().equals(GUEST_USERNAME))
	    return true;
	if (expireDate.isBeforeNow()) {
	    super.setIsValid(false);
	}
	return super.getIsValid();
    }

    private boolean passwordValidator(User user, String password) {
	if (password.length() < MIN_PASSWORD_LENGHT) {
	    if (user.getUsername().equals(GUEST_USERNAME))
		return true;
	    if (user.getUsername().equals(ROOT_USERNAME))
		return true;
	}
	String userPass = user.getPassword();
	if (userPass.equals(password)) {
	    return true;
	}
	return false;
    }

    public void updateExpirationDate() {
	String user = getUser().getUsername();
	if (user.equals(ROOT_USERNAME))
	    super.setExpireDate(getExpireDate().plusMinutes(ROOT_SESSION_DURATION));
	if (user.equals(GUEST_USERNAME))
	    super.setExpireDate(getExpireDate().plusYears(GUEST_SESSION_DURATION));
	else
	    super.setExpireDate(getExpireDate().plusHours(STANDARD_SESSION_DURATION));
    }

    public void setCurrentDirectory(String path) {
	super.setCurrentDirectory((Dir) getMydrive().getMDFileFromPath(getMydrive().getAbsolutePath(getToken(), path)));
    }

    @Override
    public void setUser(User user) {
	throw new OperationNotAllowedException();
    }

    @Override
    public void setToken(String token) {
	throw new OperationNotAllowedException();
    }

    @Override
    public void setIsValid(Boolean bool) {
	throw new OperationNotAllowedException();
    }

    @Override
    public void setExpireDate(DateTime date) {
	throw new OperationNotAllowedException();
    }

    @Override
    public void set$envVar(OJBFunctionalSetWrapper envVar) {
	throw new OperationNotAllowedException();
    }

    @Override
    public void addEnvVar(EnvVar envVar) {
	throw new OperationNotAllowedException();
    }
}
