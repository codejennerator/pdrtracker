rmg.pdrtracker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class BaseActivityTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    protected T activity;

    protected Instrumentation instrumentation;

    public BaseActivityTestCase(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        instrumentation = getInstrumentation();
    }

    /**
     * Synchronously waits for the given tab to be selected on the action bar.
     *
     * @param tabIndex the tab to select.
     */
    protected void selectNavigationItem(final int tabIndex) {
        final ActionBar actionBar = activity.getActionBar();
        BlockingUiThreadRunner blockingUiThreadRunner = new BlockingUiThreadRunner(instrumentation);
        blockingUiThreadRunner.run(new Runnable() {
            @Override
            public void run() {
                actionBar.setSelectedNavigationItem(tabIndex);
            }
        });
    }

    public void sleepMilli(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException e) {
            // Done
        }
    }

    public void sleep(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            // Done
        }
    }
}
