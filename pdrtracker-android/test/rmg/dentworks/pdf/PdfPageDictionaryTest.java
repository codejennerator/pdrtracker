package rmg.pdrtracker.pdf;

import junit.framework.TestCase;
import rmg.pdrtracker.pdf.*;

public class PdfPageDictionaryTest extends TestCase {

    public void testAll() {

        PdfPageDictionary pdfPageDict = new PdfPageDictionary();

        PdfPage page = new PdfPage(pdfPageDict, new PdfResourceDictionary());
        pdfPageDict.add(page);

        page = new PdfPage(pdfPageDict, new PdfResourceDictionary());
        pdfPageDict.add(page);

        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);
        pdfPageDict.writeToStream(pdfOut);

        String outStr = out.toString();
        System.out.println(outStr.toString());
        assertEquals("0 0 obj\n" +
                "<<\n" +
                "    /Type /Pages\n" +
                "    /Kids [0 0 R 0 0 R]\n" +
                "    /Count 2\n" +
                ">>\n" +
                "endobj\n", outStr);

        int numWritten = pdfOut.getNumWritten();
        assertEquals(outStr.length(), numWritten);

    }

}
