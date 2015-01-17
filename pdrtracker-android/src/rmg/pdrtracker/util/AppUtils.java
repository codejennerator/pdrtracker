package rmg.pdrtracker.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;


public class AppUtils {

    private static AppUtils singleton;

    private final Activity activity;

    private final DisplayMetrics displayMetrics;

    private AppUtils(Activity activity) {
        this.activity = activity;

        displayMetrics = activity.getResources().getDisplayMetrics();
    }

    public static AppUtils newInstance(Activity context) {
        singleton = new AppUtils(context);
        return singleton;
    }

    /**
     * Returns the screen width for the current device.
     */
    public static int getScreenWidth() {
        return singleton.activity.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Converts pixels into device independent pixels.
     */
    public static int toDip(int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, singleton.displayMetrics);
    }

    /**
     * Returns a string for the given resource id.
     */
    public static String getString(int resourceId) {
        return singleton.activity.getString(resourceId);
    }

    public static int getInt(int resourceId) {
        return singleton.activity.getResources().getInteger(resourceId);
    }

    /**
     * Looks up the resource string for the enumeration based on the name:
     *
     * R.string.$enumerationName + _ + $suffix
     *
     */
    public static String getLabelForEnum(Enum enumer, String suffix) {
        String packageName = singleton.activity.getPackageName();
        String idStr = packageName + ":string/" + enumer.toString().toLowerCase() + "_" + suffix;

        Resources resources = singleton.activity.getResources();
        int id = resources.getIdentifier(idStr, null, null);
        if (id == 0) {
            throw new RuntimeException("Could not find resource id for name: " + idStr);
        }

        return resources.getString(id);
    }

    public static void runOnUiThread(Runnable runnable) {
        singleton.activity.runOnUiThread(runnable);
    }

}
