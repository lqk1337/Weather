package no.semester.oppgave.weather;

import android.app.Activity;
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

        recyclerView = findViewById(R.id.gallery);

        arrayList = dbHelper.getAllPicturePaths();
        System.out.println(arrayList);
    }


}
