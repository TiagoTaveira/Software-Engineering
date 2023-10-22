package pt.tecnico.mydrive.exception;

public class ExportDocumentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public ExportDocumentException() {
        super("Error exporting document to XML");
    }
}
