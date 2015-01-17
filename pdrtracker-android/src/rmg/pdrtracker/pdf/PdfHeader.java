package rmg.pdrtracker.pdf;

/**
 * Represents the PDF header, which is the first two lines of a PDF file. Responsible for indicating the version
 * of PDF specification being implemented, as well as a line of unprintable characters. The unprintable characters
 * are to help programs realized a PDF file is binary.
 */
public class PdfHeader implements PdfElement {

    // The very first line of a PDF document.
    private final static String HEADER = "%PDF-1.1";

    // The second line of the header must be a series of unprintable characters:
    //
    // %Äåòåë§ó ÐÄÆ
    // %<C4><E5><F2><E5><EB><A7><F3><A0><D0><C4><C6>
    //
    // The above characters are the ones used by OSX. We don't have to use those exact characters
    // they just have to be above ascii 127, but might as well use the same characters OSX uses.
    private static char UNPRINTABLES[] = {
            37, // % which begins a comment in PDF
            196,
            229,
            242,
            229,
            235,
            167,
            243,
            160,
            208,
            196,
            198
    };

    public void writeToStream(PdfStream pdfOut) {
        pdfOut.write(HEADER);
        pdfOut.writeln();
        pdfOut.write(UNPRINTABLES);
        pdfOut.writeln();
    }

    public void writeToStream(PdfStream out, int indent) {
        throw new RuntimeException("Indention of header is not allowed.");
    }

}
