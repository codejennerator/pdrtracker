package rmg.pdrtracker.pdf;

public class PdfTrailer implements PdfElement {

    public static final String TRAILER_BEGIN_KEYWORD = "trailer";

    public static final String STARTXREF_BEGIN_KEYWORD = "startxref";

    private PdfXrefTable xrefTable;

    private PdfDictionary dict = new PdfDictionary();

    public PdfTrailer(PdfCatalog catalog, PdfXrefTable xrefTable) {
        this.xrefTable = xrefTable;

        dict.put("Root", new PdfObjectRef(catalog));
        dict.put("Size", new PdfInteger(xrefTable.getSize()));
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        out.writeln(TRAILER_BEGIN_KEYWORD);
        dict.writeToStream(out);

        out.writeln(STARTXREF_BEGIN_KEYWORD);
        out.write(xrefTable.getStartIndex());

    }
}
