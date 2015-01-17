package rmg.pdrtracker.pdf;


import java.io.OutputStream;

public interface ContentStreamItem {

    public int writeToStream(OutputStream out);

    public int writeToStream(OutputStream out, int indent);

}
