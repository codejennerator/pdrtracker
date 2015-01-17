package rmg.pdrtracker.pdf;

public interface PdfElement {

    void writeToStream(PdfStream out);

    void writeToStream(PdfStream out, int indent);

}
