package no.semester.oppgave.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.Blob;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TestDB.db";
    public static final String PICTURES_TABLE_NAME = "pictures";
    public static final String PICTURES_COLUMN_ID = "id";
    public static final String PICTURES_COLUMN_PATH = "path";
    public static final Blob PICTURES_COLUMN_IMAGE = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
//        insertUser(null, "fredrik");
//        insertUser(null, "ola");
//        insertUser(null, "håvard");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table pictures " +
                        "(id integer primary key AUTOINCREMENT, path varchar)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS pictures");
        onCreate(db);
    }

    public boolean insertUser (Integer id, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("path", path);
//        getBitmapAsByteArray(image);
//        contentValues.put("image", image);

        db.insert("pictures", null, contentValues);
        return true;
    }

//    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
//        return outputStream.toByteArray();
//    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PICTURES_TABLE_NAME);
        return numRows;
    }

    public Integer deleteUser (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pictures", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PICTURES_COLUMN_PATH)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

}