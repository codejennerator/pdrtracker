package rmg.pdrtracker.pdf;

import junit.framework.TestCase;
import rmg.pdrtracker.pdf.*;

import java.io.*;
import java.net.Socket;

public class PdfStreamTest extends TestCase {

    // Printed document via acrobat
    // %ADO_DSC_Encoding
    // %Äåòåë§ó ÐÄÆ

    public void testAll() throws Exception {

        PdfDocument pdfDoc = new PdfDocument();
        PdfPage page = pdfDoc.newPage();

        PdfImage image = pdfDoc.newImage("X0", 564, 206, 1, new File("/Users/comaddy/foo.jpg"));

        PdfContentStream contentStream = pdfDoc.newPdfContentStream();
        page.addContent(contentStream);

//        PdfText text = new PdfText("Boo Ya 1!");
//        contentStream.addStreamItem(text);
//
        contentStream.addStreamItem(image);

        //Socket socket = new Socket("192.168.1.121", 9100);
        //OutputStream out = socket.getOutputStream();
        FileOutputStream out = new FileOutputStream("/tmp/tmp.pdf");
        PdfStream pdfStream = new PdfStream(out);
        pdfStream.write(pdfDoc);
        pdfStream.close();

    }

    public void xtestWritePostscript() throws Exception {

        Socket socket = new Socket("192.168.1.121", 9100);
        OutputStream out = socket.getOutputStream();

//        writeRaw(out, 27);
//        write(out, "%-12345X@PJL\n");
//        write(out, "@PJL SET COPIES = 1\n");
//        write(out, "@PJL ENTER LANGUAGE = POSTSCIPT\n");

        write(out, "%!PS-Adobe-3.0\r\n" +
                "     %% Example 1\r\n" +
                "\r\n" +
                "     newpath\r\n" +
                "     100 200 moveto\r\n" +
                "     200 250 lineto\r\n" +
                "     100 300 lineto\r\n" +
                "     2 setlinewidth\r\n" +
                "     stroke\r\n" +
                "     showpage");

        writeRaw(out, 4);
        out.flush();

        writeRaw(out, 27);
        write(out, "%-12345X");

        out.close();

    }

    private void writeRaw(OutputStream out, int c) throws IOException {
        out.write(c);
    }

    private void write(OutputStream out, String str) throws IOException {
        out.write(str.getBytes());
    }


}
