package rmg.pdrtracker;

import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.damagematrix.DamageMatrixFragment;

public class DentMatrixFragmentTest extends BaseActivityTestCase<JobActivity> {

    protected DamageMatrixFragment fragment;

    public DentMatrixFragmentTest(Class<JobActivity> clazz) {
        super(clazz);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        selectNavigationItem(1);

        fragment = (DamageMatrixFragment) activity.getActiveFragment();

        while (!fragment.isInitializationComplete()) {
            sleepMilli(250);
        }
    }

    @Override
    public void tearDown() throws Exception {

        // If super.teardown is not called the next test case will hang because the current one won't stop.
        super.tearDown();

        sleep(1);
    }
}
