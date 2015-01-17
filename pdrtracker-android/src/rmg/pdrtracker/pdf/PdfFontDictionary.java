package rmg.pdrtracker.pdf;

//dict.put(new PdfName("Font"), fontDict);
public class PdfFontDictionary extends PdfDictionary {

    public PdfFontDictionary() {

        PdfDictionary fontDefDict = new PdfDictionary();
        put(new PdfName("F0"), fontDefDict);
        fontDefDict.put(new PdfName("Type"), new PdfName("Font"));
        fontDefDict.put(new PdfName("BaseFont"), new PdfName("Helvetica"));
        fontDefDict.put(new PdfName("Subtype"), new PdfName("Type1"));

    }



}
