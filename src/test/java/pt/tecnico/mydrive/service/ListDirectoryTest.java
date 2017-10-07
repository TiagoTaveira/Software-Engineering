package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import pt.tecnico.mydrive.domain.MDFile;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;

public class ListDirectoryTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	String path = user.getHome().getCompletePath();
	new PlainFile(mydrive, "PlainFile1", path, user, "text1");
    }

    @Test
    public void success() {
	ListDirectoryService service = new ListDirectoryService("/home/maria");
	service.execute();
	MDFile mdfile = mydrive.getMDFileFromPath(mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath()+"PlainFile1");
	Set<String> file = null;
	file.add((mdfile.getClass().toString() + " " + mdfile.getPermCreator() + mdfile.getPermUsers()
	    + " " + "dim?" + " " + mdfile.getCreator().getUsername() + " " + mdfile.getId() + " "
	    + mdfile.getLastModification().toString() + " " + mdfile.getFilename()));
	assertTrue(service.result().equals(file));

    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidToken() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	ListDirectoryService service = new ListDirectoryService("/home/maria");
	service.execute();
    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidSession() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	ListDirectoryService service = new ListDirectoryService("/home/maria");
	service.execute();
    }
}
