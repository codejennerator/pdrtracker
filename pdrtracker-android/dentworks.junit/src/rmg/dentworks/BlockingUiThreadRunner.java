package rmg.pdrtracker;

import android.app.Instrumentation;

public class BlockingUiThreadRunner {

    private final Instrumentation instrumentation;

    public BlockingUiThreadRunner(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void run(Runnable runnable) {
        instrumentation.runOnMainSync(runnable);
    }

}
