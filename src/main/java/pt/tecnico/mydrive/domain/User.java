package pt.tecnico.mydrive.domain;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBFunctionalSetWrapper;
import pt.tecnico.mydrive.exception.ImportUserMaskException;
import pt.tecnico.mydrive.exception.ImportUserNameException;
import pt.tecnico.mydrive.exception.ImportUserPasswordException;
import pt.tecnico.mydrive.exception.OperationNotAllowedException;
import pt.tecnico.mydrive.exception.UsernameHasSpecialCharsException;

public class User extends User_Base {

    public User(MyDrive md, String username, String password, String name, String mask)
	    throws UsernameHasSpecialCharsException {
	super();

	setUsernameProt(username);
	super.setPassword(password);
	super.setName(name);
	super.setMask(mask);
	setMyDriveProt(md);
	super.setHome(new Dir(md, username, "/home", this));
    }

    public User(MyDrive md, String username) throws UsernameHasSpecialCharsException {
	super();
	setUsernameProt(username);
	super.setPassword(username);
	super.setName(username);
	super.setMask("rwxd----");
	setMyDriveProt(md);
	super.setHome(new Dir(md, username, "/home", this));
    }

    public User(MyDrive md, Element userElement) {
	super();
	importUserXML(userElement);
	setMyDriveProt(md);
    }

    public void setMyDriveProt(MyDrive md) {
	if (md == null)
	    super.setMyDrive(null);
	else
	    md.addUser(this);
    }
    
    public void setUsernameProt(String username) throws UsernameHasSpecialCharsException {
	if (!isUsernameValid(username)) {
	    throw new UsernameHasSpecialCharsException(username);
	}
	super.setUsername(username);
    }
    
    @Override
    public void setHome(Dir dir){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setMask(String string){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setMdfile(MDFile mdfile){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setMyDrive(MyDrive mydrive){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setName(String string){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setPassword(String string){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setSession(Session session){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void setUsername(String string){
	throw new OperationNotAllowedException();
    }
    
    @Override
    public void set$ownFiles(OJBFunctionalSetWrapper ownFiles){
	throw new OperationNotAllowedException();
    }
    
    private boolean isUsernameValid(String username) {
	if (username.length() > 2) {
	    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	    Matcher m = p.matcher(username);
	    boolean b = m.find();
	    if (b) {
		return false;
	    }
	    return true;
	}
	return false;
    }
    public void importUserXML(Element userElement)
	    throws ImportUserNameException, ImportUserPasswordException, ImportUserMaskException {
	try {
	    // TODO: Check if the following encoding is correct
	    super.setName(new String(userElement.getAttribute("name").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportUserNameException();
	}
	try {
	    // TODO: Check if the following encoding is correct
	    super.setPassword(new String(userElement.getAttribute("password").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportUserPasswordException();
	}
	try {
	    // TODO: Check if the following encoding is correct
	    super.setMask(new String(userElement.getAttribute("mask").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportUserMaskException();
	}
	Element userfilesElements = userElement.getChild("userfiles");
	Dir dir = new Dir(getMyDrive(), userfilesElements);
	super.setHome(dir);

    }

    public Element xmlExport() {
	Element element = new Element("user");
	element.setAttribute("username", getUsername());
	element.setAttribute("password", getPassword());
	element.setAttribute("name", getName());
	element.setAttribute("mask", getMask());

	Element userfilesElements = new Element("userfiles");
	element.addContent(userfilesElements);
	userfilesElements.addContent(getHome().xmlExport());

	return element;
    }

}
