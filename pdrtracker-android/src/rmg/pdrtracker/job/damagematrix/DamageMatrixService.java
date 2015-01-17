package rmg.pdrtracker.job.damagematrix;


import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.widget.Button;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.DentSize;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.model.AddInfoModel;
import rmg.pdrtracker.job.model.DentDamageModel;
import rmg.pdrtracker.job.model.PriceEstimateModel;
import rmg.pdrtracker.job.prices.DentPriceMatrix;


public class DamageMatrixService {

    private static final DamageMatrixService singleton = new DamageMatrixService();

    DentDamageModel dentDamageModel;

    AddInfoModel addInfoModel;

    private PriceEstimateModel priceEstimateModel;

    private DamageMatrixFragment damageMatrixFragment;

    private DentPriceMatrix dentPriceMatrix;

    private Activity activity;

    private DamageMatrixService() {
    }

    public static DamageMatrixService get() {
        return singleton;
    }

    public void setDamageMatrixFragment(DamageMatrixFragment damageMatrixFragment) {
        this.damageMatrixFragment = damageMatrixFragment;
        this.dentDamageModel = this.damageMatrixFragment.getModel();
        this.priceEstimateModel = this.damageMatrixFragment.getPriceEstimateModel();
        this.addInfoModel = this.damageMatrixFragment.getAddInfoModel();
        if(this.dentPriceMatrix == null){
            this.dentPriceMatrix = new DentPriceMatrix();
        }

    }

   public PriceEstimateModel getPriceEstimateModel(){
       return this.priceEstimateModel;
   }

    public AddInfoModel getAddInfoModel(){
        return this.addInfoModel;
    }

    /**
     * Refreshes the the dent damage matrix with the dent damage model.
     */
    public void updateMatrixFragment() {

        // Turn off all buttons in preparation for turning on just the buttons on in the model.
        damageMatrixFragment.turnOffAllButtons();

        // Turn on only those buttons that are on in the model.
        for (CarArea carArea : CarArea.values()) {
            for (DamageClassifier damageClassifier : DamageClassifier.values()) {
                for (DentSize dentSize : DentSize.values()) {
                    DentDamageKey nextDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);
                    if (dentDamageModel.isDamageOn(nextDamageKey)) {
                        damageMatrixFragment.turnOnButton(nextDamageKey);
                    }
                }
            }
        }

    }

    /**
     * Removes values from the AddInfo Panel and RI Panel
     */
    public void removeRiAddInfoButtons(DentDamageKey damageKey) {

        if (dentDamageModel.isDamageOn(damageKey)) {

            damageMatrixFragment.turnOffRiAddInfoButtons(damageKey);
        }

    }

    /**
     * Finds out if car area has damage.
     */
    public boolean carAreaHasDamage(CarArea carArea) {
        boolean hasDamage = false;

        for (DamageClassifier damageClassifier : DamageClassifier.values()) {
            for (DentSize dentSize : DentSize.values()) {
                DentDamageKey nextDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);
                if (dentDamageModel.isDamageOn(nextDamageKey)) {
                    hasDamage = true;
                }
            }
        }
         return hasDamage;

    }

    /**
     * Finds out if car area has damage and how much it cost if it does.
     */
    public Float carAreaDamageCost(CarArea carArea) {
        Float damageCost = 0.0F;

        for (DamageClassifier damageClassifier : DamageClassifier.values()) {
            for (DentSize dentSize : DentSize.values()) {
                DentDamageKey nextDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);
                if (dentDamageModel.isDamageOn(nextDamageKey)) {
                    damageCost = Float.parseFloat(dentPriceMatrix.getDentPrice(nextDamageKey));
                }
            }
        }
        return damageCost;

    }

    /**
     * Records damage as being present in the model.
     *
     * @param damageKey the damage to record.
     */
    public void damageOn(DentDamageKey damageKey) {

        CarArea carArea = damageKey.getCarArea();

        for (DamageClassifier damageClassifier : DamageClassifier.values()) {
            for (DentSize dentSize : DentSize.values()) {
                DentDamageKey nextDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);
                if(dentDamageModel.isDamageOn(nextDamageKey))   {

                    subtractFromEstimateTally(Float.parseFloat(dentPriceMatrix.getDentPrice(nextDamageKey)));
                }
                dentDamageModel.removeDamage(nextDamageKey);
            }
        }
        addToEstimateTally(Float.parseFloat(dentPriceMatrix.getDentPrice(damageKey)));
        setActionBarSubtitle(Float.toString(priceEstimateModel.getPriceEstimate()));
        dentDamageModel.addDamage(damageKey);

    }

    public void updateDentPriceMatrix(DentDamageKey damageKey, int newValue){

        subtractFromEstimateTally(Float.parseFloat(dentPriceMatrix.getDentPrice(damageKey)));
        dentPriceMatrix.setDentPrice(damageKey, newValue);
        addToEstimateTally(Float.parseFloat(dentPriceMatrix.getDentPrice(damageKey)));
    }

    /**
     * Records damage as not being present in the model.
     *
     * @param damageKey the damage remove from the model.
     */
    public void damageOff(DentDamageKey damageKey) {

        subtractFromEstimateTally(Float.parseFloat(dentPriceMatrix.getDentPrice(damageKey)));
        setActionBarSubtitle(Float.toString(priceEstimateModel.getPriceEstimate()));
        dentDamageModel.removeDamage(damageKey);
    }

    public void setActionBarSubtitle(String subtitle){
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        try{
            this.activity.getActionBar().setSubtitle(currencyFormatter.format(Double.parseDouble(subtitle)));
        }catch (NumberFormatException e){

            System.out.println("Number Format Exception trying to set subtitle. "+e);
            this.activity.getActionBar().setSubtitle(subtitle);
        }
    }

    public void setTheActivity(Activity activity){

        this.activity = activity;
    }

    public void addToEstimateTally(Float price){
        System.out.println(price);
        priceEstimateModel.setPriceEstimate(priceEstimateModel.getPriceEstimate()+price);
    }

    public void subtractFromEstimateTally(Float price){

        priceEstimateModel.setPriceEstimate(priceEstimateModel.getPriceEstimate()-price);
    }

    public Float getEstimateTally(){

        return priceEstimateModel.getPriceEstimate();
    }

    public void updateAddInfoItemModel(AddInfoItemModel addInfoItemModel){

        addInfoModel.addAddInfoItem(addInfoItemModel);
    }

    public void showAreYouSureDeleteDialog(DentDamageKey damageKey){

        damageMatrixFragment.showAreYouSureDeleteDialog(damageKey);

    }

    public void showAreYouSureChangeDialog(DentDamageKey damageKey){

        damageMatrixFragment.showAreYouSureChangeDialog(damageKey);

    }


    public void editDamageValueDialog(DentDamageKey damageKey, Button selectedButton){

        damageMatrixFragment.editDamageValueDialog(damageKey, selectedButton);

    }

    public DentPriceMatrix getDentPriceMatrix(){

        return dentPriceMatrix;

    }

    public void setDentPriceMatrix(DentPriceMatrix priceMatrix){

        dentPriceMatrix = priceMatrix;
    }
}
