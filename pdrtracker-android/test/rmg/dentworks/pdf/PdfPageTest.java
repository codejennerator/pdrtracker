package rmg.pdrtracker.pdf;

import junit.framework.TestCase;
import rmg.pdrtracker.pdf.*;

public class PdfPageTest extends TestCase {

    public void testAll() {


        PdfPageDictionary pdfPageDict= new PdfPageDictionary();
        PdfResourceDictionary pdfResourceDict = new PdfResourceDictionary();

        PdfPage pdfPage = new PdfPage(pdfPageDict, pdfResourceDict);

        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);
        pdfPage.writeToStream(pdfOut);

        String outStr = out.toString();
        System.out.println(outStr);
        assertEquals("0 0 obj\n" +
                "<<\n" +
                "    /Type /Page\n" +
                "    /MediaBox [0 0 612 792]\n" +
                "    /Resources 0 0 R\n" +
                "    /Parent 0 0 R\n" +
                "    /Contents []\n" +
                ">>\n" +
                "endobj\n", outStr);

        int numWritten = pdfOut.getNumWritten();
        assertEquals(outStr.length(), numWritten);


    }

}
