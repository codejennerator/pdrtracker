package rmg.pdrtracker.pdf;


import java.util.ArrayList;
import java.util.List;

public class PdfArray implements PdfElement {

    List<PdfElement> pdfElementList = new ArrayList<PdfElement>(10);

    public void PdfArray() {
    }

    public void addElement(PdfElement pdfElement) {
        pdfElementList.add(pdfElement);
    }

    public <T extends PdfElement> List<T> getElements() {
        return (List<T>) pdfElementList;
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        out.write(indent, "[");
        boolean isFirst = true;
        for (PdfElement nextElement : pdfElementList) {

            if (isFirst) {
                isFirst = false;
            } else {
                out.write(" ");
            }

            nextElement.writeToStream(out);
        }
        out.write("]");
    }

}
