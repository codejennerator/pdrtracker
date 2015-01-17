package rmg.pdrtracker.pdf;

import java.util.ArrayList;
import java.util.List;

public class PdfXrefTable implements PdfElement {

    public static final String XREF_BEGIN_KEYWORD = "xref";

    private long startIndex;

    private List<Long> objectFileIndexList = new ArrayList<Long>();

    public String getStartIndex() {
        return Long.toString(startIndex);
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public void addObjectFileIndex(long fileIndex) {
        objectFileIndexList.add(fileIndex);
    }

    public int getSize() {
        return objectFileIndexList.size() + 1;
    }

    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {

        out.writeln(XREF_BEGIN_KEYWORD);

        out.writeln("0 " + getSize());

        // Trailing space has to be there for eprint reader to work
        out.writeln("0000000000 65535 f ");

        for (Long nextIndex : objectFileIndexList) {
            // Trailing space has to be there for eprint reader to work
            out.writeln(getNumberPadded(nextIndex) + " 00000 n ");
        }

        //out.writeln();

    }

    private String getNumberPadded(long num) {
        String numStr = Long.toString(num);
        int numToPad = 10 - numStr.length();

        String padding = "";
        for (int i = 0; i < numToPad; i++) {
            padding += "0";
        }

        return padding + numStr;
    }

}
