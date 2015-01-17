package rmg.pdrtracker.job.constants;

import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

/**
 * Areas of the car that can be dented.
 */
public enum CarArea {

    HOOD(R.string.hood),
    ROOF(R.string.roof),
    DECK_LID(R.string.deck_lid),
    RT_ROOF_RAIL(R.string.rt_roof_rail),
    LT_ROOF_RAIL(R.string.lt_roof_rail),
    L_QUARTER(R.string.l_quarter),
    LR_DOOR(R.string.lr_door),
    LF_DOOR(R.string.lf_door),
    L_FENDER(R.string.l_fender),
    R_FENDER(R.string.r_fender),
    RF_DOOR(R.string.rf_door),
    RR_DOOR(R.string.rr_door),
    R_QUARTER(R.string.r_quarter),
    METAL_SUNROOF(R.string.metal_sunroof),
    OTHER(R.string.other);

    private int labelId;

    private String label;

    CarArea(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

}
