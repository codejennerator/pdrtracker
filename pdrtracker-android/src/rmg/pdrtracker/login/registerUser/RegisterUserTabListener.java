package rmg.pdrtracker.login.registerUser;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.login.LoginTabListener;
import rmg.pdrtracker.login.activities.LoginActivity;


public class RegisterUserTabListener<T extends Fragment> extends LoginTabListener<T> {

    public RegisterUserTabListener(LoginActivity activity, String tag, Class<T> clazz) {
        super(activity, tag, clazz);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        super.onTabSelected(tab, ft);
        activity.setActiveTab(LoginActivity.TabNames.LOGIN_USER);

    }

}