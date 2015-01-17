package rmg.pdrtracker.job.damagematrix;


import android.content.Context;
import android.widget.LinearLayout;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.DentSize;
import rmg.pdrtracker.R;

import java.util.HashMap;
import java.util.Map;

public class CarAreaPriceRow extends LinearLayout {

    Map<DentDamageKey, DentDamageButton> costButtonMap = new HashMap<DentDamageKey, DentDamageButton>(DamageClassifier.values().length * DentSize.values().length);

    public CarAreaPriceRow(Context context) {
        super(context);

        setBackgroundColor(getResources().getColor(R.drawable.blue1));
        setPadding(0, 0, 0, 0);
        offsetTopAndBottom(0);
        setOrientation(LinearLayout.HORIZONTAL);

    }

    public void turnOn(DentDamageButton button) {
        costButtonMap.put(button.getDamageKey(), button);
    }

    public void turnOff(DentDamageButton button) {
        costButtonMap.remove(button.getDamageKey());
    }

}
