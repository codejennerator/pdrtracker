package rmg.pdrtracker.login.constants;

import rmg.pdrtracker.R;
import rmg.pdrtracker.util.AppUtils;

public enum ChangePassword {

    USER_NAME(R.string.login_user_name_label),
    CURRENT_PASSWORD(R.string.change_password_current_password_label),
    NEW_PASSWORD(R.string.change_password_new_password_label);


    private int labelId;

    private String label;

    ChangePassword(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

}