package rmg.pdrtracker.pdf;

public class PdfObjectRef implements PdfElement {

    PdfObject pdfObject;

    PdfObjectRef(PdfObject pdfObject) {
        this.pdfObject = pdfObject;
    }

    public void setObject(PdfObject pdfObject) {
        this.pdfObject = pdfObject;
    }

    public <T extends PdfElement> T getObject() {
        return (T) pdfObject;
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        out.write(indent, pdfObject.getObjectIndex() + " 0 R");
    }


}
