package rmg.pdrtracker.login.constants;

import rmg.pdrtracker.R;
import rmg.pdrtracker.util.AppUtils;

public enum RequestNewUser {

    NAME(R.string.login_user_name_label),
    PHONE(R.string.login_phone_label),
    EMAIL(R.string.login_email_label);


    private int labelId;

    private String label;

    RequestNewUser(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

}