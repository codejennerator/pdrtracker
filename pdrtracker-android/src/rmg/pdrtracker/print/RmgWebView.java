package rmg.pdrtracker.print;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

public class RmgWebView extends WebView {

    private boolean doneWithLayout;

    private List<LayoutListener> layoutListnerList = new ArrayList<LayoutListener>(2);

    public RmgWebView(Context context) {
        super(context);
    }

    public void addLayoutListener(LayoutListener layoutListener) {
        layoutListnerList.add(layoutListener);
    }

    @Override
    public void invalidate() {
        doneWithLayout = false;
        waitForLayoutFinish();
        super.invalidate();
        doneWithLayout = true;
    }

    /**
     * Allows for knowing when it's possible to capture a picture without using the deprecated PictureListener.
     */
    private void waitForLayoutFinish() {
        new AsyncTask<Object, Integer, Long>() {
            @Override
            protected Long doInBackground(Object... objects) {

                while (!doneWithLayout) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }

                notifyLayoutListeners();
                return null;
            }

            private void notifyLayoutListeners() {
                for (LayoutListener listener : layoutListnerList) {
                    listener.layoutComplete(RmgWebView.this);
                }
            }
        }.execute((Object[]) null);
    }

}
