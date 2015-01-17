package rmg.pdrtracker.pdf;

public class PdfInteger implements PdfElement {

    String num;

    PdfInteger(int num) {
        this.num = Integer.toString(num);
    }

    PdfInteger(String num) {
        this.num = num;
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        out.write(num);
    }

}
