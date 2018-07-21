package me.chrislewis.mentorship;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //private FirebaseAuth firebaseAuth;
    private Button push;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "onReceive invoked", Toast.LENGTH_LONG).show();
        }
    };

    private FragmentTransaction fragmentTransaction;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final HomeFragment homeFragment = new HomeFragment();
    final CalendarFragment calendarFragment = new CalendarFragment();
    final MessageFragment messageFragment = new MessageFragment();
    final ProfileFragment profileFragment = new ProfileFragment();

    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_IMAGE_REQUEST = 1;
    File photoFile;

    ParseGeoPoint ParseLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, homeFragment).commit();
                    return true;
                case R.id.navigation_messages:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
                    return true;
                case R.id.navigation_calendar:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, calendarFragment).commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, profileFragment).commit();
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

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, homeFragment).commit();

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

        /*
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registered Successfully.", Toast.LENGTH_LONG);
                }
                else {
                    Toast.makeText(MainActivity.this, "Could not register", Toast.LENGTH_LONG);
                }

            }
        });
        */
        push = findViewById(R.id.btnPush);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject payload = new JSONObject();

                try {
                    payload.put("sender", ParseInstallation.getCurrentInstallation().getInstallationId());
                    payload.put("alert", "my data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HashMap<String, String> data = new HashMap<>();
                data.put("alert", "hi again");
                data.put("message", "testing");
                data.put("customData", payload.toString());

                ParseCloud.callFunctionInBackground("pingReply", data);
                Toast.makeText(MainActivity.this, "called function in background", Toast.LENGTH_LONG).show();

                Toast.makeText(MainActivity.this, "tried this too", Toast.LENGTH_SHORT).show();

                ParsePush parsePush = new ParsePush();
                ParseQuery<ParseInstallation> installationParseQuery = ParseQuery.getQuery(ParseInstallation.class);
                installationParseQuery.whereEqualTo("enable", true);
                parsePush.setMessage("a direct push from from Main Activity");

                parsePush.sendInBackground();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch(item.getItemId()) {
            case R.id.btnOpenDrawer:
                //logout;
                Toast.makeText(this, "Open Drawer", Toast.LENGTH_LONG).show();
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String imageString = photoFile.getAbsolutePath();
                profileFragment.processImageString(imageString);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
                profileFragment.processImageBitmap(bitmap);

            }
        }
    }

    public void launchPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void launchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "me.chrislewis.mentorship", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MainActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("MainActivity", "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }


}