package no.semester.oppgave.weather;
/* Database helper class */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TestDB.db";
    public static final String PICTURES_TABLE_NAME = "pictures";
    public static final String PICTURES_COLUMN_PATH = "path";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
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

    public boolean insertPicturePath (Integer id, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("path", path);
        db.insert("pictures", null, contentValues);
        return true;
    }

    public ArrayList<CreateList> getAllPicturePaths() {
        ArrayList<CreateList> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select path from pictures", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            CreateList img = new CreateList();
            img.setImage_path(res.getString(res.getColumnIndex(PICTURES_COLUMN_PATH)));
            array_list.add(img);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}