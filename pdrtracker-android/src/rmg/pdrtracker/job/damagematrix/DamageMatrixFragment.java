package rmg.pdrtracker.job.damagematrix;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.DentSize;
import rmg.pdrtracker.job.damagematrix.additionalinfo.AddInfoPanel;
import rmg.pdrtracker.job.dialogs.AreYouSureChangeDialogFragment;
import rmg.pdrtracker.job.dialogs.AreYouSureDeleteDialogFragment;
import rmg.pdrtracker.job.dialogs.EditDamageValueDialogFragment;
import rmg.pdrtracker.job.model.*;
import rmg.pdrtracker.job.prices.DentPriceMatrix;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.util.Range;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.damagematrix.ri.RiPanel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DamageMatrixFragment extends Fragment implements AreYouSureChangeDialogFragment.DialogClickListener, AreYouSureDeleteDialogFragment.DialogClickListener, EditDamageValueDialogFragment.DialogClickListener {

    /**
     * The width of the screen calculated for the current device.
     */
    public static int SCREEN_WIDTH;

    /**
     * The total number of car labels that will be displayed. The first three car areas are displayed twice because
     * they are able to have damage classified as severe - ceiling
     */
    public final static int NUM_CAR_AREA_LABELS = (CarArea.values().length + 3);

    /**
     * The total number of buttons that will be displayed on the dent matrix. This is used to size maps that hold
     * button state information to the optimal size.
     */
    public final static int NUM_DAMAGE_BUTTONS = NUM_CAR_AREA_LABELS * DamageClassifier.values().length * DentSize.values().length;

    /**
     * The total number of header columns to display across the top of the matrix including the first column that contains
     * the label that describes what the headers represent.
     */
    public static final int NUM_HEADER_COLS = 6;

    int DAMAGE_CLASSIFIER_FONT_SIZE;

    int DAMAGE_CLASSIFIER_ROW_HEIGHT;

    int DENT_SIZE_TEXT_SIZE;

    int DENT_SIZE_ROW_HEIGHT;

    int DENT_BUTTON_ROW_HEIGHT;

    int LABEL_BACKGROUND;

    int LABEL_FOREGROUND;

    private JobModel jobModel;

    private RelativeLayout root;

    private DamageMatrixService damageMatrixService = DamageMatrixService.get();

    private Map<DentDamageKey, DentDamageButton> buttonMap = new HashMap<DentDamageKey, DentDamageButton>(NUM_DAMAGE_BUTTONS);

    private Map<DentDamageKey, CarAreaLabel> carAreaLabelMap = new HashMap<DentDamageKey, CarAreaLabel>(NUM_CAR_AREA_LABELS);

    private RiPanel riPanel;

    private AddInfoPanel addInfoPanel;

    private boolean isInitializationComplete;

    private DentDamageKey currentDentDamageKey;

    private Button currentSelectedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initModel();
        damageMatrixService.setTheActivity(getActivity());
        DAMAGE_CLASSIFIER_FONT_SIZE = getResources().getInteger(R.integer.damage_classifier_font_size);
        DAMAGE_CLASSIFIER_ROW_HEIGHT = getResources().getInteger(R.integer.damage_classifier_row_height);
        DENT_SIZE_TEXT_SIZE = getResources().getInteger(R.integer.dent_size_text_size);
        DENT_SIZE_ROW_HEIGHT = getResources().getInteger(R.integer.dent_size_row_height);
        DENT_BUTTON_ROW_HEIGHT = getResources().getInteger(R.integer.dent_button_row_height);
        LABEL_BACKGROUND = getResources().getColor(R.drawable.label_background);
        LABEL_FOREGROUND = getResources().getColor(R.drawable.label_foreground);

        SCREEN_WIDTH = AppUtils.getScreenWidth();

        // Inflate the layout for this fragment
        root = (RelativeLayout) inflater.inflate(R.layout.matrix_layout, container, false);

        riPanel = (RiPanel) root.findViewById(R.id.ri_panel);

        addInfoPanel = (AddInfoPanel) root.findViewById(R.id.addInfo_panel);

        Context context = root.getContext();

        LinearLayout verticalContainer = new LinearLayout(context);
        root.addView(verticalContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        verticalContainer.setOrientation(LinearLayout.VERTICAL);

        // First group of damage cells for very light - heavy
        createDamageGroup(context, verticalContainer, new Range<CarArea>(CarArea.HOOD, CarArea.OTHER),
                new Range<DamageClassifier>(DamageClassifier.VERY_LIGHT, DamageClassifier.HEAVY), 0);

        // Second group of damage cells for severe - ceiling
        createDamageGroup(context, verticalContainer, new Range<CarArea>(CarArea.HOOD, CarArea.DECK_LID),
                new Range<DamageClassifier>(DamageClassifier.SEVERE, DamageClassifier.CEILING), 20);

        damageMatrixService.updateMatrixFragment();

        isInitializationComplete = true;

        return root;

    }

    private void initModel() {
        ModelService modelService = ModelService.getInstance();
        jobModel = modelService.getJobModel();
        damageMatrixService.setDamageMatrixFragment(this);
    }

    public DentDamageModel getModel() {
        return jobModel.getDentDamageModel();
    }

    public PriceEstimateModel getPriceEstimateModel() {
        return jobModel.getPriceEstimateModel();
    }

    public AddInfoModel getAddInfoModel() {
        return jobModel.getAddInfoModel();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveModel();
        ModelService modelService = ModelService.getInstance();
        modelService.saveModelState(savedInstanceState, jobModel);


    }


    public void saveModel() {
        // Model is updated in real time nothing to do.
    }

    /**
     * Turns off all buttons in the matrix.
     */
    public void turnOffAllButtons() {
        Set<DentDamageKey> dentDamageKeys = buttonMap.keySet();

        for (DentDamageKey dentDamageKey : dentDamageKeys) {
            DentDamageButton button = buttonMap.get(dentDamageKey);
            button.turnOff();
        }
    }

    /**
     * Turns off all buttons in the matrix.
     */
    public void turnOffRiAddInfoButtons(DentDamageKey dentDamageKey) {
        DentDamageButton button = buttonMap.get(dentDamageKey);
        if (button != null) {
            button.turnOn();

            CarAreaLabel carAreaLabel = carAreaLabelMap.get(new DentDamageKey(dentDamageKey.getCarArea(), dentDamageKey.getDamageClassifier(), null));
            carAreaLabel.turnOffRI();
            carAreaLabel.turnOffAddInfo();
        }
    }



    /**
     * Turns on a single button in the matrix.
     *
     * @param dentDamageKey the key to decide which button to turn on.
     */
    public void turnOnButton(DentDamageKey dentDamageKey) {
        DentDamageButton button = buttonMap.get(dentDamageKey);
        if (button != null) {
            button.turnOn();

            CarAreaLabel carAreaLabel = carAreaLabelMap.get(new DentDamageKey(dentDamageKey.getCarArea(), dentDamageKey.getDamageClassifier(), null));
            carAreaLabel.turnOnRi();
            carAreaLabel.turnOnAddInfo();
        }
    }

    /**
     * Creates one of the two damage groups.
     *
     * Typically the two damage classifier groups would be:
     *     hood - other, very light - heavy
     *     hood - deck lid, severe - ceiling
     *
     * This could be changed if desired.
     */
    private void createDamageGroup(Context context, ViewGroup parent, Range<CarArea> carAreaRange, Range<DamageClassifier> damageClassifierRange, int paddingTop) {

        LinearLayout verticalContainer = new LinearLayout(context);
        parent.addView(verticalContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        verticalContainer.setOrientation(LinearLayout.VERTICAL);
        verticalContainer.setPadding(0, paddingTop, 0, 0);

        LinearLayout headerRow = new LinearLayout(context);
        verticalContainer.addView(headerRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headerRow.setOrientation(LinearLayout.VERTICAL);

        createDamageClassHeaderRow(context, headerRow, damageClassifierRange);
        createDentSizeHeaderRow(context, headerRow, damageClassifierRange);
        createDamageButtonRows(context, verticalContainer, carAreaRange, damageClassifierRange);

    }


    /**
     * Creates the header row that contains a range of damage classifiers.
     *
     * Something like:
     *
     * VERY LIGHT LIGHT MODERATE MEDIUM HEAVY
     */
    private void createDamageClassHeaderRow(Context context, ViewGroup parent, Range<DamageClassifier> damageClassifierRange) {

        int width = SCREEN_WIDTH / NUM_HEADER_COLS;
        int widthRemainder = SCREEN_WIDTH % NUM_HEADER_COLS;

        LinearLayout severityClassLabelRow = new LinearLayout(context);
        parent.addView(severityClassLabelRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        severityClassLabelRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(context);
        severityClassLabelRow.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(DAMAGE_CLASSIFIER_ROW_HEIGHT)));
        textView.setText(R.string.severity_class_label);
        textView.setTextSize(DAMAGE_CLASSIFIER_FONT_SIZE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundColor(LABEL_BACKGROUND);
        textView.setTextColor(LABEL_FOREGROUND);
        textView.setMinimumWidth(width + widthRemainder);

        int startIndex = damageClassifierRange.getStartValue().ordinal();
        int endIndex = damageClassifierRange.getEndValue().ordinal() + 1;
        for (int i = startIndex; i < endIndex; i++) {
            textView = new TextView(context);
            severityClassLabelRow.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(DAMAGE_CLASSIFIER_ROW_HEIGHT)));
            DamageClassifier damageClassifier = DamageClassifier.values()[i];
            textView.setText(damageClassifier.getLabel() + ": " + damageClassifier.getMin() + " to " + damageClassifier.getMax());
            textView.setTextSize(DAMAGE_CLASSIFIER_FONT_SIZE);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setBackgroundColor(LABEL_BACKGROUND);
            textView.setTextColor(LABEL_FOREGROUND);
            textView.setMinimumWidth(width);
        }

    }

    /**
     * Creates the header row that contains the dent sizes for a range of damage classifiers.
     *
     * Something like:
     *
     * DIME NKL QTR HALF DIME NKL QTR HALF DIME NKL QTR HALF DIME NKL QTR HALF DIME NKL QTR HALF
     */
    private void createDentSizeHeaderRow(Context context, ViewGroup parent, Range<DamageClassifier> damageClassifierRange) {

        int width = SCREEN_WIDTH / NUM_HEADER_COLS;
        int widthRemainder = SCREEN_WIDTH % NUM_HEADER_COLS;

        LinearLayout dentSizeLabelRow = new LinearLayout(context);
        parent.addView(dentSizeLabelRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dentSizeLabelRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(context);
        dentSizeLabelRow.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(DENT_SIZE_ROW_HEIGHT)));
        textView.setText(R.string.average_size_label);
        textView.setTextSize(DENT_SIZE_TEXT_SIZE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundColor(LABEL_BACKGROUND);
        textView.setTextColor(LABEL_FOREGROUND);
        textView.setMinimumWidth(width + widthRemainder);

        int startIndex = damageClassifierRange.getStartValue().ordinal();
        int endIndex = damageClassifierRange.getEndValue().ordinal() + 1;
        for (int i = startIndex; i < endIndex; i++) {

            LinearLayout dentSizeGroup = new LinearLayout(context);
            dentSizeLabelRow.addView(dentSizeGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dentSizeGroup.setPadding(2, 2, 0, 0);
            dentSizeGroup.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < DentSize.values().length; j++) {
                DentSize dentSize = DentSize.values()[j];
                textView = new TextView(context);
                dentSizeGroup.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(DENT_SIZE_ROW_HEIGHT)));
                textView.setText(dentSize.getLabel() + "\n");
                textView.setTextSize(DENT_SIZE_TEXT_SIZE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(LABEL_FOREGROUND);

                widthRemainder = width % 4;
                if (j == 3) {
                    textView.setMinimumWidth(width / 4 + widthRemainder);
                } else {
                    textView.setMinimumWidth(width / 4);
                }
            }

        }

    }

    /**
     * Creates all the rows for a range of car areas and a range of damage clasifiers.
     *
     * Something like:
     *
     * Hood        101 102 103 104 ...
     * Roof        201 202 203 204 ...
     * Deck Lid    301 302 303 304 ...
     * .
     * .
     * .
     */
    private void createDamageButtonRows(Context context, ViewGroup parent, Range<CarArea> carAreaRange, Range<DamageClassifier> damageClassifierRange) {
        CarArea[] carAreas = CarArea.values();
        int startIndex = carAreaRange.getStartValue().ordinal();
        int endIndex = carAreaRange.getEndValue().ordinal() + 1;
        for (int i = startIndex; i < endIndex; i++) {
            CarArea carArea = carAreas[i];
            createDamageButtonRow(context, parent, carArea, damageClassifierRange);
        }
    }

    /**
     * Creates on row of damage buttons for one car area and a range of damage classifiers.
     *
     * Something like:
     *
     * Hood        101 102 103 104 ...
     */
    private void createDamageButtonRow(Context context, ViewGroup parent, CarArea carArea, Range<DamageClassifier> damageClassifierRange) {

        DentPriceMatrix dentPriceMatrix = jobModel.getDentPriceMatrixModel().getDentPriceMatrix();
        if(dentPriceMatrix == null){
            dentPriceMatrix = new DentPriceMatrix();
            jobModel.getDentPriceMatrixModel().setDentPriceMatrix(dentPriceMatrix);
        }
        damageMatrixService.setDentPriceMatrix(dentPriceMatrix);

        int width = SCREEN_WIDTH / NUM_HEADER_COLS;
        int widthRemainder = SCREEN_WIDTH % NUM_HEADER_COLS;

        CarAreaPriceRow priceRow = new CarAreaPriceRow(context);
        parent.addView(priceRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(DENT_BUTTON_ROW_HEIGHT)));

        // Car Area cell
        CarAreaLabel carAreaLabel = new CarAreaLabel(context, carArea, root, riPanel, addInfoPanel);
        priceRow.addView(carAreaLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        carAreaLabel.setMinimumWidth(width + widthRemainder);


        int startIndex = damageClassifierRange.getStartValue().ordinal();
        int endIndex = damageClassifierRange.getEndValue().ordinal() + 1;
        for (int i = startIndex; i < endIndex; i++) {

            DamageClassifier damageClassifier = DamageClassifier.values()[i];
            carAreaLabelMap.put(new DentDamageKey(carArea, damageClassifier, null), carAreaLabel);

            LinearLayout dentSizeGroup = new LinearLayout(context);
            priceRow.addView(dentSizeGroup, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dentSizeGroup.setPadding(2, 2, 0, 0);
            priceRow.offsetTopAndBottom(0);
            dentSizeGroup.setOrientation(LinearLayout.HORIZONTAL);

            int numDentSizes = DentSize.values().length;
            for (int j = 0; j < numDentSizes; j++) {
                DentSize dentSize = DentSize.values()[j];

                DentDamageKey dentDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);

                final DentDamageButton button = new DentDamageButton(context, dentDamageKey, getActivity());
                dentSizeGroup.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                buttonMap.put(dentDamageKey, button);
                button.setText(dentPriceMatrix.getDentPrice(dentDamageKey));
                widthRemainder = width % numDentSizes;
                if (j == numDentSizes - 1) {
                    button.setMinimumWidth(width / numDentSizes + widthRemainder);
                } else {
                    button.setMinimumWidth(width / numDentSizes);
                }
            }

        }

    }

    public Map<DentDamageKey, DentDamageButton> getButtonMap() {
        return buttonMap;
    }

    public Map<DentDamageKey, CarAreaLabel> getCarAreaLabelMap() {
        return carAreaLabelMap;
    }

    public boolean isInitializationComplete() {
        return isInitializationComplete;
    }

    public void showAreYouSureDeleteDialog(DentDamageKey damageKey) {
        this.currentDentDamageKey = damageKey;
        DialogFragment dialog = new AreYouSureDeleteDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }

    public void showAreYouSureChangeDialog(DentDamageKey damageKey) {
        this.currentDentDamageKey = damageKey;
        DialogFragment dialog = new AreYouSureChangeDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onYesDeleteClick() {
        damageMatrixService.removeRiAddInfoButtons(currentDentDamageKey);
        damageMatrixService.damageOff(currentDentDamageKey);
        damageMatrixService.updateMatrixFragment();
    }

    @Override
    public void onNoDeleteClick() {
    }

    @Override
    public void onYesChangeClick() {
        damageMatrixService.damageOn(currentDentDamageKey);
        damageMatrixService.updateMatrixFragment();
    }

    @Override
    public void onNoChangeClick() {
    }

    public void editDamageValueDialog(DentDamageKey damageKey, Button selectedButton){

        this.currentSelectedButton = selectedButton;
        this.currentDentDamageKey = damageKey;
        DialogFragment dialog = new EditDamageValueDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");

    }
    @Override
    public void onOkClick(String inputValue) {

        currentSelectedButton.setText(inputValue);
        damageMatrixService.updateDentPriceMatrix(currentDentDamageKey, Integer.parseInt(inputValue));
        damageMatrixService.damageOn(currentDentDamageKey);
        damageMatrixService.updateMatrixFragment();
    }

    @Override
    public void onCancelClick() {
    }
}