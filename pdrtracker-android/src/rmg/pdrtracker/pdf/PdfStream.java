package rmg.pdrtracker.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PdfStream {

    private OutputStream out;

    int numWritten = 0;

    public PdfStream(OutputStream out) {
        this.out = out;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public void write(PdfDocument doc) {

        // PDF Header
        PdfHeader pdfHeader = doc.getHeader();
        pdfHeader.writeToStream(this);

        PdfXrefTable pdfXrefTable = doc.newXrefTable();

        // PDF Page Dictionary
        PdfPageDictionary pdfPageDict = doc.getPageDict();
        pdfXrefTable.addObjectFileIndex(numWritten);
        pdfPageDict.writeToStream(this);

        // PDF Resource Dictionary
        PdfResourceDictionary pdfResourceDict = doc.getResourceDict();
        pdfXrefTable.addObjectFileIndex(numWritten);
        pdfResourceDict.writeToStream(this);

        // Object Catalog
        PdfCatalog pdfCatalog = doc.newCatalog();

        List<PdfObject> objList = doc.getObjList();
        for (PdfObject nextObj : objList) {
            pdfXrefTable.addObjectFileIndex(numWritten);
            nextObj.writeToStream(this);
        }

        pdfXrefTable.setStartIndex(numWritten);
        pdfXrefTable.writeToStream(this);

        PdfTrailer trailer = doc.newTrailer(pdfCatalog, pdfXrefTable);
        trailer.writeToStream(this);
        writeln();

        writeln("%%EOF");

    }

    void write(String str) {
        numWritten += StreamUtils.write(out, str);
    }

    void write(byte[] bytes) {
        numWritten += StreamUtils.write(out, bytes);
    }

    public void write(char[] chars) {
        numWritten += StreamUtils.write(out, chars);
    }

    void writeln() {
        numWritten += StreamUtils.writeln(out);
    }

    public void write(int indent, String str) {
        numWritten += StreamUtils.write(out, indent, str);
    }

    public void writeln(String str) {
        numWritten += StreamUtils.writeln(out, str);
    }

    public void writeln(int indent, String str) {
        numWritten += StreamUtils.writeln(out, indent, str);
    }


    public void close() {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumWritten() {
        return numWritten;
    }

}
