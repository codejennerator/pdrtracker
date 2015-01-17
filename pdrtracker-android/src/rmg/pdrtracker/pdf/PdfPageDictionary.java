package rmg.pdrtracker.pdf;

import java.util.ArrayList;
import java.util.List;

public class PdfPageDictionary extends PdfObject {

    PdfDictionary dict = new PdfDictionary();

    List<PdfPage> pageList = new ArrayList<PdfPage>(10);

    public PdfPageDictionary() {
        dict.put("Type", "Pages");
        dict.put("Kids", new PdfArray());
    }

    public void add(PdfPage pdfPage) {
        pageList.add(pdfPage);

        dict.put(new PdfName("Count"), new PdfInteger(pageList.size()));

        PdfArray kids = dict.get("Kids");
        kids.addElement(new PdfObjectRef(pdfPage));
    }

    public List<PdfPage> getPageList() {
        return pageList;
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        printBeginObject(out);
        dict.writeToStream(out);
        printEndObject(out);
    }
}
