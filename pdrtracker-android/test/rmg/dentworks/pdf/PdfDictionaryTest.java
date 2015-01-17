package rmg.pdrtracker.pdf;

import junit.framework.TestCase;

public class PdfDictionaryTest extends TestCase {

    public void testSimpleValues() {

        PdfDictionary pdfDict = new PdfDictionary();
        pdfDict.put("name1", "value1");
        pdfDict.put("name2", "value2");
        pdfDict.put("name3", "value3");

        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);
        pdfDict.writeToStream(pdfOut);

        int numWritten = pdfOut.getNumWritten();
        String outStr = out.toString();
        assertEquals(outStr.length(), numWritten);

        System.out.println();
        System.out.println("Simple dictionary");
        System.out.print(outStr);

    }


    public void testSimpleValuesIndented() {
        PdfDictionary pdfDict = new PdfDictionary();
        pdfDict.put("name1", "value1");
        pdfDict.put("name2", "value2");
        pdfDict.put("name3", "value3");

        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);
        pdfDict.writeToStream(pdfOut, 1);

        int numWritten = pdfOut.getNumWritten();
        String outStr = out.toString();
        assertEquals(outStr.length(), numWritten);

        System.out.println();
        System.out.println("Simple dictionary indented");
        System.out.print(outStr);

    }

    public void testDictInDict() {

        PdfDictionary pdfDict1 = new PdfDictionary();
        pdfDict1.put("name1", "value1");
        pdfDict1.put("name2", "value2");
        pdfDict1.put("name3", "value3");

        PdfDictionary pdfDict2 = new PdfDictionary();
        pdfDict1.put("dict2", pdfDict2);
        pdfDict2.put("name4", "value4");
        pdfDict2.put("name5", "value5");
        pdfDict2.put("name6", "value6");

        StringOutputStream out = new StringOutputStream();
        PdfStream pdfOut = new PdfStream(out);
        pdfDict1.writeToStream(pdfOut);

        int numWritten = pdfOut.getNumWritten();
        String outStr = out.toString();
        assertEquals(outStr.length(), numWritten);

        System.out.println();
        System.out.println("Dictionary nested in another dictionary.");
        System.out.print(outStr);

    }


}
