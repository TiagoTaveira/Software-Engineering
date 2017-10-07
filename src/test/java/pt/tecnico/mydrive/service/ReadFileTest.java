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
import pt.tecnico.mydrive.exception.CannotReadDirectoryException;
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;

public class ReadFileTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    @Override
    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	String path = user.getHome().getCompletePath();
	new PlainFile(mydrive, "PlainFile1", path, user, "text1");
	new Link(mydrive, "Link1", path, user, "text2");
	new App(mydrive, "App1", path, user, "text3");
	new Dir(mydrive, "Dir1", path, user);

    }

    @Test
    public void success() {
	String[] mdfileTypeList = ("PlainFile/App/Link").split("/");
	for (int i = 0; i < mdfileTypeList.length; i++) {
	    String filename = mdfileTypeList[i] + "1";
	    ReadFileService service = new ReadFileService(token, filename);
	    service.execute();
	    String result = service.result();
	    PlainFile plainfile = (PlainFile) mydrive.getMDFileFromPath(
		    mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath() + "/" + filename);
	    boolean equal = plainfile.getContent().equals(result);
	    assertTrue(filename + " was read", equal);
	}
    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidToken() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	ReadFileService service = new ReadFileService(token, "PlainFile1");
	service.execute();
    }

    @Test(expected = CannotReadDirectoryException.class)
    public void invalidReadDirectory() {
	String[] mdfileTypeList = ("PlainFile/App/Link").split("/");
	for (int i = 0; i < mdfileTypeList.length; i++) {
	    String filename = mdfileTypeList[i] + "1";
	    ReadFileService service = new ReadFileService(token, filename);
	    service.execute();
	    String result = service.result();
	    Dir directory = (Dir) mydrive.getMDFileFromPath(
		    mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath() + "/" + filename); // not
														// good
	    //boolean equal = directory.getContent().equals(result);
	    //assertTrue(directory + " was read", equal);
	}
    }

}
