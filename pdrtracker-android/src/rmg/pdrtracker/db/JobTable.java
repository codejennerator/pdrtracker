package rmg.pdrtracker.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JobTable implements DbTable {

    private final static String LOGTAG = JobTable.class.getName();

    public final static String TABLE_NAME = "jobs";
    public final static String ID_COLUMN = "id";
    public final static String JOB_OBJ_COLUMN = "job_obj";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOGTAG, "Created table: " + TABLE_NAME);
        String createTable = "create table " + TABLE_NAME + "(" + ID_COLUMN + " integer primary key autoincrement, " +
                JOB_OBJ_COLUMN + " blob not null);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOGTAG, "Dropping Job table.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
