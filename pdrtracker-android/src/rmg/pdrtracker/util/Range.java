package rmg.pdrtracker.util;

public class Range<T> {

    protected T startValue;

    protected T endValue;

    public Range(T startValue, T endValue) {

        this.startValue = startValue;
        this.endValue = endValue;

    }

    public T getStartValue() {
        return startValue;
    }

    public void setStartValue(T startValue) {
        this.startValue = startValue;
    }

    public T getEndValue() {
        return endValue;
    }

    public void setEndValue(T endValue) {
        this.endValue = endValue;
    }
}
