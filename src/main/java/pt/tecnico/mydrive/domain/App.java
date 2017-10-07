package pt.tecnico.mydrive.domain;

import java.io.UnsupportedEncodingException;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.CreateFileCreatorDoesNotExistException;
import pt.tecnico.mydrive.exception.ImportAppContentException;
import pt.tecnico.mydrive.exception.ImportFileCreatorException;
import pt.tecnico.mydrive.exception.ImportFileFilenameException;
import pt.tecnico.mydrive.exception.ImportFilePathException;
import pt.tecnico.mydrive.exception.ImportFilePermCreatorException;
import pt.tecnico.mydrive.exception.ImportFilePermUsersException;

public class App extends App_Base {

    private static final String ROOT_USERNAME = "root";

    public App(MyDrive md, String filename, String path, User creator, String content) {
	setFilename(filename);
	setPath(path);
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
	setContent(content);

    }

    public App(MyDrive md, String filename, String path, User creator) {
	setFilename(filename);
	setPath(path);
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

    public App(MyDrive md, String filename, String path, String content) {
	setFilename(filename);
	setPath(path);
	setLastModification(new DateTime());
	setPermCreator(md.getUserByUsername(ROOT_USERNAME).getMask().substring(0, 3));
	setPermUsers(md.getUserByUsername(ROOT_USERNAME).getMask().substring(4, 7));
	if (md.getUserByUsername(md.getUserByUsername(ROOT_USERNAME).getUsername()) == null) {
	    throw new CreateFileCreatorDoesNotExistException();
	}
	setCreator(md.getUserByUsername(ROOT_USERNAME));
	setMyDrive(md);
	setDir((Dir) md.scoutPath(path));
	setId(md.getNextFID());
	setContent(content);

    }

    public App(MyDrive md, Element appElement) {
	xmlImport(appElement);
    }

    public void xmlImport(Element appElement)
	    throws ImportFilePathException, ImportFileFilenameException, ImportFilePermCreatorException,
	    ImportFilePermUsersException, ImportFileCreatorException, ImportAppContentException {
	super.importXML(appElement);
	try {
	    // TODO: Check if the following encoding is correct
	    setContent(new String(appElement.getAttribute("content").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportAppContentException();
	}
    }

    public Element xmlExport() {
	Element element = super.xmlExport();
	element.setName("app");
	element.setAttribute("content", getContent());

	return element;
    }

}
