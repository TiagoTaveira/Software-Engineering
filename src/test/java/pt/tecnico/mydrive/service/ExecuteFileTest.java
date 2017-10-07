package pt.tecnico.mydrive.service;

import org.junit.Test;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;

public class ExecuteFileTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    @Override
    protected void populate() {
	mydrive = MyDrive.getInstance();
	User user = new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());
    }

    @Test
    public void success() {
	String filename = null;
	String[] args = null;
	ExecuteFileService service = new ExecuteFileService(token, filename, args);
	service.execute();
	//TODO: test if executed

    }

}
