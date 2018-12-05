//Husk å fikse permissions. Første gang man starter programmet kræsjer det, til man har godtatt location.


package no.semester.oppgave.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView cityTextView;
    TextView descriptionTextView;
    TextView windTextView;
    TextView tempTextView;
    TextView pressureTextView;
    TextView humidityTextView;


    GPSTracker gps;
    Context mContext;
    FetchWeatherData weather;
    ArrayList<Weather> arrayList;
    private static final int CAM_REQUEST=1313;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityTextView = findViewById(R.id.cityTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        windTextView = findViewById(R.id.windTextView);
        tempTextView = findViewById(R.id.tempTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
//        new DataBaseHelper(this);

        final Button buttonCamera = findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new buttonTakePhotoClicker());


        try {

            weather  = new FetchWeatherData();
            arrayList = weather.execute(getURL()).get();
            //endre til skikkelig async fra onkel
            //onfinish-listner
            //callback

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("#0");
        cityTextView.setText(arrayList.get(0).name);
        descriptionTextView.setText(getString(R.string.headerForecast)+arrayList.get(0).description);
        windTextView.setText(getString(R.string.headerWind)+String.valueOf(df.format(arrayList.get(0).windSpeed)) + getString(R.string.headerMeterPerSeconds) + arrayList.get(0).windDirection);
        tempTextView.setText(String.valueOf(df.format(arrayList.get(0).temp))+getString(R.string.headerCelsius));
        pressureTextView.setText(getString(R.string.headerPressure)+String.valueOf(arrayList.get(0).pressure)+getString(R.string.headerHPA));
        humidityTextView.setText(getString(R.string.headerHumidity)+String.valueOf(arrayList.get(0).humidity) + getString(R.string.headerPercentage));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("hei");

        if(requestCode == CAM_REQUEST) {

            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, arrayList.get(0).name + counter + ".jpg");
            System.out.println(file.getAbsolutePath());
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
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
//            String fp = file.getAbsolutePath();
//            dbHelper.insertUser(null, fp);



        }
    }



    public String getURL(){
        String latitude;
        String longitude;
        String url;
        String apiKey =  "70bf7e30392c53b16565c6e31b5eec55";
        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Toast.makeText(mContext, "You need have granted permission", Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, MainActivity.this);
            // Get location of user to create api-call
            if (gps.canGetLocation()) {
                latitude = String.valueOf(gps.getLatitude());
                longitude = String.valueOf(gps.getLongitude());
                url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID="+ apiKey;
                return url;
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        return null;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    gps = new GPSTracker(mContext, MainActivity.this);
//                    // Check if GPS enabled
//                    if (gps.canGetLocation()) {
//                        double latitude = gps.getLatitude();
//                        double longitude = gps.getLongitude();
//                        // \n is for new line
//                        Toast.makeText(getApplicationContext(),
//                                "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                    } else {
//                        // Can't get location.
//                        // GPS or network is not enabled.
//                        // Ask user to enable GPS/network in settings.
//                        gps.showSettingsAlert();
//                    }
//                }
//                else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    class buttonTakePhotoClicker implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }
    }
}