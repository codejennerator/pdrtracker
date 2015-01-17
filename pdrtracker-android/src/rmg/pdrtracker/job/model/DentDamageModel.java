package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.damagematrix.DamageMatrixFragment;
import rmg.pdrtracker.job.damagematrix.DentDamageKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DentDamageModel implements Serializable {

    /**
     * Records all the dent damage. A missing value for a key indicates no damage for that key.
     */
    public Map<DentDamageKey, Boolean> dentDamageMap = new HashMap<DentDamageKey, Boolean>(DamageMatrixFragment.NUM_DAMAGE_BUTTONS);

    public void addDamage(DentDamageKey damageKey) {
        dentDamageMap.put(damageKey, true);
    }

    public void removeDamage(DentDamageKey damageKey) {
        dentDamageMap.remove(damageKey);
    }

    public boolean isDamageOn(DentDamageKey damageKey) {
        Boolean  isOn = dentDamageMap.get(damageKey);
        return isOn != null && isOn;
    }

}
