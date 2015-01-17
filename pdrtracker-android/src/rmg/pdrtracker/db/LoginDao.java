package rmg.pdrtracker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import rmg.pdrtracker.login.model.LoginModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {

    private final PdrTrackerDbHelper dbHelper;
    private SQLiteDatabase database;

    public LoginDao(Context context) {
        dbHelper = new PdrTrackerDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public List<LoginModel> getUser(){

        List<LoginModel> loginList = new ArrayList<LoginModel>(10);

        Cursor cursor = database.query(LoginTable.TABLE_NAME, new String[]{"id", "login_obj"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            byte[] loginObjBytes = cursor.getBlob(1);
            try {
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(loginObjBytes));
                LoginModel loginModel = (LoginModel) in.readObject();
                loginModel.setId(id);
                loginList.add(loginModel);
            } catch (Exception e) {
                throw new RuntimeException("Could not deserialize login model.", e);
            }
            cursor.moveToNext();
        }

        return loginList;

    }

    public List<LoginModel> selectAll() {

        List<LoginModel> loginList = new ArrayList<LoginModel>(10);

        Cursor cursor = database.query(LoginTable.TABLE_NAME, new String[]{"id", "login_obj"}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            byte[] loginObjBytes = cursor.getBlob(1);
            try {
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(loginObjBytes));
                LoginModel loginModel = (LoginModel) in.readObject();
                loginModel.setId(id);
                loginList.add(loginModel);
            } catch (Exception e) {
                throw new RuntimeException("Could not deserialize login model.", e);
            }
            cursor.moveToNext();
        }

        return loginList;
    }

    public void deleteAll() {
        String delSql = "delete from login";
        SQLiteStatement delStmt = database.compileStatement(delSql);
        delStmt.execute();
    }

    public void saveLogin(LoginModel loginModel) {
        if (loginModel.getId() == -1) {
            insertLogin(loginModel);
            return;
        }

        updateLogin(loginModel);
    }

    public void insertLogin(LoginModel loginModel) {
            SQLiteStatement sqlStmt = database.compileStatement("insert into login (login_obj) values(?)");
            sqlStmt.bindBlob(1, getLoginAsBytes(loginModel));
            sqlStmt.executeInsert();
    }

    private void updateLogin(LoginModel loginModel) {
            SQLiteStatement sqlStmt = database.compileStatement("update login set login_obj = ? where id = ?");
            sqlStmt.bindBlob(1, getLoginAsBytes(loginModel));
            sqlStmt.bindLong(2, loginModel.getId());
            sqlStmt.executeUpdateDelete();
    }

    private byte[] getLoginAsBytes(LoginModel loginModel) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(loginModel);
            return byteStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize login model", e);
        }

    }

}
