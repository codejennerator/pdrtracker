package rmg.pdrtracker.job.generalNotes;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.JobTabListener;


public class GeneralNotesTabListener<T extends Fragment> extends JobTabListener<T> {

    public GeneralNotesTabListener(JobActivity activity, String tag, Class<T> clazz) {
        super(activity, tag, clazz);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        super.onTabSelected(tab, ft);
        activity.setActiveTab(JobActivity.TabNames.GENERAL_NOTES);

    }

}