package rmg.pdrtracker.profile.estimatesSetup;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.profile.ProfileTabListener;
import rmg.pdrtracker.profile.activities.ProfileActivity;

public class EstimatesSetupTabListener<T extends Fragment> extends ProfileTabListener<T> {

    public EstimatesSetupTabListener(ProfileActivity activity, String tag, Class<T> clazz) {
        super(activity, tag, clazz);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        super.onTabSelected(tab, ft);
        activity.setActiveTab(ProfileActivity.TabNames.USER_INFO);

    }

}