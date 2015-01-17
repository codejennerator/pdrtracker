package rmg.pdrtracker.job;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.R;

public class JobTabListener<T extends Fragment> implements ActionBar.TabListener {

    private Fragment fragment;

    protected final JobActivity activity;

    private final String tag;

    private final Class<T> clazz;

    /** Constructor used each time a new tab is created.
     * @param activity  The host Activity, used to instantiate the fragment
     * @param tag  The identifier tag for the fragment
     * @param clazz  The fragment's Class, used to instantiate the fragment
     */
    public JobTabListener(JobActivity activity, String tag, Class<T> clazz) {
        this.activity = activity;
        this.tag = tag;
        this.clazz = clazz;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        // Check if the fragment is already initialized
        if (fragment == null) {
            // If not, instantiate and add it to the activity
            fragment = Fragment.instantiate(activity, clazz.getName());
            ft.add(R.id.job_details_layout_root, fragment, tag);
            activity.setActiveFragment(fragment);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(fragment);
            activity.setActiveFragment(fragment);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        if (fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(fragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }

    public T getFragment() {
        return (T) fragment;
    }
}
