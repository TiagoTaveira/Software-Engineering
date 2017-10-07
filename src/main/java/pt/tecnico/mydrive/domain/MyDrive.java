package pt.tecnico.mydrive.domain;

import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.exception.AddUserUserAlreadyExistsException;
import pt.tecnico.mydrive.exception.CannotRemoveUserException;
import pt.tecnico.mydrive.exception.CreateFileInvalidFilenameException;
import pt.tecnico.mydrive.exception.FileWithSpecifiedIdDoesNotExistException;
import pt.tecnico.mydrive.exception.GetFileListPathIsNotDirectoryException;
import pt.tecnico.mydrive.exception.GetMDFileFromPathFileDoesNotExistException;
import pt.tecnico.mydrive.exception.ImportUserAlreadyExistsException;
import pt.tecnico.mydrive.exception.MDFileAlreadyExistsException;
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;
import pt.tecnico.mydrive.exception.OperationNotAllowedException;
import pt.tecnico.mydrive.exception.RelativePathDoesNotStartOnCurrentDirectoryException;
import pt.tecnico.mydrive.exception.RemoveMDFileCannotRemoveHomeException;
import pt.tecnico.mydrive.exception.RemoveUserUserDoesNotExistException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.UsernameHasSpecialCharsException;
import pt.tecnico.mydrive.exception.WrongUseOfCreateHomeException;

public class MyDrive extends MyDrive_Base {

    private static final String ROOT_USERNAME = "root";
    private static final String GUEST_USERNAME = "nobody";

    public static MyDrive getInstance() {
	MyDrive md = FenixFramework.getDomainRoot().getMydrive();
	if (md != null)
	    return md;
	return new MyDrive();
    }

    private MyDrive() {
	setNextFID(0);
	try {
	    Dir mydriveHome = new Dir(this, " ", true);
	    addMdfile(mydriveHome);
	    Dir home = new Dir(this, "home", true);
	    home.setPath("");
	    User root = new User(this, ROOT_USERNAME, "****", "Super User", "rwxdr-x-");
	    mydriveHome.setCreator(root);
	    home.setCreator(root);
	    new User(this, "nobody", "", "Guest", "rwxdr-x-");

	} catch (WrongUseOfCreateHomeException | CreateFileInvalidFilenameException
		| UsernameHasSpecialCharsException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	setRoot(FenixFramework.getDomainRoot());
    }

    @Override
    public void addMdfile(MDFile fileToBeAdded) {
	for (MDFile mdfile : getMdfileSet()) {
	    if (mdfile.getFilename().equals(fileToBeAdded.getFilename())) {
		if (mdfile.getPath().equals(fileToBeAdded.getPath())) {
		    if (fileToBeAdded.getFilename().equals(" ")) {
			break;
		    }
		    throw new MDFileAlreadyExistsException(fileToBeAdded.getFilename(), fileToBeAdded.getPath());
		}
	    }
	}
	super.addMdfile(fileToBeAdded);
    }

    @Override
    public void addUser(User user) throws AddUserUserAlreadyExistsException {
	for (User user1 : getUserSet()) {
	    if (user1.getUsername().equals(user.getUsername())) {
		throw new AddUserUserAlreadyExistsException(user.getUsername());
	    }
	}
	super.addUser(user);

    }

    public void cleanup() {
	for (User user : getUserSet())
	    user.getMask();
    }

    public MDFile scoutPath(String path) {
	String[] parts = path.split("/");
	scoutAUX(1, parts, "");
	return getMDFileFromPath(path);

    }

    @Override
    public void setRoot(DomainRoot domainroot) {
	throw new OperationNotAllowedException();
    }

    @Override
    public Set<Session> getSessionSet() {
	throw new OperationNotAllowedException();
    }

    private void scoutAUX(int level, String[] parts, String currentPath) {
	if (parts.length == level) {
	    return;
	}
	String newPath = currentPath + "/" + parts[level];
	for (MDFile mdfile : getMdfileSet()) {
	    if ((mdfile.getCompletePath().equals(newPath))) {
		scoutAUX(level + 1, parts, newPath);
		return;
	    }
	}
	Dir dir = new Dir(this, parts[level], currentPath, getUserByUsername(ROOT_USERNAME));
	addMdfile(dir);
	scoutAUX(level + 1, parts, newPath);
	return;
    }

    public Set<String> getFileListFromDir(Dir dir) {
	Set<MDFile> filelist = dir.getMdfileSet();
	// TODO: They need to be sorted by id
	Set<String> finallist = null;
	for (MDFile mdfile : filelist) {
	    String fileData = (mdfile.getClass().toString() + " " + mdfile.getPermCreator() + mdfile.getPermUsers()
		    + " " + "dim?" + " " + mdfile.getCreator().getUsername() + " " + mdfile.getId() + " "
		    + mdfile.getLastModification().toString() + " " + mdfile.getFilename());
	    finallist.add(fileData);
	}
	return finallist;
    }

    public void getFileListFromPath(String completePath)
	    throws GetFileListPathIsNotDirectoryException, GetMDFileFromPathFileDoesNotExistException {
	MDFile mdfile = getMDFileFromPath(completePath);
	if (mdfile instanceof Dir) {
	    getFileListFromDir((Dir) mdfile);
	} else {
	    throw new GetFileListPathIsNotDirectoryException(completePath);
	}
    }

    public Dir getHomeDir() {
	for (MDFile mdfile : getMdfileSet()) {
	    if (mdfile.getId() == 0) {
		return (Dir) mdfile;
	    }
	}
	return null;
    }

    public void readMDFileFromPath(String completePath) throws GetMDFileFromPathFileDoesNotExistException {
	MDFile mdfile = getMDFileFromPath(completePath);
	readMDFile(mdfile);
    }

    public void refreshSessionList() {
	for (Session session : super.getSessionSet()) {
	    if (!session.getIsValid() && !session.equals(this)) {
		removeSession(session);
	    }
	}
    }

    public void readMDFile(MDFile mdfile) {
	if (mdfile instanceof PlainFile) {
	    String text = ((PlainFile) mdfile).getContent();
	    System.out.println(text);
	    return;
	}
	if (mdfile instanceof Dir) {
	    return;
	}
	if (mdfile instanceof Link) {
	    String content = ((App) mdfile).getContent();
	    System.out.println(content);
	    return;
	}
	if (mdfile instanceof App) {
	    String content = ((App) mdfile).getContent();
	    System.out.println(content);
	    return;
	}
    }

    public MDFile getMDFileFromPath(String completePath) throws GetMDFileFromPathFileDoesNotExistException {
	for (MDFile mdfile : getMdfileSet()) {
	    if ((mdfile.getCompletePath().equals(completePath))) {
		return mdfile;
	    }
	}
	throw new GetMDFileFromPathFileDoesNotExistException(completePath);
    }

    @Override
    public int getNextFID() {
	int id = super.getNextFID();
	setNextFID(id + 1);
	return id;
    }

    public User getUserByUsername(String username) {
	for (User user : getUserSet()) {
	    if (user.getUsername().equals(username)) {
		return user;
	    }
	}
	throw new UserDoesNotExistException(username);
    }

    public MDFile getMDFileById(int id) {
	for (MDFile mdfile : getMdfileSet()) {
	    if (mdfile.getId() == id) {
		return mdfile;
	    }
	}
	throw new FileWithSpecifiedIdDoesNotExistException(id);
    }

    public void removeMdfdileFromPath(String path)
	    throws RemoveMDFileCannotRemoveHomeException, GetMDFileFromPathFileDoesNotExistException {
	MDFile mdfile = getMDFileFromPath(path);
	removeMdfile(mdfile);
    }

    @Override
    public void removeMdfile(MDFile mdfile) throws RemoveMDFileCannotRemoveHomeException {
	if (mdfile.getId() == 0) {
	    throw new RemoveMDFileCannotRemoveHomeException();
	}
	if (mdfile instanceof Dir) {
	    if (((Dir) mdfile).getMdfileSet().size() > 0) {
		for (MDFile mdfile2 : ((Dir) mdfile).getMdfileSet()) {
		    removeMdfile(mdfile2);
		}
	    }
	}
	// REMOVER DOS FILES DO USER E AFINS
	super.removeMdfile(mdfile);
    }

    public void executeMdfile(MDFile mdfile) {
	if (mdfile instanceof PlainFile) {
	    System.out.println("execute plainFile");
	    return;
	}
	if (mdfile instanceof Dir) {
	    System.out.println("execute Dir");
	    return;
	}
	if (mdfile instanceof Link) {
	    System.out.println("execute Link");
	    return;
	}
	if (mdfile instanceof App) {
	    System.out.println("execute App");
	    return;
	}
    }

    @Override
    public void removeUser(User user) throws RemoveUserUserDoesNotExistException, CannotRemoveUserException {
	String username = user.getUsername();
	if (getUserByUsername(username) == null) {
	    throw new RemoveUserUserDoesNotExistException();
	}
	if (username.equals(ROOT_USERNAME))
	    throw new CannotRemoveUserException(username);
	if (username.equals(GUEST_USERNAME))
	    throw new CannotRemoveUserException(username);

	super.removeUser(user);
    }

    public Document xmlExport() {
	Element element = new Element("mydrive");
	Document doc = new Document(element);
	for (User u : getUserSet())
	    element.addContent(u.xmlExport());

	for (MDFile mdfiles : getMdfileSet())
	    element.addContent(mdfiles.xmlExport());

	return doc;
    }

    public void xmlImport(Element rootElement) {
	for (Element userElement : rootElement.getChildren("user")) {
	    String username = userElement.getAttribute("username").getValue();
	    User user = getUserByUsername(username);

	    if (user != null) {
		throw new ImportUserAlreadyExistsException();
	    }
	    user = new User(this, userElement);

	}
	for (Element mdfileElement : rootElement.getChildren("dir")) {
	    Dir dir = new Dir(this, mdfileElement);
	    addMdfile(dir);
	}
	for (Element mdfileElement : rootElement.getChildren("plainfile")) {
	    PlainFile plainfile = new PlainFile(this, mdfileElement);
	    addMdfile(plainfile);
	}
	for (Element mdfileElement : rootElement.getChildren("link")) {
	    Link link = new Link(this, mdfileElement);
	    addMdfile(link);
	}
	for (Element mdfileElement : rootElement.getChildren("app")) {
	    App app = new App(this, mdfileElement);
	    addMdfile(app);
	}
    }

    public String getAbsolutePath(String token, String path) {

	if (!path.startsWith(getSessionByToken(token).getCurrentDirectory().getCompletePath())) {
	    String[] relParts = path.split("/");
	    String absolutePath = getSessionByToken(token).getCurrentDirectory().getCompletePath();
	    String[] absParts = absolutePath.split("/");
	    if (!relParts[0].equals(absParts[absParts.length - 1])) {
		throw new RelativePathDoesNotStartOnCurrentDirectoryException(path,
			getSessionByToken(token).getCurrentDirectory().getCompletePath());
	    }
	    for (int i = 1; i < relParts.length; i++) {
		String dir = relParts[i];
		if (dir.equals("..")) {
		    if (absParts.length == 0) {
			continue;
		    }
		    absolutePath = absolutePath.substring(0,
			    absolutePath.length() - (absParts[absParts.length - 1]).length() - 1);
		    absParts = absolutePath.split("/");
		    continue;
		}
		if (dir.equals(".")) {
		    continue;
		} else {
		    absolutePath = absolutePath + "/" + dir;
		    absParts = absolutePath.split("/");
		    continue;
		}
	    }
	}
	return path;

    }

    public String generatePathFromParts(String[] parts) {
	String path = "/";
	for (int i = 0; i < parts.length; i++) {
	    String mdfile = parts[i];
	    path = path + mdfile;
	}
	return path;
    }

    public Session getSessionByToken(String token) {
	for (Session session : super.getSessionSet()) {
	    if (session.getToken().equals(token)) {
		return session;
	    }
	}
	throw new NoSessionForSpecifiedTokenException();
    }

    public boolean checkToken(String token) {
	for (Session session : super.getSessionSet()) {
	    if (session.getToken().equals(token)) {
		return true;
	    }
	}
	return false;
    }
}
