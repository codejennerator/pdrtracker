package rmg.pdrtracker.job.damagematrix.ri;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import rmg.pdrtracker.job.damagematrix.DamageMatrixService;
import rmg.pdrtracker.job.model.RiItemModel;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PartReplacementTable extends LinearLayout {

    private int size;

    private DamageMatrixService damageMatrixService = DamageMatrixService.get();

    public PartReplacementTable(Context context) {

        super(context);

        setOrientation(LinearLayout.VERTICAL);

        createTopHeader();
        createTableHeaders();
    }

    public void reset() {
        removeAllViews();
        createTopHeader();
        createTableHeaders();
        size = 0;
    }

    public int getSize() {
        return size;
    }

    private void createTopHeader() {
        TextView textView = new TextView(getContext());
        addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Replacement Parts");
        textView.setTextSize(20);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(getResources().getColor(R.drawable.black));
        textView.setBackgroundColor(getResources().getColor(R.drawable.gray1));
    }

    private void createTableHeaders() {
        LinearLayout row = createTableRow();
        TextView headerLabel = createLabel("Part");
        headerLabel.setBackgroundColor(Color.BLACK);
        headerLabel.setTextSize(20);
        headerLabel.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row.addView(headerLabel, new LayoutParams(AppUtils.toDip(200), WRAP_CONTENT));

        headerLabel = createLabel("Labor Hours");
        headerLabel.setBackgroundColor(Color.BLACK);
        headerLabel.setTextSize(20);
        headerLabel.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row.addView(headerLabel, new LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F / 2));

        headerLabel = createLabel("Part Cost");
        headerLabel.setBackgroundColor(Color.BLACK);
        headerLabel.setTextSize(20);
        headerLabel.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row.addView(headerLabel, new LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F / 2));
    }

    public void addItem(final RiItemModel riPrice) {

        final LinearLayout row = createTableRow();

        TextView partNameLabel = createLabel(riPrice.getRiItemName().getLabel());
        row.addView(partNameLabel, new LinearLayout.LayoutParams(AppUtils.toDip(200), WRAP_CONTENT));

        EditText laborHoursField = createTextField(Float.toString(riPrice.getLaborHours()));
        laborHoursField.setInputType(InputType.TYPE_CLASS_NUMBER);
        row.addView(laborHoursField, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F / 2));
        laborHoursField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText laborHoursField = (EditText) row.getChildAt(1);
                try{
                    if(laborHoursField.getText() != null && laborHoursField.getText().toString().length()>0){
                        damageMatrixService.subtractFromEstimateTally(riPrice.getLaborHours()*riPrice.getLaborCost());
                        damageMatrixService.addToEstimateTally(Float.parseFloat(laborHoursField.getText().toString())*riPrice.getLaborCost());
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                        riPrice.setLaborHours(Float.parseFloat(laborHoursField.getText().toString()));
                    }
                    else{

                        damageMatrixService.subtractFromEstimateTally(riPrice.getLaborHours()*riPrice.getLaborCost());
                        riPrice.setLaborHours(0.0F);
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                    }

                }catch(NumberFormatException e){
                    System.out.println("Number Format Exception trying to setLaborHours. "+e);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        EditText partCostField = createTextField(Float.toString(riPrice.getPartCost()));
        row.addView(partCostField, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F / 2));
        partCostField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText partCostField = (EditText) row.getChildAt(2);
                try{
                    if(partCostField.getText() != null && partCostField.getText().toString().length()>0){
                        damageMatrixService.subtractFromEstimateTally(riPrice.getPartCost());
                        damageMatrixService.addToEstimateTally(Float.parseFloat(partCostField.getText().toString()));
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                        riPrice.setPartCost(Float.parseFloat(partCostField.getText().toString()));
                    }
                    else{
                        damageMatrixService.subtractFromEstimateTally(riPrice.getPartCost());
                        riPrice.setPartCost(0.0F);
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                    }
                }catch(NumberFormatException e){
                    System.out.println("Number Format Exception trying to setPartCost. "+e);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        size++;
    }

    public void clearItem(final RiItemModel riPrice) {
        try{
            damageMatrixService.subtractFromEstimateTally(riPrice.getLaborHours()*riPrice.getLaborCost());
            damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
            riPrice.setLaborHours(0.0F);
            damageMatrixService.subtractFromEstimateTally(riPrice.getPartCost());
            damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
            riPrice.setPartCost(0.0F);

        }catch(NumberFormatException e){
            System.out.println("Number Format Exception trying to setPartCost. "+e);
        }
    }

    private EditText createTextField() {
        return createTextField(null);
    }

    private EditText createTextField(String text) {
        EditText textField = new EditText(getContext());
        textField.setTextSize(16);
        textField.setTextColor(getResources().getColor(R.drawable.text_field_foreground));
        textField.setBackground(getResources().getDrawable(R.drawable.backwithborder));

        if (text != null) {
            textField.setText(text);
        }

        return textField;
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
        addView(layout, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 0, 0, 0);
        return layout;
    }

}