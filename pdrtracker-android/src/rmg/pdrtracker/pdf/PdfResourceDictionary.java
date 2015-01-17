package rmg.pdrtracker.pdf;

public class PdfResourceDictionary extends PdfObject {

    PdfDictionary dict = new PdfDictionary();

    public void addResource(String name, PdfElement element) {
        dict.put(name, element);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        printBeginObject(out);
        dict.writeToStream(out);
        printEndObject(out);
    }
}
