package rmg.pdrtracker.job.constants;

import rmg.pdrtracker.R;
import rmg.pdrtracker.util.AppUtils;

public enum AddInfo {

    ALUMINUM_PANEL(R.string.aluminum_panel_label),
    DOUBLE_PANEL(R.string.double_panel_label),
    OVERSIZED_DENT(R.string.oversized_dent_label),
    SHARP_DENT(R.string.sharp_dent_label),
    OTHER(R.string.other_label);

    private int labelId;

    private String label;

    AddInfo(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

}