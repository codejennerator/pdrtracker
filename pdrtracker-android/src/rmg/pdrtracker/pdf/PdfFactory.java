package rmg.pdrtracker.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import rmg.pdrtracker.print.JpegFile;
import rmg.pdrtracker.print.LayoutListener;
import rmg.pdrtracker.print.RmgWebView;

import java.io.File;
import java.io.FileOutputStream;

public class PdfFactory {

    public static final String ENCODING = "utf-8";

    public static final String HTML_MIME_TYPE = "text/html";

    // Width of a 8.5x11 page at 72dpi
    private final static int PAGE_WIDTH = 612;

    // Height of a 8.5x11 page at 72dpi
    private final static int PAGE_HEIGHT = 792;


    public static final int TOP_PADDING = 10;

    public static final int LEFT_PADDING = 10;

    public static final int SCALE_FACTOR = 2;

    public RmgWebView createWebViewFromHtml(Context context, final String html, LayoutListener layoutListener) {
        final RmgWebView webView = new RmgWebView(context);
        webView.addLayoutListener(layoutListener);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.setPadding(0, 0, 0, 0);

        webView.loadData(html, HTML_MIME_TYPE, ENCODING);
        webView.layout(0, 0, (PAGE_WIDTH - LEFT_PADDING) * SCALE_FACTOR, PAGE_HEIGHT);

        return webView;
    }

    public File createPdfFromWebView(WebView webView, String fileName) {
        try {

            String fileNameRoot = getFileNameRoot(fileName);
            JpegFile imageFile = createJpegFromWebView(webView, fileNameRoot + ".jpg");

            PdfDocument pdfDoc = new PdfDocument();
            PdfPage page = pdfDoc.newPage();
            PdfImage image = pdfDoc.newImage("X0", imageFile.getWidth(), imageFile.getHeight(), 1F / SCALE_FACTOR, imageFile.getFile());

            PdfContentStream contentStream = pdfDoc.newPdfContentStream();
            page.addContent(contentStream);

            contentStream.addStreamItem(image);

            File pdfFile = new File(imageFile.getFile().getParent(), fileName);
            FileOutputStream out = new FileOutputStream(pdfFile);
            PdfStream pdfStream = new PdfStream(out);
            pdfStream.write(pdfDoc);
            pdfStream.close();

            return pdfFile;
        } catch (Exception e) {
            throw new RuntimeException("Could not create pdf file.", e);
        }
    }

    private JpegFile createJpegFromWebView(WebView webView, String fileName) {

        Picture picture = webView.capturePicture();

        // We allow for 10px of top padding on a 792px page
        Bitmap bitmap = createBitmapFromPicture(picture, 0, (PAGE_HEIGHT - TOP_PADDING) * SCALE_FACTOR);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not created jpeg file from webview.", e);
        }

        JpegFile jpegFile = new JpegFile(file, bitmap.getWidth(), bitmap.getHeight());
        Log.d("PdrTracker", "Created file: " + jpegFile.toString());

        return jpegFile;

    }

    private static Bitmap createBitmapFromPicture(Picture picture, int startRow, int numRows) {
        Bitmap fullBitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fullBitmap);
        canvas.drawPicture(picture);
        System.out.println("picture height "+picture.getHeight()+"start Row "+startRow+"numRows "+numRows);
        return Bitmap.createBitmap(fullBitmap, 0, startRow, picture.getWidth(), picture.getHeight());
       // jenn return Bitmap.createBitmap(fullBitmap, 0, startRow, picture.getWidth(), startRow + numRows);
    }

    private String getFileNameRoot(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }

        return fileName.substring(0, dotIndex);
    }

}
