package me.chrislewis.mentorship;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseGeoPoint;

public class MainActivity extends AppCompatActivity {

    private Button btnUpdateLocation;
    private TextView mTextMessage;
    private FragmentTransaction fragmentTransaction;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final HomeFragment homeFragment = new HomeFragment();
    final MessageFragment messageFragment = new MessageFragment();
    private String TAG = "yay";
    public Location myLocation;
    ParseGeoPoint ParseLocation;


    private LocationManager locationManager;
    private LocationListener locationListener;

    /*

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
*/

    /*
    // location-related APIs
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    */

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, homeFragment).commit();
                    return true;
                case R.id.navigation_favorites:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_messages:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
                    return true;
                case R.id.navigation_calendar:
                    mTextMessage.setText("Calendar");
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    return true;
            }
            return false;
        }
    };
    private int REQUEST_LOCATION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, homeFragment).commit();

        btnUpdateLocation = (Button) findViewById(R.id.btnUpdateLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ParseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });

    }
}