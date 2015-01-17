package rmg.pdrtracker.pdf;

import java.util.*;

/**
 * Represents a PDF Dictionary. Dictionaries can contain other dictionaries.
 *
 * The following example is of three nested dictionaries:
 *
 * <<
 *     /key1
 *     <<
 *         /key1
 *         <<
 *             /key1 /value1
 *             /key2 /value2
 *             /key3 /value3
 *         >>
 *     >>
 * >>
 */
public class PdfDictionary extends PdfObject {

    private final static String OPEN_DICTIONARY = "<<";

    private final static String CLOSE_DICTIONARY = ">>";

    private Map<PdfName, PdfElement> map = new LinkedHashMap<PdfName, PdfElement>(10);

    public PdfDictionary() {
    }

    public void put(String name, String value) {
        map.put(new PdfName(name), new PdfName(value));
    }

    public void put(String name, PdfElement value) {
        map.put(new PdfName(name), value);
    }

    public void put(PdfName name, PdfElement value) {
        map.put(name, value);
    }

    public <T extends PdfElement> T get(String name) {
        return (T) map.get(new PdfName(name));
    }

    public <T extends PdfElement> T get(PdfName name) {
        return (T) map.get(name);
    }

    public <T extends PdfElement> List<T> getValueList() {
        List<T> valueList = new ArrayList<T>();
        Collection<T> valueCollection = (Collection<T>) map.values();
        for (T nextValue : valueCollection) {
            valueList.add(nextValue);
        }
        return valueList;
    }

    @Override
    public void writeToStream(PdfStream out) {
        writeToStream(out, 0);
    }

    public void writeToStream(PdfStream out, int indent) {

        out.writeln(indent, OPEN_DICTIONARY);

        // write name value pairs
        Set<PdfName> keys = map.keySet();
        for (PdfName key : keys) {

            out.write(indent + 1, key.toString());
            PdfElement nextElement = map.get(key);

            if (nextElement instanceof PdfDictionary) {
                out.writeln();
                nextElement.writeToStream(out, indent + 1);
            } else {
                out.write(" ");
                nextElement.writeToStream(out);
                out.writeln();
            }

        }

        out.writeln(indent, CLOSE_DICTIONARY);

    }

}
