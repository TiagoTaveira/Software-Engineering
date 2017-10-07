package pt.tecnico.mydrive.domain;

import java.io.UnsupportedEncodingException;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.CreateFileCreatorDoesNotExistException;
import pt.tecnico.mydrive.exception.ImportFileCreatorException;
import pt.tecnico.mydrive.exception.ImportFileFilenameException;
import pt.tecnico.mydrive.exception.ImportFilePathException;
import pt.tecnico.mydrive.exception.ImportFilePermCreatorException;
import pt.tecnico.mydrive.exception.ImportFilePermUsersException;
import pt.tecnico.mydrive.exception.ImportLinkContentException;

public class Link extends Link_Base {

    private static final String ROOT_USERNAME = "root";

    public Link(MyDrive md, String filename, String path, User creator, String content) {
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

    public Link(MyDrive md, String filename, String path, String content) {
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

    public Link(MyDrive md, Element linkElement) {
	xmlImport(linkElement);
    }

    public void xmlImport(Element linkElement)
	    throws ImportFilePathException, ImportFileFilenameException, ImportFilePermCreatorException,
	    ImportFilePermUsersException, ImportFileCreatorException, ImportLinkContentException {
	super.importXML(linkElement);
	try {
	    // TODO: Check if the following encoding is correct
	    setContent(new String(linkElement.getAttribute("content").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e) {
	    throw new ImportLinkContentException();
	}
    }

    public Element xmlExport() {
	Element element = super.xmlExport();
	element.setName("link");
	element.setAttribute("content", getContent());

	return element;
    }
}
