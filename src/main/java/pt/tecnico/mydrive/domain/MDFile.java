package pt.tecnico.mydrive.domain;

import java.io.UnsupportedEncodingException;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.ChangeFileCreatorNotEnoughPrevileges;
import pt.tecnico.mydrive.exception.ChangeFilePermNotEnoughPrevileges;
import pt.tecnico.mydrive.exception.CreateFileCreatorDoesNotExistException;
import pt.tecnico.mydrive.exception.CreateFileInvalidFilenameException;
import pt.tecnico.mydrive.exception.CreateFilePathnameToLongException;
import pt.tecnico.mydrive.exception.GetMDFileFromPathFileDoesNotExistException;
import pt.tecnico.mydrive.exception.ImportFileCreatorException;
import pt.tecnico.mydrive.exception.ImportFileFilenameException;
import pt.tecnico.mydrive.exception.ImportFilePathException;
import pt.tecnico.mydrive.exception.ImportFilePermCreatorException;
import pt.tecnico.mydrive.exception.ImportFilePermUsersException;
import pt.tecnico.mydrive.exception.InvalidTaskException;

public class MDFile extends MDFile_Base {

    private static final String ROOT_USERNAME = "root";

    public MDFile() {
	super();
    }

    public MDFile(MyDrive md, String filename, String path, User creator) throws CreateFileInvalidFilenameException,
	    CreateFileCreatorDoesNotExistException, GetMDFileFromPathFileDoesNotExistException {
	setPath(path);
	setFilename(filename);
	setLastModification(new DateTime());
	setPermCreator(creator.getMask().substring(0, 3));
	setPermUsers(creator.getMask().substring(4, 7));
	if (md.getUserByUsername(creator.getUsername()) == null) {
	    throw new CreateFileCreatorDoesNotExistException();
	}
	setCreator(creator);
	setMyDrive(md);
	setDir((Dir) md.scoutPath(path));
	setId(md.getNextFID());

    }

    public MDFile(MyDrive md, String filename, String path)
	    throws CreateFileInvalidFilenameException, GetMDFileFromPathFileDoesNotExistException {
	this(md, filename, path, md.getUserByUsername(ROOT_USERNAME));
    }

    public MDFile(MyDrive md, Element xml) throws GetMDFileFromPathFileDoesNotExistException {
	super();
	importXML(xml);
	setMyDrive(md);
	setDir((Dir) md.scoutPath(getPath()));// May cause error
	setId(md.getNextFID());
    }

    @Override
    public void setMyDrive(MyDrive md) {
	if (md == null)
	    super.setMyDrive(null);
	else
	    md.addMdfile(this);
    }

    @Override
    public void setFilename(String filename) throws CreateFileInvalidFilenameException {
	if (filename.contains("/"))
	    throw new CreateFileInvalidFilenameException(filename);
	if (filename.contains("\0"))
	    throw new CreateFileInvalidFilenameException(filename);
	if ((getPath() + "/" + filename).length() > 1024)
	    throw new CreateFilePathnameToLongException(getPath() + "/" + filename);
	super.setFilename(filename);
    }

    public void changePermCreator(User user, String newMask) throws ChangeFilePermNotEnoughPrevileges {
	if (user.equals(getCreator()) || user.equals(getMyDrive().getUserByUsername(ROOT_USERNAME))) {
	    setPermCreator(newMask);
	} else {
	    throw new ChangeFilePermNotEnoughPrevileges();
	}
    }

    public void changePermUsers(User user, String newMask) throws ChangeFilePermNotEnoughPrevileges {
	if (user.equals(getCreator()) || user.equals(getMyDrive().getUserByUsername(ROOT_USERNAME))) {
	    setPermUsers(newMask);
	} else {
	    throw new ChangeFilePermNotEnoughPrevileges();
	}
    }

    public void changeCreator(User user, User newCreator) throws ChangeFileCreatorNotEnoughPrevileges {
	if (user.equals(getMyDrive().getUserByUsername(ROOT_USERNAME))) {
	    setCreator(newCreator);
	} else {
	    throw new ChangeFileCreatorNotEnoughPrevileges();
	}
    }

    public boolean checkForUserPerms(String task, User user) {
	int taskId = 0;
	if (task.equals("r"))
	    taskId = 0;
	else {
	    if (task.equals("w"))
		taskId = 1;
	    else {
		if (task.equals("x"))
		    taskId = 2;
		else {
		    if (task.equals("d"))
			taskId = 3;
		    else
			throw new InvalidTaskException(task);
		}
	    }
	}
	if (getCreator().getUsername().equals(user.getUsername())) {
	    if (String.valueOf(getPermCreator().charAt(taskId)).equals("-"))
		return false;
	    return true;
	}
	if (String.valueOf(getPermUsers().charAt(taskId)).equals("-"))
	    return false;
	return true;
    }

    public void importXML(Element mdfileElement) throws ImportFilePathException, ImportFileFilenameException,
	    ImportFilePermCreatorException, ImportFilePermUsersException, ImportFileCreatorException {
	try {
	    setPath(new String(mdfileElement.getAttribute("path").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportFilePathException();
	}
	try {
	    setFilename(new String(mdfileElement.getAttribute("filename").getValue().getBytes("UTF-8")));
	} catch (Exception e) {
	    throw new ImportFileFilenameException();
	}
	try {
	    String username = (new String(mdfileElement.getAttribute("creator").getValue().getBytes("UTF-8")));

	    User creator = getMyDrive().getUserByUsername(username);

	    setCreator(creator);
	} catch (Exception e) {
	    throw new ImportFileCreatorException();
	}
	try {
	    setPermCreator(new String(mdfileElement.getAttribute("permcreator").getValue().getBytes("UTF-8")));
	} catch (Exception e) {
	    throw new ImportFilePermCreatorException();
	}
	try {
	    setPermUsers(new String(mdfileElement.getAttribute("permusers").getValue().getBytes("UTF-8")));
	} catch (Exception e) {
	    throw new ImportFilePermUsersException();
	}

    }

    public String getCompletePath() {
	String a = getPath();
	String b = getFilename();
	return a + "/" + b;
    }

    public Element xmlExport() {
	Element element = new Element("mdfile");

	element.setAttribute("id", getId() + "");
	element.setAttribute("filename", getFilename());
	element.setAttribute("path", getPath());
	element.setAttribute("lastModification", getLastModification().toString());
	element.setAttribute("permcreator", getPermCreator());
	element.setAttribute("permusers", getPermUsers());
	element.setAttribute("creator", getCreator().getUsername());

	return element;
    }
}
