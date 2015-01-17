package rmg.pdrtracker.job.damagematrix.ri;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.job.damagematrix.DamageMatrixService;
import rmg.pdrtracker.job.model.RiItemModel;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class RiTable extends LinearLayout {


    private int size;

    private DamageMatrixService damageMatrixService = DamageMatrixService.get();

    public RiTable(Context context) {

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

    /**
     * Displays the RI header label.
     */
    private void createTopHeader() {
        TextView textView = new TextView(getContext());
        addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("R&I");
        textView.setTextSize(20);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(getResources().getColor(R.drawable.black));
        textView.setBackgroundColor(getResources().getColor(R.drawable.gray1));
    }

    private void createTableHeaders() {
        LinearLayout row = createTableRow();
        TextView headerLabel = createLabel("Item");
        headerLabel.setBackgroundColor(Color.BLACK);
        headerLabel.setTextSize(20);
        headerLabel.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row.addView(headerLabel, new LayoutParams(AppUtils.toDip(200), WRAP_CONTENT));

        headerLabel = createLabel("Labor Hours");
        headerLabel.setBackgroundColor(Color.BLACK);
        headerLabel.setTextSize(20);
        headerLabel.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row.addView(headerLabel, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    public void addItem(final RiItemModel riItem) {
        final LinearLayout row = createTableRow();

        final CheckBox checkbox = new CheckBox(getContext());
        row.addView(checkbox, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        checkbox.setChecked(riItem.isSelected());
        checkbox.setBackground(getResources().getDrawable(R.drawable.backwithborder));

        TextView partNameLabel = createLabel(riItem.getRiItemName().getLabel());
        row.addView(partNameLabel, new LayoutParams(AppUtils.toDip(200), WRAP_CONTENT));
        partNameLabel.setPadding(AppUtils.toDip(10), 0, 0, 0);

        EditText laborHoursField = createTextField(Float.toString(riItem.getLaborHours()));
        row.addView(laborHoursField, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (!riItem.isSelected()) {
            laborHoursField.setVisibility(INVISIBLE);
        }

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    riItem.setSelected(true);
                    EditText laborHoursField = (EditText) row.getChildAt(2);
                    laborHoursField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    laborHoursField.setVisibility(VISIBLE);
                } else {
                    riItem.setSelected(false);
                    EditText laborHoursField = (EditText) row.getChildAt(2);
                    damageMatrixService.subtractFromEstimateTally(riItem.getLaborHours()*riItem.getLaborCost());
                    riItem.setLaborHours(0.0F);
                    laborHoursField.setText("0.0");
                    damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                    laborHoursField.setVisibility(INVISIBLE);
                }
            }
        });

        laborHoursField.addTextChangedListener(new TextWatcher(){
           @Override
            public void afterTextChanged(Editable s) {
                EditText laborHoursField = (EditText) row.getChildAt(2);
                try{
                    if(laborHoursField.getText() == null || laborHoursField.getText().toString().length()==0){
                        damageMatrixService.subtractFromEstimateTally(riItem.getLaborHours()*riItem.getLaborCost());
                        riItem.setLaborHours(0.0F);
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));                                }
                    if(laborHoursField.getText() != null && laborHoursField.getText().toString().length()>0){
                        damageMatrixService.subtractFromEstimateTally(riItem.getLaborHours()*riItem.getLaborCost());
                        damageMatrixService.addToEstimateTally(Float.parseFloat(laborHoursField.getText().toString())*riItem.getLaborCost());
                        damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                        riItem.setLaborHours(Float.parseFloat(laborHoursField.getText().toString()));
                    }
                }catch(NumberFormatException e){
                    damageMatrixService.subtractFromEstimateTally(riItem.getLaborHours()*riItem.getLaborCost());
                    riItem.setLaborHours(0.0F);
                    laborHoursField.setText("0.0");
                    damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
                    System.out.println("Number Format Exception trying to setLaborHours. "+e);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });

        size++;
    }

    public void clearItem(final RiItemModel riItem) {
        try{
            if(riItem.isSelected()){
                riItem.setSelected(false);
            }
            damageMatrixService.subtractFromEstimateTally(riItem.getLaborHours()*riItem.getLaborCost());
            damageMatrixService.setActionBarSubtitle(Float.toString(damageMatrixService.getEstimateTally()));
            riItem.setLaborHours(0.0F);
        }catch(NumberFormatException e){
            System.out.println("Number Format Exception trying to setPartCost. "+e);
        }
    }

    private EditText createTextField(String text) {
        EditText textField = new EditText(getContext());
        textField.setText(text);
        textField.setTextSize(16);
        textField.setTextColor(getResources().getColor(R.drawable.text_field_foreground));
        textField.setBackground(getResources().getDrawable(R.drawable.backwithborder));
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
        addView(layout, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 0, 0, 0);
        return layout;
    }


}
