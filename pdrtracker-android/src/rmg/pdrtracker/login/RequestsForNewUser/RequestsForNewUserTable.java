package rmg.pdrtracker.login.RequestsForNewUser;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.R;
import rmg.pdrtracker.login.constants.RequestNewUser;
import rmg.pdrtracker.login.utils.LoginService;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static rmg.pdrtracker.util.AppUtils.toDip;

public class RequestsForNewUserTable extends LinearLayout {


    private int size;

    private LoginService loginService = LoginService.get();

    public RequestsForNewUserTable(Context context) {

        super(context);

        setOrientation(LinearLayout.VERTICAL);

    }

    public void reset() {

        removeAllViews();
        createTopHeader();
        //jenn temp remove createTableHeaders();
        size = 0;
    }

    public void resetFromClear() {

    }

    public int getSize() {
        return size;
    }

    /**
     * Displays the Request New User header label.
     */
    private void createTopHeader() {
        TextView textView = new TextView(getContext());
        addView(textView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("New User Requests");
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
        label.setPadding(toDip(10), toDip(20), toDip(0), toDip(0));

        return label;
    }

    private LinearLayout createTableRow() {
        LinearLayout layout = new LinearLayout(getContext());
        addView(layout, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 0, 0, 0);
        return layout;
    }

    private EditText createTextField(String text) {
        EditText textField = new EditText(getContext());
        textField.setText(text);
        textField.setTextSize(10);
        textField.setX(toDip(10));
        textField.setMinimumWidth(toDip(200));
        textField.setMaxWidth(toDip(200));
        textField.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textField.setTextColor(getResources().getColor(R.drawable.text_field_foreground));
        textField.setBackground(getResources().getDrawable(R.drawable.backwithborder));
        return textField;
    }

    public void loadRequests(String name, String email, String phone, int id) {

        final LinearLayout row = createTableRow();

        final CheckBox checkbox = new CheckBox(getContext());
        row.addView(checkbox, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        checkbox.setId(id);
        checkbox.setChecked(false);
        checkbox.setBackground(getResources().getDrawable(R.drawable.backwithborder));

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    System.out.println("selected id "+checkbox.getId());
                    loginService.addUserContactedToHS(""+checkbox.getId());
                } else{
                    System.out.println("**unselected id "+checkbox.getId());
                    loginService.removeUserContactedFromHS("" + checkbox.getId());
                }
            }
        });

        TextView addInfoLabel = createLabel(name+"  "+phone+"  "+email);
        row.addView(addInfoLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        addInfoLabel.setPadding(toDip(10), toDip(10), toDip(10), toDip(10));
        size++;

    }
}