package me.chrislewis.mentorship;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.parse.ParseACL;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.File;

import me.chrislewis.mentorship.models.User;
public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private int REQUEST_LOCATION = 10;

    SharedViewModel model;

    GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private FragmentTransaction fragmentTransaction;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final SearchFragment searchFragment = new SearchFragment();
    final CalendarFragment calendarFragment = new CalendarFragment();
    final MessageListFragment messageFragment = new MessageListFragment();
    final ProfileFragment profileFragment = new ProfileFragment();
    final ComposeReviewFragment composeReviewFragment = new ComposeReviewFragment();
    final FavoritesFragment favoritesFragment = new FavoritesFragment();

    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_IMAGE_REQUEST = 1;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    File photoFile;

    ParseGeoPoint parseLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public android.support.v7.widget.Toolbar toolbar;

    public ActionBar actionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_favorites:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, favoritesFragment).commit();
                    return true;
                case R.id.navigation_search:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    fragmentTransaction.replace(R.id.flContainer, searchFragment).commit();
                    return true;
                case R.id.navigation_messages:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
                    return true;
                case R.id.navigation_calendar:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    fragmentTransaction.replace(R.id.flContainer, calendarFragment, "CalendarFragment").addToBackStack(null).commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    fragmentTransaction.replace(R.id.flContainer, profileFragment).commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        User currentUser = new User(ParseUser.getCurrentUser());
        currentUser.getFavorites();
        currentUser.getMatches();
        currentUser.getChats();
        ParseACL parseACL = new ParseACL(ParseUser.getCurrentUser());
        parseACL.setPublicReadAccess(true);

        ParseUser.getCurrentUser().setACL(parseACL);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.setFragmentManager(fragmentManager);
        model.setFragmentTransaction(fragmentTransaction);
        model.setCurrentUser(currentUser);


        setupLocationServices();

        Intent getI = getIntent();
        if (getI.hasExtra("fromSignUp")){
            fragmentTransaction.replace(R.id.flContainer, profileFragment).commit();
        }
        else {
            fragmentTransaction.replace(R.id.flContainer, favoritesFragment).commit();

        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void setupLocationServices (){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                model.setLocation(parseLocation);
                ParseUser parseUser = ParseUser.getCurrentUser();
                if (parseUser != null) {
                    User currentUser = new User(ParseUser.getCurrentUser());
                    currentUser.setLocation(parseLocation);
                    currentUser.saveInBackground();
                }
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }

}

