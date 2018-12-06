package no.semester.oppgave.weather;
/* class that lets the user take a photo with their native photo application */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class CameraActivity extends Activity {
    private static final int CAM_REQUEST=1313;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        new DataBaseHelper(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Integer counter = 0;
        String path = Environment.getExternalStorageDirectory().toString();

        if(resultCode != RESULT_CANCELED){
            for (int i= 0; i < Environment.getExternalStorageDirectory().length(); i++) {

                File tmpDir = new File(path, "picture" + i + ".jpg");
                boolean exists = tmpDir.exists();

                if (exists) {
                    counter++;
                }
            }
            if(requestCode == CAM_REQUEST) {
                OutputStream fOut = null;
                File file = new File(path, "picture" + counter + ".jpg");

                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(Objects.requireNonNull(data.getExtras()).get("data") != null) {
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    }
                }

                try {
                    if (fOut != null) {
                        fOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                addPictureRefToDatabase(file);
            }
        }
        startActivity(new Intent(CameraActivity.this, MainActivity.class));
    }

    public void addPictureRefToDatabase(File file) {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        dbHelper.insertPicturePath(null, file.getAbsolutePath());
    }
}
