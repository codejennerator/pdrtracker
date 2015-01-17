package rmg.pdrtracker.pdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfText implements ContentStreamItem {

    List<String> textList = new ArrayList<String>(10);

    public PdfText(String text) {
        textList.add(text);
    }

    public void addText(String text) {
        textList.add(text);
    }

    public int writeToStream(OutputStream out) {
        return writeToStream(out, 0);
    }

    public int writeToStream(OutputStream out, int indent) {
        int numWritten = 0;

        numWritten += StreamUtils.writeln(out, "1. 0. 0. 1. 40. 700. cm");
        numWritten += StreamUtils.writeln(out, "BT");
        numWritten += StreamUtils.writeln(out, "/F0 14. Tf");
        numWritten += StreamUtils.writeln(out, "14 TL");
        for (String nextText : textList) {
            numWritten += StreamUtils.writeln(out, new PdfString(nextText ).toString() + " Tj T*");
        }
        numWritten += StreamUtils.writeln(out, "ET");

        return numWritten;
    }

}
