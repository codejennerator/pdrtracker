package rmg.pdrtracker;

import android.test.suitebuilder.annotation.SmallTest;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.DamageClassifier;
import rmg.pdrtracker.job.constants.DentSize;
import rmg.pdrtracker.job.activities.JobActivity;
import rmg.pdrtracker.job.damagematrix.CarAreaLabel;
import rmg.pdrtracker.job.damagematrix.DentDamageButton;
import rmg.pdrtracker.job.damagematrix.DentDamageKey;

import java.util.Map;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class rmg.pdrtracker.JobActivityTest \
 * rmg.pdrtracker.tests/android.test.InstrumentationTestRunner
 * <p/>
 * getInstrumentation().waitForIdleSync();
 */
public class JobActivityTest extends DentMatrixFragmentTest {

    public JobActivityTest() {
        super(JobActivity.class);
    }

    @SmallTest
    public void testCarAreaLabels() {

        Map<DentDamageKey, CarAreaLabel> carAreaLabelMap = fragment.getCarAreaLabelMap();

        for (CarArea carArea : CarArea.values()) {
            for (DamageClassifier damageClassifier : DamageClassifier.values()) {
                final CarAreaLabel carAreaLabel = carAreaLabelMap.get(new DentDamageKey(carArea, damageClassifier, null));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (carAreaLabel == null) {
                            return;
                        }
                        carAreaLabel.turnOn();
                    }

                });

                instrumentation.waitForIdleSync();
            }
        }

    }

    @SmallTest
    public void testDamageButtons() {

        Map<DentDamageKey, DentDamageButton> buttonMap = fragment.getButtonMap();
        for (CarArea carArea : CarArea.values()) {
            for (DamageClassifier damageClassifier : DamageClassifier.values()) {
                for (DentSize dentSize : DentSize.values()) {
                    final DentDamageButton dentDamageButton = buttonMap.get(new DentDamageKey(carArea,
                            damageClassifier, dentSize));

                    if (dentDamageButton == null) {
                        continue;
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dentDamageButton.performClick();
                        }

                    });

                    getInstrumentation().waitForIdleSync();
                }
            }
        }
    }


}
