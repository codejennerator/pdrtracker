package rmg.pdrtracker.print;

import java.io.File;

public class JpegFile {

    private File file;

    private int width;

    private int height;

    public JpegFile(File file, int width, int height) {
        this.file = file;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "JpegFile {" +
            "file=" + file.getAbsolutePath() +
            ", width=" + width +
            ", height=" + height +
        '}';
    }
}
