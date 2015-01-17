package rmg.pdrtracker.job.details;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.JobTabListener;

public class JobDetailsTabListener<T extends Fragment> extends JobTabListener<T> {


    public JobDetailsTabListener(JobActivity activity, String tag, Class<T> clazz) {
        super(activity, tag, clazz);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        super.onTabSelected(tab, ft);
        activity.setActiveTab(JobActivity.TabNames.JOB_DETAILS);

    }

}