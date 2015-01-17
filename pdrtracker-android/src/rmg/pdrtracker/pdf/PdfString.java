package rmg.pdrtracker.pdf;



public class PdfString {

    private String str;

    public PdfString(String str) {
        this.str = "(" + str + ")";
    }

    @Override
    public String toString() {
        return str;
    }

}
