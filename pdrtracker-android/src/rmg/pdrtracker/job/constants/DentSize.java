package rmg.pdrtracker.job.constants;


import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

/**
 * Classifies the dent sizes.
 */
public enum DentSize {

    NKL(R.string.nickle_label),
    DIME(R.string.dime_label),
    QTR(R.string.quarter_label),
    HALF(R.string.half_label);

    private int labelId;

    private String label;

    DentSize(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }
}
