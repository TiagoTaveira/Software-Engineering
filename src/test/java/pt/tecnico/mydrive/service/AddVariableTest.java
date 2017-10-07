package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;

public class AddVariableTest extends AbstractServiceTest {
    private String token;
    private MyDrive mydrive;

    @Override
    protected void populate() {
	mydrive = MyDrive.getInstance();
	new User(mydrive, "user1");
	token = (new Session(mydrive, "user1", "user1").getToken());
    }

    @Test
    public void success() {
	String variableName = "nome1";
	String value = "valor2";
	AddVariableService service = new AddVariableService(token, variableName, value);
	service.execute();
	Set<EnvVar> varList = mydrive.getSessionByToken(token).getEnvVarSet();
	boolean equal = varList.equals(service.result());
	assertTrue(variableName+"-"+value+"was added", equal);
    }

}
