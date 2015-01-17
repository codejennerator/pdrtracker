package rmg.pdrtracker.job.damagematrix;

import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.DentSize;

import java.io.Serializable;

/**
 * Used to completely classify a quantity of dent damage.
 *
 * Example: 10 dents nickle sized on the hood: (HOOD, LIGHT, NKL)
 *
 */
public class DentDamageKey implements Serializable {

    /**
     * The area of the car the button represents.
     */
    protected CarArea carArea;

    /**
     * The level of damage the button represents.
     */
    protected DamageClassifier damageClassifier;

    /**
     * The size of the dents the button represents.
     */
    protected DentSize dentSize;

    /**
     * String representation of the this key.
     */
    protected String key;

    public DentDamageKey(CarArea carArea, DamageClassifier damageClassifier, DentSize dentSize) {

        this.carArea = carArea;
        this.damageClassifier = damageClassifier;
        this.dentSize = dentSize;

        if (dentSize != null) {
            key = carArea.toString() + ":" + damageClassifier.toString() + ":" + dentSize.toString();
        } else {
            key = carArea.toString() + ":" + damageClassifier.toString();
        }
    }

    public String toString() {
        return key;
    }

    public CarArea getCarArea() {
        return carArea;
    }

    public DamageClassifier getDamageClassifier() {
        return damageClassifier;
    }

    public DentSize getDentSize() {
        return dentSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DentDamageKey that = (DentDamageKey) o;

        if (!key.equals(that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
