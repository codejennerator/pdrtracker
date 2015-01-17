package rmg.pdrtracker.job.constants;


import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

/**
 * Classifieds the different levels of damage.
 */
public  enum DamageClassifier {

    VERY_LIGHT(R.string.very_light_label, R.integer.very_light_start, R.integer.very_light_end),
    LIGHT(R.string.light_label, R.integer.light_start, R.integer.light_end),
    MODERATE(R.string.moderate_label, R.integer.moderate_start, R.integer.moderate_end),
    MEDIUM(R.string.medium_label, R.integer.medium_start, R.integer.medium_end),
    HEAVY(R.string.heavy_label, R.integer.heavy_start, R.integer.heavy_end),
    SEVERE(R.string.severe_label, R.integer.severe_start, R.integer.severe_end),
    EXTREME(R.string.extreme_label, R.integer.extreme_start, R.integer.extreme_end),
    LIMIT(R.string.limit_label, R.integer.limit_start, R.integer.limit_end),
    CEILING(R.string.ceiling_label, R.integer.ceiling_start, R.integer.ceiling_end);

    private int labelId;
    private String label;

    private final int minId;
    private Integer min;

    private final int maxId;
    private Integer max;

    DamageClassifier(int labelId, int minId, int maxId) {
        this.labelId = labelId;
        this.minId = minId;
        this.maxId = maxId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

    public int getMin() {
        if (min == null) {
            min = AppUtils.getInt(minId);
        }
        return min;
    }

    public int getMax() {
        if (max == null) {
            max = AppUtils.getInt(maxId);
        }
        return max;
    }

}
