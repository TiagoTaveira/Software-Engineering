package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.service.*;

public class ChangeDirectoryTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    @Override
    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());

	String path = user.getHome().getCompletePath();
	new PlainFile(mydrive, "PlainFile1", path, user, "text1");
    }

    @Test
    public void success() {
	String path = "/home";
	ChangeDirectoryService service = new ChangeDirectoryService(token, path);
	service.execute();
	assertTrue(service.result().equals(path));
    }

    @Test(expected = ChangeDirectoryInvalidPathException.class)
    public void invalidPath() {
	String path = "/home/rekt";
	ChangeDirectoryService service = new ChangeDirectoryService(token, path);
	service.execute();
    }

    @Test(expected = GetFileListPathIsNotDirectoryException.class)
    public void notDirectory() {
	String path1 = "/home/PlainFile1";
	ChangeDirectoryService service = new ChangeDirectoryService(token, path1);
	service.execute();
    }

}
