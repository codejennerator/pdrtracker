package rmg.pdrtracker.db;

import android.database.sqlite.SQLiteDatabase;

public interface DbTable {

    public void onCreate(SQLiteDatabase db);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
