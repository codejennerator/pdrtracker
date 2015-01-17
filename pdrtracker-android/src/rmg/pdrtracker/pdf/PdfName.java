package rmg.pdrtracker.pdf;

public class PdfName implements PdfElement {

    private String name;

    public PdfName(String name) {
        this.name = "/" + name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    @Override
    public void writeToStream(PdfStream out, int indent) {
        out.write(indent, name);
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(((PdfName)o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
