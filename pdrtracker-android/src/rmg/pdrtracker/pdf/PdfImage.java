package rmg.pdrtracker.pdf;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<
 * /Type /XObject
 * /Subtype /Image
 * /Width 564
 * /Height 206
 * /ColorSpace /DeviceRGB
 * /BitsPerComponent 8
 * /Filter /DCTDecode
 * /Length 10000
 * >>
 * <p/>
 * 564 x 206
 */
public class PdfImage extends PdfXobject implements ContentStreamItem {

    public static final String BEGIN_STREAM_KEYWORD = "stream";

    public static final String END_STREAM_KEYWORD = "endstream";

    private String name;

    PdfDictionary dict = new PdfDictionary();

    private int width;

    private int height;

    private float scale;

    private final List<byte[]> fileChunkList = new ArrayList<byte[]>();

    PdfImage(String name, int width, int height, float scale, byte[] imageBytes) {
        int totalRead = initImageFileChunkList(imageBytes);
        init(name, width, height, scale, totalRead);
    }

    PdfImage(String name, int width, int height, float scale, File imageFile) {
        int totalRead = initImageFileChunkList(imageFile);
        init(name, width, height, scale, totalRead);
    }

    public void init(String name, int width, int height, float scale, int fileSize) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.scale = scale;

        dict.put("Type", "XObject");
        dict.put("Subtype", "Image");
        dict.put("ColorSpace", "DeviceRGB");
        dict.put("Length", new PdfInteger(fileSize));
        dict.put("Width", new PdfInteger(width));
        dict.put("Height", new PdfInteger(height));
        dict.put("BitsPerComponent", new PdfInteger("8"));

        // JPEG, PNG
        dict.put("Filter", "DCTDecode");

        // Gzip
        //dict.put("Filter", "FlateDecode");

    }

    private int initImageFileChunkList(byte[] imageBytes) {
        fileChunkList.add(imageBytes);
        return imageBytes.length;
    }

    private int initImageFileChunkList(File imageFile) {
        int totalRead = 0;
        try {
            FileInputStream fin = new FileInputStream(imageFile);

            int numRead = 0;
            byte[] byteBuf = new byte[1024];
            while ((numRead = fin.read(byteBuf)) != -1) {
                byte[] fileChunk = new byte[numRead];
                for (int i = 0; i < numRead; i++) {
                    fileChunk[i] = byteBuf[i];
                }
                fileChunkList.add(fileChunk);
                totalRead += numRead;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not create PDF image.", e);
        }
        return totalRead;
    }


    @Override
    public void writeToStream(PdfStream out, int indent) {
        printBeginObject(out);
        dict.writeToStream(out);
        out.writeln(BEGIN_STREAM_KEYWORD);

        for (byte[] nextFileChunk : fileChunkList) {
            out.write(nextFileChunk);
        }

        out.writeln();
        out.writeln(END_STREAM_KEYWORD);
        printEndObject(out);
    }

    @Override
    public int writeToStream(OutputStream out) {
        return writeToStream(out, 0);
    }

    @Override
    public int writeToStream(OutputStream out, int indent) {
        int numWritten = 0;

        numWritten += StreamUtils.writeln(out, "q");

        int scaledWidth = Math.round(width * scale);
        int scaledHeight = Math.round(height * scale);

        int leftPadding = 10;
        int topPadding = 10;

        // Translate
        numWritten += StreamUtils.writeln(out, "1 0 0 1 " + leftPadding + " " + (792 - scaledHeight - topPadding) + " cm");

        // Scale
        numWritten += StreamUtils.writeln(out, Integer.toString(scaledWidth) + " 0 0 " + Integer.toString(scaledHeight) + " 0 0 cm");

        // Draw image
        numWritten += StreamUtils.writeln(out, "/" + name + " Do");

        numWritten += StreamUtils.writeln(out, "Q");

        return numWritten;
    }
}
