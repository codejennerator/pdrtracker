package rmg.pdrtracker.pdf;

import java.util.ArrayList;
import java.util.List;

public class PdfPage extends PdfObject {

    private final PdfObjectRef parent;

    PdfDictionary dict = new PdfDictionary();

    private PdfArray contentsArray = new PdfArray();

    PdfPage(PdfPageDictionary pageDict, PdfResourceDictionary resourceDict) {

        this.parent = new PdfObjectRef(pageDict);

        dict.put(new PdfName("Type"), new PdfName("Page"));

        // US Letter Portrait 8.5 x 11 inches
        // For ever 1/72 of an inch there is one point
        // 612 points by 792 Points
        PdfArray paperSize = new PdfArray();
        dict.put(new PdfName("MediaBox"), paperSize);
        paperSize.addElement(new PdfInteger("0"));
        paperSize.addElement(new PdfInteger("0"));
        paperSize.addElement(new PdfInteger("612"));
        paperSize.addElement(new PdfInteger("792"));

        dict.put(new PdfName("Resources"), new PdfObjectRef(resourceDict));

        dict.put(new PdfName("Parent"), new PdfObjectRef(pageDict));

        dict.put(new PdfName("Contents"), contentsArray);

    }

    public void addContent(PdfContentStream contentStream) {
        contentsArray.addElement(new PdfObjectRef(contentStream));
    }

    public List<PdfContentStream> getContentStreamList() {

        List<PdfObjectRef> objRefList = contentsArray.getElements();

        List<PdfContentStream> contentStreamList = new ArrayList<PdfContentStream>(objRefList.size());

        for (PdfObjectRef nextContentStreamRef : objRefList) {
            PdfContentStream nextContentStream = nextContentStreamRef.getObject();
            contentStreamList.add(nextContentStream);
        }

        return contentStreamList;

    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        printBeginObject(out);
        dict.writeToStream(out);
        printEndObject(out);
    }
}
