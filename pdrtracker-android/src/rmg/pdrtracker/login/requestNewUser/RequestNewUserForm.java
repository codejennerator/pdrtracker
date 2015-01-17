package rmg.pdrtracker.login.requestNewUser;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.R;
import rmg.pdrtracker.db.LoginDao;
import rmg.pdrtracker.job.constants.AddInfo;
import rmg.pdrtracker.job.damagematrix.DamageMatrixService;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.prices.AddOnSurcharge;
import rmg.pdrtracker.login.constants.RequestNewUser;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.login.model.LoginModelService;
import rmg.pdrtracker.login.model.RequestNewUserModel;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.login.utils.LoginService;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static rmg.pdrtracker.util.AppUtils.toDip;

public class RequestNewUserForm extends LinearLayout {


    private int size;

    private LoginService loginService = LoginService.get();

    public RequestNewUserForm(Context context) {

        super(context);

        setOrientation(LinearLayout.VERTICAL);

    }

    public void reset() {

        removeAllViews();
        createTopHeader();
        //jenn temp remove createTableHeaders();
        size = 0;
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
        textView.setText("Request New User");
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

    public void loadForm() {

        final LinearLayout row = createTableRow();

        TextView nameLabel = createLabel(RequestNewUser.NAME.getLabel());
        row.addView(nameLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row2 = createTableRow();

        EditText nameField = createTextField("");
        nameField.setInputType(InputType.TYPE_CLASS_TEXT);
        row2.addView(nameField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        nameField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText nameField = (EditText) row2.getChildAt(0);
                try{
                    if(nameField.getText() != null && nameField.getText().toString().length() > 0){
                        loginService.setRequestNewUserName(nameField.getText().toString());
                    }
                }catch (Exception e){

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        final LinearLayout row3 = createTableRow();

        TextView phoneLabel = createLabel(RequestNewUser.PHONE.getLabel());
        row3.addView(phoneLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row4 = createTableRow();

        EditText phoneField = createTextField("");
        phoneField.setInputType(InputType.TYPE_CLASS_TEXT);
        row4.addView(phoneField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        phoneField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText phoneField = (EditText) row4.getChildAt(0);
                try{
                    if(phoneField.getText() != null && phoneField.getText().toString().length() > 0){
                        loginService.setRequestNewUserPhone(phoneField.getText().toString());
                    }
                }catch (Exception e){

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        final LinearLayout row5 = createTableRow();

        TextView emailLabel = createLabel(RequestNewUser.EMAIL.getLabel());
        row5.addView(emailLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row6 = createTableRow();

        EditText emailField = createTextField("");
        emailField.setInputType(InputType.TYPE_CLASS_TEXT);
        row6.addView(emailField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        emailField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText emailField = (EditText) row6.getChildAt(0);
                try{
                    if(emailField.getText() != null && emailField.getText().toString().length() > 0){
                        loginService.setRequestNewUserEmail(emailField.getText().toString());
                    }
                }catch (Exception e){

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        size++;

    }
}