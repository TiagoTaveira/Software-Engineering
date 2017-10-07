package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Dir;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.CreateDirMustNotReceiveContentException;
import pt.tecnico.mydrive.exception.CreateFileInvalidFilenameException;
import pt.tecnico.mydrive.exception.CreateLinkMustReceivePathException;
import pt.tecnico.mydrive.exception.CreateFileInvalidTypeException;
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;

public class CreateFileTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	Dir userHome = user.getHome();
	new PlainFile(mydrive, "PlainFile1", userHome.getCompletePath(), user, "text1");
	new Link(mydrive, "Link1", userHome.getCompletePath(), user, "text2");
	new App(mydrive, "App1", userHome.getCompletePath(), user, "text3");
	new Dir(mydrive, "Dir1", userHome.getCompletePath(), user);
    }

    @Test
    public void success() {
	String[] mdfileTypeList = ("PlainFile/Dir/App/Link").split("/");
	for (int i = 0; i < mdfileTypeList.length; i++) {
	    String mdfileType = mdfileTypeList[i];
	    String filename = mdfileTypeList[i] + "2";
	    if (mdfileType != mdfileTypeList[3]) {
		CreateFileService service = new CreateFileService(token, filename, mdfileType);
		service.execute();
		Session a = mydrive.getSessionByToken(token);
		Dir b = a.getCurrentDirectory();
		String c = b.getCompletePath();
		String d = c + "/" + filename;

		boolean created = mydrive.getMDFileFromPath(d) != null;
		assertTrue(filename + " was created", created);
	    }
	}
	for (int i = 0; i < mdfileTypeList.length; i++) {
	    String mdfileType = mdfileTypeList[i];
	    String filename = mdfileTypeList[i] + "3";
	    if (mdfileType != mdfileTypeList[1]) {
		CreateFileService service = new CreateFileService(token, filename, mdfileType,
			mdfileType + "3_content");
		service.execute();
		Session a = mydrive.getSessionByToken(token);
		Dir b = a.getCurrentDirectory();
		String c = b.getCompletePath();
		String d = c + "/" + filename;

		boolean created = mydrive.getMDFileFromPath(d) != null;
		assertTrue(filename + " was created", created);
	    }
	}
    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidToken() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	CreateFileService service = new CreateFileService(token, "PlainFile2", "PlainFile");
	service.execute();
    }

    @Test(expected = SessionIsNotValidException.class)
    public void invalidSession() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user1").getToken());
	CreateFileService service = new CreateFileService(token, "PlainFile2", "PlainFile");
	service.execute();
    }

    @Test(expected = CreateFileInvalidTypeException.class)
    public void invalidFileType() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	CreateFileService service = new CreateFileService(token, "PlainFile2", "NotAFile");
	service.execute();
    }

    @Test(expected = CreateLinkMustReceivePathException.class)
    public void linkMustHaveContent() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	CreateFileService service = new CreateFileService(token, "Link2", "Link");
	service.execute();
    }

    @Test(expected = CreateDirMustNotReceiveContentException.class)
    public void dirMustNotHaveContent() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	CreateFileService service = new CreateFileService(token, "Dir2", "Dir", "content");
	service.execute();
    }

    @Test(expected = CreateFileInvalidFilenameException.class)
    public void invalidFilename() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	CreateFileService service = new CreateFileService(token, "PlainFile/2", "PlainFile");
	service.execute();
    }

    @Test(expected = UserDoesNotExistException.class)
    public void creatorDoesNotExist() {
	User user = new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	mydrive.removeUser(user);
	CreateFileService service = new CreateFileService(token, "PlainFile2", "PlainFile");
	service.execute();
    }

}