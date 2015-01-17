package rmg.pdrtracker.login.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoginModelService {

    public static final String LOGIN_MODEL = "loginModel";

    private static LoginModelService singleton = new LoginModelService();

    private LoginModel loginModel;

    private LoginModelService() {
    }

    public static LoginModelService getInstance() {
        return singleton;
    }

    public void init(Activity activity, Bundle savedInstanceState) {

        Intent intent = activity.getIntent();

        // This is the temporary state bundle that saves state when the app is minified.
        if (savedInstanceState != null) {
            loginModel = (LoginModel) savedInstanceState.get(LOGIN_MODEL);
        }

        // This is the bundle that was loaded out of the database
        Bundle savedBundle = intent.getExtras();
        loginModel = (LoginModel) savedBundle.get(LOGIN_MODEL);
        if (loginModel != null) {
            return;
        }

        loginModel = new LoginModel();

    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void saveModelState(Bundle savedInstanceState, LoginModel loginModel) {
        savedInstanceState.putSerializable(LOGIN_MODEL, loginModel);
    }

}
