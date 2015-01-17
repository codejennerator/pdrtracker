package rmg.pdrtracker.pdf;

public class PdfCatalog extends PdfObject {

    private PdfDictionary dict = new PdfDictionary();

    PdfCatalog(PdfPageDictionary pageDict) {
        dict.put("Pages", new PdfObjectRef(pageDict));
        dict.put("Type", "Catalog");
    }


    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        printBeginObject(out);
        dict.writeToStream(out);
        printEndObject(out);
    }

}
