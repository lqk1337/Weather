package no.semester.oppgave.weather;
/* class for prompting permissions at startup */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import java.util.HashMap;
import java.util.Map;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Splash extends Activity {

    private static final int PERMISSION_ALL = 0;
    private Handler h;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        };

        String[] PERMISSIONS = {
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                CAMERA,
                WRITE_EXTERNAL_STORAGE
        };

        if(!UtilPermissions.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
            h.postDelayed(r, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0)
                || (PermissionsMap.get(ACCESS_COARSE_LOCATION) != 0)
                || (PermissionsMap.get(CAMERA) != 0)
                || (PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0)){
            finish();
        }
        else
        {
            h.postDelayed(r, 1500);
        }
    }
}