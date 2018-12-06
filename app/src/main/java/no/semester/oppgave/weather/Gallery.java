package no.semester.oppgave.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Gallery extends Activity {
    RecyclerView recyclerView;
    DataBaseHelper dbHelper;
    private List picturePath;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

    }


    public void getImages() {
        dbHelper.getAllPicturePaths();
        arrayList = dbHelper.getAllPicturePaths();

        for (int i = 0; i <arrayList.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(i));
            recyclerView.;
        }
    }
}
