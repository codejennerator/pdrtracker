package rmg.pdrtracker.pdf;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfContentStream extends PdfObject {

    public static final String BEGIN_STREAM_KEYWORD = "stream";

    public static final String END_STREAM_KEYWORD = "endstream";

    List<ContentStreamItem> contentStreamItems = new ArrayList<ContentStreamItem>(10);

    public void addStreamItem(ContentStreamItem streamItem) {
        contentStreamItems.add(streamItem);
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    public void writeToStream(PdfStream out, int indent) {

        printBeginObject(out);

        ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();

        int streamLength = 0;
        for (ContentStreamItem nextItem : contentStreamItems) {
            streamLength += nextItem.writeToStream(tmpOut);
        }

        PdfDictionary dict = new PdfDictionary();
        dict.put(new PdfName("Length"), new PdfInteger(streamLength));

        dict.writeToStream(out);
        out.writeln(BEGIN_STREAM_KEYWORD);
        out.write(tmpOut.toByteArray());
        out.writeln(END_STREAM_KEYWORD);

        printEndObject(out);

    }

}
