package rmg.pdrtracker.profile.invoicesSetup;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.profile.ProfileTabListener;
import rmg.pdrtracker.profile.activities.ProfileActivity;

public class InvoicesSetupTabListener<T extends Fragment> extends ProfileTabListener<T> {

    public InvoicesSetupTabListener(ProfileActivity activity, String tag, Class<T> clazz) {
        super(activity, tag, clazz);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        super.onTabSelected(tab, ft);
        activity.setActiveTab(ProfileActivity.TabNames.USER_INFO);

    }

}