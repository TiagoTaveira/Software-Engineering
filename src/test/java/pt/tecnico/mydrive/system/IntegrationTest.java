package pt.tecnico.mydrive.system;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.tecnico.mydrive.service.AbstractServiceTest;
import pt.tecnico.mydrive.service.CreateFileService;
import pt.tecnico.mydrive.service.DeleteFileService;
import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.LoginUserService;

public class IntegrationTest extends AbstractServiceTest {

    private static final List<String> users = new ArrayList<String>();
    private static final String u1 = "user1", u2 = "user2", u3 = "user3";
    private static final String u4 = "user4", u5 = "user5", u6 = "user6";

    private static final List<String> files = new ArrayList<String>();
    private static final String f1 = "file1", f2 = "file2", f3 = "file3";
    private static final String f4 = "file4", f5 = "file5", f6 = "file6";
    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "***";
    private static final String PF_MDFTYPE = "PlainFile";
    private static final String A_MDFTYPE = "App";
    private static final String D_MDFTYPE = "Dir";
    private static final String L_MDFTYPE = "Link";

    protected void populate() { // populate mockup
	users.add(u2);
	users.add(u4);
    }

    @Test
    public void success() throws Exception {

	LoginUserService loginRootService = new LoginUserService(ROOT_USERNAME, ROOT_PASSWORD);
	loginRootService.execute();
	String rootToken = loginRootService.result();

	new CreateFileService(rootToken, f1, PF_MDFTYPE);
	new CreateFileService(rootToken, f2, A_MDFTYPE);
	ListDirectoryService ls1 = new ListDirectoryService(rootToken);
	ls1.execute();
	assertEquals(ls1.result().size(), 2);

	new DeleteFileService(rootToken, f2);
	ListDirectoryService ls2 = new ListDirectoryService(rootToken);
	ls2.execute();
	assertEquals(ls2.result().size(), 1);
	
	
	//TODO: NOT COMPLETED
    }

}
