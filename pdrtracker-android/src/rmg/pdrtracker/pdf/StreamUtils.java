package rmg.pdrtracker.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class StreamUtils {

    private static int BYTE_MASK = 0x00FF;

    private static String INDENT_ONE = "    ";

    private static String[] PADDING = new String[10];

    static {
        PADDING[0] = "";
        PADDING[1] = INDENT_ONE;
        for (int i = 2; i < PADDING.length; i++) {
            PADDING[i] = PADDING[i - 1] + INDENT_ONE;
        }
    }

    public static int write(OutputStream out, String str) {
        try {
            byte[] bytes = str.getBytes();
            out.write(bytes);
            return bytes.length;
        } catch (IOException e) {
            throw new RuntimeException("Could not write string to output stream", e);
        }
    }

    public static int write(OutputStream out, byte[] bytes) {
        try {
            out.write(bytes);
            return bytes.length;
        } catch (IOException e) {
            throw new RuntimeException("Could not write bytes to output stream", e);
        }
    }

    public static int write(OutputStream out, char[] chars) {
        try {
            for (char nextChar : chars) {
                out.write(nextChar);
            }
            return chars.length;
        } catch (IOException e) {
            throw new RuntimeException("Could not write chars to output stream", e);
        }
    }

    public static int writeln(OutputStream out) {
        try {
            out.write(10); // \r
            return 1;
            //out.write(13); // \r
            //out.write(10); // \n
            //return 2;
        } catch (IOException e) {
            throw new RuntimeException("Could not write end of line to output stream", e);
        }
    }

    public static int write(OutputStream out, int indent, String str) {
        return write(out, PADDING[indent] + str);
    }

    public static int writeln(OutputStream out, String str) {
        int numBytesInStr = write(out, str);
        numBytesInStr += writeln(out);
        return numBytesInStr;
    }

    public static int writeln(OutputStream out, int indent, String str) {
        int numBytesInLine = write(out, PADDING[indent] + str);
        numBytesInLine += writeln(out);
        return numBytesInLine;
    }

}
