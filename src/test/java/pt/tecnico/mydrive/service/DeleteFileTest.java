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
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;
import pt.tecnico.mydrive.exception.SessionIsNotValidException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;

public class DeleteFileTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	String path = user.getHome().getCompletePath();
	new PlainFile(mydrive, "PlainFile1", path, user, "text1");
	new Link(mydrive, "Link1", path, user, "text2");
	new App(mydrive, "App1", path, user, "text3");
	Dir a = new Dir(mydrive, "Dir1", path, user);

	new PlainFile(mydrive, "PlainFile2", a.getCompletePath(), user);
	new Link(mydrive, "Link2", a.getCompletePath(), user, "text2");
	new App(mydrive, "App2", a.getCompletePath(), user);
	new Dir(mydrive, "Dir2", a.getCompletePath(), user);
    }

    @Test
    public void success() {
	String[] mdfileTypeList = ("PlainFile/Dir/App/Link").split("/");
	for (int i = 0; i < mdfileTypeList.length; i++) {
	    String filename = mdfileTypeList[i] + "1";
	    DeleteFileService service = new DeleteFileService(token, filename);
	    service.execute();
	    boolean deleted = mydrive.getMDFileFromPath(
		    mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath() + "/" + filename) != null;
	    assertTrue(filename + " was deleted", deleted);
	}
    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidToken() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	DeleteFileService service = new DeleteFileService(token, "PlainFile2");
	service.execute();
    }

    @Test(expected = SessionIsNotValidException.class)
    public void invalidSession() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user1").getToken());
	DeleteFileService service = new DeleteFileService(token, "PlainFile2");
	service.execute();
    }

    @Test(expected = UserDoesNotExistException.class)
    public void userDoesNotExist() {
	User user = new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken());
	mydrive.removeUser(user);
	DeleteFileService service = new DeleteFileService(token, "PlainFile2");
	service.execute();
    }
}
