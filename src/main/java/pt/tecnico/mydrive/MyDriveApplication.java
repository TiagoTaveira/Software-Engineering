package pt.tecnico.mydrive;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.CreateFileInvalidFilenameException;
import pt.tecnico.mydrive.exception.UsernameHasSpecialCharsException;
import pt.tecnico.mydrive.exception.WrongUseOfCreateHomeException;

public class MyDriveApplication {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
	System.out.println("*** Welcome to the MyDrive application! ***");
	try {
	    setup();
	} finally {
	    FenixFramework.shutdown();
	}
    }

    @Atomic
    public static void init() {
	log.trace("Init: " + FenixFramework.getDomainRoot());
	// MyDrive.getInstance().cleanup();
    }

    @Atomic
    public static void setup() {
	log.trace("Setup: " + FenixFramework.getDomainRoot());
	MyDrive md = MyDrive.getInstance();
    }

    @Atomic
    public static void xmlPrint() throws FileAlreadyExistsException, UsernameHasSpecialCharsException,
	    WrongUseOfCreateHomeException, CreateFileInvalidFilenameException {
	log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
	Document doc = MyDrive.getInstance().xmlExport();
	XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
	try {
	    xmlOutput.output(doc, new PrintStream(System.out));
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    @Atomic
    public static void xmlScan(File file) throws Exception {
	log.trace("xmlScan: " + FenixFramework.getDomainRoot());
	MyDrive md = MyDrive.getInstance();
	SAXBuilder builder = new SAXBuilder();
	try {
	    Document document = (Document) builder.build(file);
	    md.xmlImport(document.getRootElement());
	} catch (JDOMException | IOException e) {
	    e.printStackTrace();
	}
    }
}
