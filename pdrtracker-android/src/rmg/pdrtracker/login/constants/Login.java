package rmg.pdrtracker.login.constants;

import rmg.pdrtracker.R;
import rmg.pdrtracker.util.AppUtils;

public enum Login {

    LOGIN_URL(R.string.login_url),
    LOGIN_REGISTRATION_ADMIN(R.string.login_registration_admin),
    REGISTER_USER_EXISTS_ERROR(R.string.register_user_exists_error),
    LOGIN_ERROR(R.string.login_error),
    LOGIN_INACTIVE_ERROR(R.string.login_inactive_error);


    private int labelId;

    private String label;

    Login(int labelId) {
        this.labelId = labelId;
    }

    public String getLabel() {
        if (label == null) {
            label = AppUtils.getString(labelId);
        }
        return label;
    }

}