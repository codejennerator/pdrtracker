package rmg.pdrtracker.pdf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfDocument {

    // The first object in a PDF object is at index 1
    private int nextObjectIndex = 1;

    private PdfPageDictionary pageDict;

    private PdfFontDictionary fontDict;

    private PdfXobjectDictionary xobjectDict;

    private PdfResourceDictionary resourceDict;

    private List<PdfObject> pdfObjectList = new ArrayList<PdfObject>();

    public PdfDocument() {
        pageDict = new PdfPageDictionary();
        pageDict.setObjectIndex(nextObjectIndex++);

        resourceDict = new PdfResourceDictionary();
        resourceDict.setObjectIndex(nextObjectIndex++);

        fontDict = new PdfFontDictionary();
        resourceDict.addResource("Font", fontDict);

        xobjectDict = new PdfXobjectDictionary();
        resourceDict.addResource("XObject", xobjectDict);
    }

    PdfHeader getHeader() {
        return new PdfHeader();
    }

    public PdfPageDictionary getPageDict() {
        return pageDict;
    }

    public PdfResourceDictionary getResourceDict() {
        return resourceDict;
    }

    public PdfPage newPage() {
        PdfPage page = new PdfPage(pageDict, resourceDict);
        page.setObjectIndex(nextObjectIndex++);
        pdfObjectList.add(page);
        pageDict.add(page);
        return page;
    }

    public PdfContentStream newPdfContentStream() {
        PdfContentStream contentStream = new PdfContentStream();
        contentStream.setObjectIndex(nextObjectIndex++);
        pdfObjectList.add(contentStream);
        return contentStream;
    }

    public PdfImage newImage(String name, int width, int height, float scale, byte[] bytes) {
        PdfImage pdfImage = new PdfImage(name, width, height, scale, bytes);
        pdfImage.setObjectIndex(nextObjectIndex++);
        pdfObjectList.add(pdfImage);
        xobjectDict.put(name, new PdfObjectRef(pdfImage));
        return pdfImage;
    }

    public PdfImage newImage(String name, int width, int height, float scale, File file) {
        PdfImage pdfImage = new PdfImage(name, width, height, scale, file);
        pdfImage.setObjectIndex(nextObjectIndex++);
        pdfObjectList.add(pdfImage);
        xobjectDict.put(name, new PdfObjectRef(pdfImage));
        return pdfImage;
    }

    public PdfCatalog newCatalog() {
        PdfCatalog catalog = new PdfCatalog(pageDict);
        catalog.setObjectIndex(nextObjectIndex++);
        pdfObjectList.add(catalog);
        return catalog;
    }

    public PdfXrefTable newXrefTable() {
        return new PdfXrefTable();
    }

    public PdfTrailer newTrailer(PdfCatalog catalog, PdfXrefTable xrefTable) {
        return new PdfTrailer(catalog, xrefTable);
    }

    public List<PdfObject> getObjList() {
        return pdfObjectList;
    }

}
