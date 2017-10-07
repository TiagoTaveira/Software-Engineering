package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.NoSessionForSpecifiedTokenException;

public class WriteFileTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    @Override
    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	String path = user.getHome().getCompletePath();
	new PlainFile(mydrive, "PlainFile1", path, user, "text1");
	new PlainFile(mydrive, "PlainFile2", path, user, "text2");

    }

    @Test
    public void success() {
	WriteFileService service = new WriteFileService(token, "PlainFile1", "text2");
	service.execute();
	PlainFile plainfile1 = (PlainFile) mydrive.getMDFileFromPath(
		mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath() + "/" + "PlainFile1");
	PlainFile plainfile2 = (PlainFile) mydrive.getMDFileFromPath(
		mydrive.getSessionByToken(token).getCurrentDirectory().getCompletePath() + "/" + "PlainFile2");
	boolean equal = plainfile1.getContent().equals(plainfile2.getContent());
	assertTrue("PlainFile1" + " was writen", equal);
    }

    @Test(expected = NoSessionForSpecifiedTokenException.class)
    public void invalidToken() {
	new User(mydrive, "user2");
	token = (new Session(mydrive, "user2", "user2").getToken()) + "d";
	WriteFileService service = new WriteFileService(token, "PlainFile2", "text2");
	service.execute();
    }
}
