package rmg.pdrtracker.job.damagematrix;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import rmg.pdrtracker.job.model.PriceEstimateModel;
import rmg.pdrtracker.job.prices.DentPriceMatrix;
import rmg.pdrtracker.R;

import static rmg.pdrtracker.util.AppUtils.toDip;

public class DentDamageButton extends Button {


    int BUTTON_WIDTH = getResources().getInteger(R.integer.cost_button_width);

    int TEXT_SIZE = getResources().getInteger(R.integer.cost_button_text_size);

    int TEXT_COLOR = getResources().getColor(R.drawable.cost_button_text_color);

    int ON_COLOR = getResources().getColor(R.drawable.cost_button_on_color);

    int OFF_COLOR = getResources().getColor(R.drawable.cost_button_off_color);

    /**
     * Tracks whether the button is recording damage or not. If the button is on then it's price will account for
     * damage on the final invoice.
     */
    private boolean isOn;
    private DentPriceMatrix dentPriceMatrix;
    private PriceEstimateModel priceEstimateModel;


    /**
     * The damage this button represents.
     */
    private DentDamageKey damageKey;

    DamageMatrixService damageMatrixService = DamageMatrixService.get();

    public DentDamageButton(Context context, DentDamageKey damageKey, final Activity activity) {
        super(context);
        this.dentPriceMatrix = damageMatrixService.getDentPriceMatrix();
        this.damageKey = damageKey;

        setTextSize(TEXT_SIZE);
        setWidth(toDip(BUTTON_WIDTH));
        setMinimumWidth(toDip(BUTTON_WIDTH));

        setPadding(0, 0, 0, 0);
        offsetTopAndBottom(0);

        setTextColor(TEXT_COLOR);
        setBackgroundColor(OFF_COLOR);

        setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                isOn = !isOn;

                if (isOn) {

                    if(damageMatrixService.carAreaHasDamage(DentDamageButton.this.damageKey.getCarArea())){
                        damageMatrixService.showAreYouSureChangeDialog(DentDamageButton.this.damageKey);
                    }
                    else{

                        dentPriceMatrix.getDentPrice(DentDamageButton.this.damageKey);
                        damageMatrixService.damageOn(DentDamageButton.this.damageKey);
                    }

                } else {

                   // damageMatrixService.removeRiAddInfoButtons(DentDamageButton.this.damageKey);
                   // damageMatrixService.damageOff(DentDamageButton.this.damageKey);
                    damageMatrixService.showAreYouSureDeleteDialog(DentDamageButton.this.damageKey);
                }

                damageMatrixService.updateMatrixFragment();

            }

        });
        setOnLongClickListener(new Button.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                isOn = !isOn;

                if (isOn) {

                    dentPriceMatrix.getDentPrice(DentDamageButton.this.damageKey);
                    damageMatrixService.damageOn(DentDamageButton.this.damageKey);
                    damageMatrixService.editDamageValueDialog(DentDamageButton.this.damageKey, DentDamageButton.this);

                } else {

                    // damageMatrixService.removeRiAddInfoButtons(DentDamageButton.this.damageKey);
                    // damageMatrixService.damageOff(DentDamageButton.this.damageKey);
                    damageMatrixService.editDamageValueDialog(DentDamageButton.this.damageKey, DentDamageButton.this);
                }

                return true;

            }
        });

    }

    public void turnOn() {
        isOn = true;
        setBackgroundColor(ON_COLOR);
    }

    public void turnOff() {
        isOn = false;
        setBackgroundColor(OFF_COLOR);
    }

    public DentDamageKey getDamageKey() {
        return damageKey;
    }

}
