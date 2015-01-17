package rmg.pdrtracker.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class StringOutputStream extends OutputStream {

    StringBuilder buf = new StringBuilder();

    @Override
    public void write(int i) throws IOException {
        buf.append((char) i);
    }

    public String toString() {
        return buf.toString();
    }

    public void clear() {
        buf = new StringBuilder();
    }
}
