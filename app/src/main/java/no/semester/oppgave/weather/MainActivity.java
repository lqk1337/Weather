package no.semester.oppgave.weather;
/* MainActivity class */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import no.semester.oppgave.PermissionFragment.ShowPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    DrawerLayout drawer;

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

        final Button buttonCamera = findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new buttonTakePhotoClicker());

        try {
            weather  = new FetchWeatherData();
            arrayList = weather.execute(getURL()).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("#0");
        cityTextView.setText(arrayList.get(0).name);
        descriptionTextView.setText(getString(R.string.headerForecast)+arrayList.get(0).description);
        windTextView.setText(getString(R.string.headerWind)+String.valueOf(df.format(arrayList.get(0).windSpeed)) + getString(R.string.headerMeterPerSeconds));
//        windTextView.setText(getString(R.string.headerWind)+String.valueOf(df.format(arrayList.get(0).windSpeed)) + "m/s" + arrayList.get(0).windDirection);
        tempTextView.setText(String.valueOf(df.format(arrayList.get(0).temp))+getString(R.string.headerCelsius));
        pressureTextView.setText(getString(R.string.headerPressure)+String.valueOf(arrayList.get(0).pressure)+getString(R.string.headerHPA));
        humidityTextView.setText(getString(R.string.headerHumidity)+String.valueOf(arrayList.get(0).humidity) + getString(R.string.headerPercentage));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        } else if (id == R.id.nav_permissions) {
            startActivity(new Intent(getApplicationContext(), ShowPermissions.class));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}