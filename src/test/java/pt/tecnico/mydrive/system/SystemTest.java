package pt.tecnico.mydrive.system;

import org.junit.Test;

import pt.tecnico.mydrive.presentation.LoginUserCommand;
import pt.tecnico.mydrive.presentation.MyDriveShell;
import pt.tecnico.mydrive.service.AbstractServiceTest;

public class SystemTest extends AbstractServiceTest {

    private MyDriveShell mdShell;

    protected void populate() {
	mdShell = new MyDriveShell();
    }

    @Test
    public void success() {
	new LoginUserCommand(mdShell).execute(new String[] { "root", "***" });
	//TODO: OTHER TEST COMMANDS
    }
}
