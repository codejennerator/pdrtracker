package rmg.pdrtracker.pdf;


import junit.framework.TestCase;

public class PdfHeaderTest extends TestCase {

    public void testAll() {
        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);

        PdfHeader pdfHeader = new PdfHeader();
        pdfHeader.writeToStream(pdfOut);

        assertEquals("%PDF-1.1\n%Äåòåë§ó ÐÄÆ\n", out.toString());
    }

}
