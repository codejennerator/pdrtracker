package rmg.pdrtracker.job.damagematrix.additionalinfo;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.job.constants.AddInfo;
import rmg.pdrtracker.job.damagematrix.DamageMatrixService;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.prices.AddOnSurcharge;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static rmg.pdrtracker.util.AppUtils.toDip;

public class AddInfoTable extends LinearLayout {


    private int size;

    private DamageMatrixService damageMatrixService = DamageMatrixService.get();

    private AddOnSurcharge addOnSurcharge = new AddOnSurcharge();

    private boolean hasAluminum = false;

    public int numOversizeDents = 0;

    public AddInfoTable(Context context) {

        super(context);

        setOrientation(LinearLayout.VERTICAL);

        // jenn  createCarAreaRows( new Range<CarArea>(CarArea.HOOD, CarArea.OTHER));
    }

    public void reset() {

        removeAllViews();
        createTopHeader();
        //jenn temp remove createTableHeaders();
        size = 0;
    }

    public void resetFromClear() {
        numOversizeDents = 0;
        hasAluminum = false;
    }

    public int getSize() {
        return size;
    }

    /**
     * Displays the AddInfo header label.
     */
    private void createTopHeader() {
        TextView textView = new TextView(getContext());
        addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Additional Information");
        textView.setTextSize(20);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(getResources().getColor(R.drawable.black));
        textView.setBackgroundColor(getResources().getColor(R.drawable.gray1));
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        label.setTextSize(16);
        label.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        label.setTextColor(getResources().getColor(R.drawable.label_foreground));
        return label;
    }

    private LinearLayout createTableRow() {
        LinearLayout layout = new LinearLayout(getContext());
        addView(layout, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 0, 0, 0);
        return layout;
    }



    public void addItem(final AddInfoItemModel addInfoItem) {

        Float carAreaCost = damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea());
        final LinearLayout row = createTableRow();

        final CheckBox checkbox = new CheckBox(getContext());
        row.addView(checkbox, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        checkbox.setChecked(addInfoItem.isSelected());
        checkbox.setBackground(getResources().getDrawable(R.drawable.backwithborder));

        TextView addInfoLabel = createLabel(addInfoItem.getAddInfo().getLabel());
        row.addView(addInfoLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        addInfoLabel.setPadding(toDip(10), toDip(10), toDip(10), toDip(10));

        EditText oversizedDentField = createTextField(Integer.toString(addInfoItem.getQuantity()));
        oversizedDentField.setBackground(getResources().getDrawable(R.drawable.backwithborder));
        oversizedDentField.setInputType(InputType.TYPE_CLASS_NUMBER);
        oversizedDentField.setMinimumWidth(toDip(60));
        oversizedDentField.setMaxWidth(toDip(60));
        if(addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT) ){
            row.addView(oversizedDentField, new LayoutParams(toDip(60), WRAP_CONTENT));
            if (!addInfoItem.isSelected()) {
                oversizedDentField.setVisibility(INVISIBLE);
            }
        }
   //checking for saved data and if it is set don't overwrite it with another item that is not the oversize dent
        if(oversizedDentField != null && addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)){
            numOversizeDents = Integer.parseInt(oversizedDentField.getText().toString());
        }
        if(addInfoItem.getAddInfo().equals(AddInfo.ALUMINUM_PANEL)) {

            hasAluminum = true;
        }

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
               if (isChecked) {

                    addInfoItem.setSelected(true);
                   if(!addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {
                        damageMatrixService.getAddInfoModel().addAddInfoItem(addInfoItem);
                       if(addInfoItem.getAddInfo().equals(AddInfo.ALUMINUM_PANEL)) {

                           hasAluminum = true;
                       }
                       if(numOversizeDents > 0)  {
                           Float overSizeSurcharge = numOversizeDents*addOnSurcharge.getAddOnSurcharge(AddInfo.OVERSIZED_DENT);
                           damageMatrixService.addToEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurcharge)*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));

                       }
                       else{
                            damageMatrixService.addToEstimateTally(damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));

                       }
                       damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                   }
                   if(addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {
                        EditText oversizedDentField = (EditText) row.getChildAt(2);
                        oversizedDentField.setVisibility(VISIBLE);
                        oversizedDentField.setText(Integer.toString(addInfoItem.getQuantity()));
                        numOversizeDents = addInfoItem.getQuantity();
                   }
               } else {
                    addInfoItem.setSelected(false);
                   if(!addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {
                       if(addInfoItem.getAddInfo().equals(AddInfo.ALUMINUM_PANEL)) {

                           hasAluminum = false;
                       }
                       if(numOversizeDents > 0)  {
                           Float overSizeSurcharge = numOversizeDents*addOnSurcharge.getAddOnSurcharge(AddInfo.OVERSIZED_DENT);
                           damageMatrixService.subtractFromEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurcharge)*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));

                       }
                       else{
                            damageMatrixService.subtractFromEstimateTally(damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));

                       }
                       damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                   }
                    if(addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {
                        EditText oversizedDentField = (EditText) row.getChildAt(2);
                        oversizedDentField.setVisibility(INVISIBLE);
                        Float overSizeSurcharge = addInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo());
                        if(hasAluminum){
                            damageMatrixService.subtractFromEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurcharge)*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                            damageMatrixService.subtractFromEstimateTally(overSizeSurcharge);
                            damageMatrixService.addToEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea()))*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                        }
                        else{
                            damageMatrixService.subtractFromEstimateTally(overSizeSurcharge);
                        }
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));

                        addInfoItem.setQuantity(0);
                        numOversizeDents = addInfoItem.getQuantity();
                        oversizedDentField.setText("0");
                    }
                }
            }
        });
        oversizedDentField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText oversizedDentField = (EditText) row.getChildAt(2);
                try{
                    if(oversizedDentField.getText() != null && oversizedDentField.getText().toString().length() > 0){

                        Float overSizeSurchargeBefore = addInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo());
                        addInfoItem.setQuantity(Integer.parseInt(oversizedDentField.getText().toString()));
                        numOversizeDents = addInfoItem.getQuantity();
                        Float overSizeSurchargeAfter = addInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo());
                        if(hasAluminum){
                            damageMatrixService.subtractFromEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurchargeBefore)*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                            damageMatrixService.subtractFromEstimateTally(overSizeSurchargeBefore);
                            damageMatrixService.addToEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurchargeAfter)*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                            damageMatrixService.addToEstimateTally(overSizeSurchargeAfter);
                        }
                        else{

                            damageMatrixService.subtractFromEstimateTally(overSizeSurchargeBefore);
                            damageMatrixService.addToEstimateTally(overSizeSurchargeAfter);

                        }

                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                    }
                    else{
                        Float overSizeSurcharge = addInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo());
                        if(hasAluminum){
                            damageMatrixService.subtractFromEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurcharge)*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                            damageMatrixService.subtractFromEstimateTally(overSizeSurcharge);
                            damageMatrixService.addToEstimateTally(damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())*addOnSurcharge.getAddOnSurcharge(AddInfo.ALUMINUM_PANEL));
                        }
                        else{
                            damageMatrixService.subtractFromEstimateTally(overSizeSurcharge);
                        }
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                        addInfoItem.setQuantity(0);
                        numOversizeDents = addInfoItem.getQuantity();
                    }
                }catch(NumberFormatException e){
                    System.out.println("Number Format Exception trying to set oversize dent quantity. "+e);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        size++;

    }

    public void addComment(final AddInfoItemModel addInfoItem) {

        final LinearLayout row = createTableRow();
        row.setPadding(toDip(0),toDip(50),toDip(0),toDip(0));
        row.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL);
        TextView addInfoLabel = createLabel("Notes");
        row.addView(addInfoLabel, new LayoutParams(toDip(75), WRAP_CONTENT));
        addInfoLabel.setPadding(toDip(10), toDip(10), toDip(10), toDip(10));

        EditText notesField = createTextField(addInfoItem.getNote());
        notesField.setBackground(getResources().getDrawable(R.drawable.backwithborder));
        notesField.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP);
        notesField.setMinimumWidth(toDip(540));
        notesField.setMaxWidth(toDip(540));
        notesField.setScrollContainer(true);
        notesField.setScrollBarStyle(SCROLLBARS_INSIDE_INSET);
        row.addView(notesField, new LayoutParams(toDip(540), WRAP_CONTENT));
        notesField.setMinimumHeight(toDip(300));
        notesField.setMaxHeight(toDip(300));

        notesField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText notesField = (EditText) row.getChildAt(1);

                if(notesField.getText() != null && notesField.getText().toString().length()>0){
                    addInfoItem.setNote(notesField.getText().toString());
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        size++;

    }

    private EditText createTextField(String text) {
        EditText textField = new EditText(getContext());
        textField.setText(text);
        textField.setTextSize(16);
        textField.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textField.setTextColor(getResources().getColor(R.drawable.text_field_foreground));
        textField.setBackground(getResources().getDrawable(R.drawable.cell_shape));
        return textField;
    }

    public void clearItem(final AddInfoItemModel addInfoItem, int numOversizeDents) {

        Float carAreaCost = damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea());

        try{

            if(addInfoItem.isSelected() && !addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)){
                if(addInfoItem.getAddInfo().equals(AddInfo.ALUMINUM_PANEL)){
                    if(numOversizeDents > 0){
                        Float overSizeSurcharge = numOversizeDents*addOnSurcharge.getAddOnSurcharge(AddInfo.OVERSIZED_DENT);
                        damageMatrixService.subtractFromEstimateTally((damageMatrixService.carAreaDamageCost(addInfoItem.getCarArea())+overSizeSurcharge)*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));
                    }
                    else{
                        damageMatrixService.subtractFromEstimateTally(carAreaCost*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));
                    }
                }
                else{
                    damageMatrixService.subtractFromEstimateTally(carAreaCost*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo()));
                }
                damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                addInfoItem.setSelected(false);
            }
            if(addInfoItem.isSelected() && addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)){
                addInfoItem.setSelected(false);
            }

            if(addInfoItem.getQuantity() != 0 && addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)){
                Float overSizeSurchargeSubtract = addInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(addInfoItem.getAddInfo());
                damageMatrixService.subtractFromEstimateTally(overSizeSurchargeSubtract);
                damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                addInfoItem.setQuantity(0);
                numOversizeDents = addInfoItem.getQuantity();
            }
        }catch(NumberFormatException e){
            System.out.println("Number Format Exception trying to set oversize dent quantity. "+e);
        }
        finally{
            addInfoItem.setQuantity(0);
            numOversizeDents = addInfoItem.getQuantity();
        }
        if(addInfoItem.getNote() != null && addInfoItem.getNote().length() > 0)  {
            addInfoItem.setNote(" ");
        }
        addInfoItem.setQuantity(0);
        numOversizeDents = addInfoItem.getQuantity();
        size--;
    }

}