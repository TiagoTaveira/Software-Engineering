package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.CreateFileCreatorDoesNotExistException;
import pt.tecnico.mydrive.exception.CreateFileInvalidFilenameException;
import pt.tecnico.mydrive.exception.ImportFileCreatorException;
import pt.tecnico.mydrive.exception.ImportFileFilenameException;
import pt.tecnico.mydrive.exception.ImportFilePathException;
import pt.tecnico.mydrive.exception.ImportFilePermCreatorException;
import pt.tecnico.mydrive.exception.ImportFilePermUsersException;
import pt.tecnico.mydrive.exception.WrongUseOfCreateHomeException;

public class Dir extends Dir_Base {

    private static final String ROOT_USERNAME = "root";

    public Dir(MyDrive md, String filename, String path, User creator) {
	setFilename(filename);
	setPath(path);
	setLastModification(new DateTime());
	setPermCreator(creator.getMask().substring(0, 4));
	setPermUsers(creator.getMask().substring(4, 8));
	if (md.getUserByUsername(creator.getUsername()) == null) {
	    throw new CreateFileCreatorDoesNotExistException();
	}
	setCreator(creator);
	setMyDrive(md);
	setDir((Dir) md.scoutPath(path));
	setId(md.getNextFID());

    }

    public Dir(MyDrive md, String filename, Boolean isHome)
	    throws WrongUseOfCreateHomeException, CreateFileInvalidFilenameException {
	if (!isHome) {
	    throw new WrongUseOfCreateHomeException();
	}
	setPath("");
	setCreator(null);
	setLastModification(new DateTime());
	setFilename(filename);
	setPermCreator("----");
	setPermUsers("----");
	setMyDrive(md);
	setDir(this);
	setId(md.getNextFID());

    }

    public Dir(MyDrive md, String filename, String path) {
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
    }

    public Dir(MyDrive md, Element dirElements) {
	xmlImport(dirElements);
    }

    public void xmlImport(Element dirElements) throws ImportFilePathException, ImportFileFilenameException,
	    ImportFilePermCreatorException, ImportFilePermUsersException, ImportFileCreatorException {
	super.importXML(dirElements);
    }

    public Element xmlExport() {
	Element element = super.xmlExport();
	element.setName("dir");

	Element mdfilesElement = new Element("mdfiles");
	element.addContent(mdfilesElement);

	return element;
    }

}
