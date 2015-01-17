package rmg.pdrtracker.job.constants;

import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

public enum RiItemName {

    ASSEMBLY(R.string.assembly_label),
    LINER(R.string.liner_label),
    HEADLINER_WITH_SUNROOF(R.string.headliner_with_sunroof_label),
    HEADLINER_WITHOUT_SUNROOF(R.string.headliner_without_sunroof_label),
    SUNROOF_ASSEMBLY(R.string.sunroof_assembly_label),
    ROOF_RACK(R.string.roof_rack_label),
    REAR_HATCH(R.string.rear_hatch_label),
    LEFT_DRIP_MOLDING(R.string.left_drip_molding_label),
    RIGHT_DRIP_MOLDING(R.string.right_drip_molding_label),
    DVD(R.string.dvd_label),
    THIRD_BRAKE_LIGHT(R.string.third_brake_light_label),
    WINDSHIELD(R.string.windshield_label),
    CENTER_CONSOLE(R.string.center_console_label),
    HEADLIGHT(R.string.headlight_label),
    DOOR_PANEL(R.string.door_panel),
    UPPER_MOLDING(R.string.upper_molding),
    LOWER_BELT_MOLDING(R.string.lower_belt_molding),
    APPLIQUE(R.string.applique),
    TAILLIGHT(R.string.taillight);

    private int labelId;

    private String label;

    RiItemName(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }
}
