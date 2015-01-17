package rmg.pdrtracker.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PdrTrackerDbHelper extends SQLiteOpenHelper {

    private final static String LOGTAG = PdrTrackerDbHelper.class.getName();

    public final static String DATABASE_NAME = "pdrtracker.db";
    public final static int DATABASE_VERSION = 1;

    public PdrTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOGTAG, "Creating database.");

        DbTable jobTable = new JobTable();
        jobTable.onCreate(db);

        DbTable loginTable = new LoginTable();
        loginTable.onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOGTAG, "Dropping all tables.");
        DbTable jobTable = new JobTable();
        jobTable.onUpgrade(db, oldVersion, newVersion);

        DbTable loginTable = new LoginTable();
        loginTable.onUpgrade(db, oldVersion, newVersion);
    }

}
