//Husk å fikse permissions. Første gang man starter programmet kræsjer det, til man har godtatt location.


package no.semester.oppgave.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    TextView cityTextView;
    TextView descriptionTextView;
    TextView windTextView;
    TextView tempTextView;
    TextView pressureTextView;
    TextView humidityTextView;
//    ImageView mImageView;

    GPSTracker gps;
    Context mContext;
    FetchWeatherData weather;
    ArrayList<Weather> arrayList;

    DataBaseHelper dbHelper;
    CameraActivity cam;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cityTextView = findViewById(R.id.cityTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        windTextView = findViewById(R.id.windTextView);
        tempTextView = findViewById(R.id.tempTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
//        mImageView = findViewById(R.id.mImageView);
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

//        String path = Environment.getExternalStorageDirectory().toString();
//        File imgFile = new File(path+"/picture" + "0" + ".jpg");
//        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//        mImageView.setImageBitmap(bitmap);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public List<String> getPicturePath() {
        List<String> picturePathList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString();
        for (int i= 0; i < Environment.getExternalStorageDirectory().length(); i++) {

            File tmpDir = new File(path, "picture" + i + ".jpg");
            boolean exists = tmpDir.exists();

            if (exists) {
                picturePathList.add(path+"picture" + i + ".jpg");
            }
        }
        return picturePathList;
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

    class buttonTakePhotoClicker implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, Gallery.class));
        } else if (id == R.id.nav_exit) {
            System.exit(-1);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}