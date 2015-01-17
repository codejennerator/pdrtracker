package rmg.pdrtracker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import rmg.pdrtracker.job.model.JobModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JobDao {

    private final PdrTrackerDbHelper dbHelper;
    private SQLiteDatabase database;

    public JobDao(Context context) {
        dbHelper = new PdrTrackerDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public List<JobModel> selectAll() {

        List<JobModel> jobList = new ArrayList<JobModel>(10);

        Cursor cursor = database.query(JobTable.TABLE_NAME, new String[]{"id", "job_obj"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            byte[] jobObjBytes = cursor.getBlob(1);
            try {
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(jobObjBytes));
                JobModel jobModel = (JobModel) in.readObject();
                jobModel.setId(id);
                jobList.add(jobModel);
            } catch (Exception e) {
                throw new RuntimeException("Could not deserialize job model.", e);
            }
            cursor.moveToNext();
        }

        return jobList;
    }

    public void deleteAll() {
        String delSql = "delete from jobs";
        SQLiteStatement delStmt = database.compileStatement(delSql);
        delStmt.execute();
    }

    public void saveJob(JobModel jobModel) {
        if (jobModel.getId() == -1) {
            insertJob(jobModel);
            return;
        }

        updateJob(jobModel);
    }

    public void insertJob(JobModel jobModel) {
            SQLiteStatement sqlStmt = database.compileStatement("insert into jobs (job_obj) values(?)");
            sqlStmt.bindBlob(1, getJobAsBytes(jobModel));
            sqlStmt.executeInsert();
    }

    private void updateJob(JobModel jobModel) {
            SQLiteStatement sqlStmt = database.compileStatement("update jobs set job_obj = ? where id = ?");
            sqlStmt.bindBlob(1, getJobAsBytes(jobModel));
            sqlStmt.bindLong(2, jobModel.getId());
            sqlStmt.executeUpdateDelete();
    }

    private byte[] getJobAsBytes(JobModel jobModel) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(jobModel);
            return byteStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize job model", e);
        }

    }

}
