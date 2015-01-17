package rmg.pdrtracker.login.loginUser.changePassword;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.R;
import rmg.pdrtracker.login.constants.ChangePassword;
import rmg.pdrtracker.login.constants.RequestNewUser;
import rmg.pdrtracker.login.utils.LoginService;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static rmg.pdrtracker.util.AppUtils.toDip;

public class ChangePasswordForm extends LinearLayout {


    private int size;

    private LoginService loginService = LoginService.get();

    public ChangePasswordForm(Context context) {

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

    public void showChangePasswordForm() {

        final LinearLayout row = createTableRow();

        TextView userNameLabel = createLabel(ChangePassword.USER_NAME.getLabel());
        row.addView(userNameLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row2 = createTableRow();

        EditText userNameField = createTextField("");
        userNameField.setInputType(InputType.TYPE_CLASS_TEXT);
        row2.addView(userNameField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        userNameField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText userNameField = (EditText) row2.getChildAt(0);
                try{
                    if(userNameField.getText() != null && userNameField.getText().toString().length() > 0){
                        loginService.setChangePasswordUserName(userNameField.getText().toString());
                    }
                }catch (Exception e){

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        final LinearLayout row3 = createTableRow();

        TextView currentPasswordLabel = createLabel(ChangePassword.CURRENT_PASSWORD.getLabel());
        row3.addView(currentPasswordLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row4 = createTableRow();

        EditText currentPasswordField = createTextField("");
        currentPasswordField.setInputType(InputType.TYPE_CLASS_TEXT);
        row4.addView(currentPasswordField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        currentPasswordField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText currentPasswordField = (EditText) row4.getChildAt(0);
                try{
                    if(currentPasswordField.getText() != null && currentPasswordField.getText().toString().length() > 0){
                        loginService.setChangePasswordCurrentPassword(currentPasswordField.getText().toString());
                    }
                }catch (Exception e){

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        final LinearLayout row5 = createTableRow();

        TextView newPasswordLabel = createLabel(ChangePassword.NEW_PASSWORD.getLabel());
        row5.addView(newPasswordLabel, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        final LinearLayout row6 = createTableRow();

        EditText newPasswordField = createTextField("");
        newPasswordField.setInputType(InputType.TYPE_CLASS_TEXT);
        row6.addView(newPasswordField, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        newPasswordField.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                EditText newPasswordField = (EditText) row6.getChildAt(0);
                try{
                    if(newPasswordField.getText() != null && newPasswordField.getText().toString().length() > 0){
                        loginService.setChangePasswordNewPassword(newPasswordField.getText().toString());
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