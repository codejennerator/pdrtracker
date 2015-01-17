package rmg.pdrtracker.pdf;

public abstract class PdfObject implements PdfElement {

    private int objectIndex;

    public static final String BEGIN_OBJECT_KEYWORD = "obj";
    public static final String END_OBJECT_KEYWORD = "endobj";


    public void setObjectIndex(int objectIndex) {
        this.objectIndex = objectIndex;
    }

    public int getObjectIndex() {
        return objectIndex;
    }

    public void printBeginObject(PdfStream out) {
        out.writeln(String.format("%d 0 " + BEGIN_OBJECT_KEYWORD + " ", objectIndex));
    }

    public void printEndObject(PdfStream out) {
        out.writeln(END_OBJECT_KEYWORD + " ");
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    abstract public void writeToStream(PdfStream out, int indent);
}
