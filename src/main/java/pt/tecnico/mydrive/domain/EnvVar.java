package pt.tecnico.mydrive.domain;

public class EnvVar extends EnvVar_Base {
    
    public EnvVar(Session session, String name, String value) {
        setName(name);
        setValue(value);
        setSession(session);
    }
    
}
