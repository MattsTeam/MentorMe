package me.chrislewis.mentorship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import me.chrislewis.mentorship.models.User;

public class SignUpDetailsActivity extends AppCompatActivity {
    private ImageButton languagesButton;
    private ImageView ivLanguages;
    private ImageButton artButton;
    private ImageView ivArt;
    private ImageButton engineeringButton;
    private ImageView ivEngineering;
    private ImageButton sportsButton;
    private ImageView ivSports;
    private ImageButton humanitiesButton;
    private ImageView ivHumanities;
    private ImageView sciencesButton;
    private ImageView ivSciences;
    private Button mentorButton;
    private Button menteeButton;
    private Button registerButton;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        Intent intent = getIntent();
        Bundle loginInfo = intent.getExtras();
        username = loginInfo.getString("username");
        password = loginInfo.getString("password");

        engineeringButton = findViewById(R.id.engineeringButton);
        ivEngineering = findViewById(R.id.ivEngineering);
        artButton = findViewById(R.id.artButton);
        ivArt = findViewById(R.id.ivArt);
        sportsButton = findViewById(R.id.sportsButton);
        ivSports = findViewById(R.id.ivSport);
        sciencesButton = findViewById(R.id.scienceButton);
        ivSciences = findViewById(R.id.ivSciences);
        humanitiesButton = findViewById(R.id.humanitiesButton);
        ivHumanities = findViewById(R.id.ivHumanities);
        languagesButton = findViewById(R.id.languagesButton);
        ivLanguages = findViewById(R.id.ivLanguages);
        mentorButton = findViewById(R.id.mentorButton);
        menteeButton = findViewById(R.id.menteeButton);
        registerButton = findViewById(R.id.registerButton);

        engineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engineeringButton.setSelected(!engineeringButton.isSelected());
                if(engineeringButton.isSelected()) {
                    ivEngineering.setImageResource(R.drawable.ic_engineering_pressed);
                }
                else {
                    ivEngineering.setImageResource(R.drawable.ic_engineering);
                }
            }
        });

        artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artButton.setSelected(!artButton.isSelected());
                if(artButton.isSelected()) {
                    ivArt.setImageResource(R.drawable.ic_art_pressed);
                }
                else {
                    ivArt.setImageResource(R.drawable.ic_art);
                }
            }
        });

        humanitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                humanitiesButton.setSelected(!humanitiesButton.isSelected());
                if(humanitiesButton.isSelected()) {
                    ivHumanities.setImageResource(R.drawable.ic_humanities_pressed);
                }
                else {
                    ivHumanities.setImageResource(R.drawable.ic_humanities);
                }
            }
        });

        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportsButton.setSelected(!sportsButton.isSelected());
                if(sportsButton.isSelected()) {
                    ivSports.setImageResource(R.drawable.ic_sport_pressed);
                }
                else {
                    ivSports.setImageResource(R.drawable.ic_sport);
                }
            }
        });

        sciencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sciencesButton.setSelected(!sciencesButton.isSelected());
                if(sciencesButton.isSelected()) {
                    ivSciences.setImageResource(R.drawable.ic_sciences_pressed);
                }
                else {
                    ivSciences.setImageResource(R.drawable.ic_sciences);
                }
            }
        });

        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagesButton.setSelected(!languagesButton.isSelected());
                if(languagesButton.isSelected()) {
                    ivLanguages.setImageResource(R.drawable.ic_languages_pressed);
                }
                else {
                    ivLanguages.setImageResource(R.drawable.ic_languages);
                }
            }
        });

        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mentorButton.setSelected(!mentorButton.isSelected());
                if(mentorButton.isSelected()) {
                    mentorButton.setTextColor(Color.parseColor("#ff8000"));
                    if(menteeButton.isSelected()) {
                        menteeButton.setSelected(!menteeButton.isSelected());
                        menteeButton.setTextColor(Color.parseColor("#36373C"));
                    }
                }
                else {
                    mentorButton.setTextColor(Color.parseColor("#36373C"));
                }
            }
        });

        menteeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menteeButton.setSelected(!menteeButton.isSelected());
                if(menteeButton.isSelected()) {
                    menteeButton.setTextColor(Color.parseColor("#ff8000"));
                    if(mentorButton.isSelected()) {
                        mentorButton.setSelected(!mentorButton.isSelected());
                        mentorButton.setTextColor(Color.parseColor("#36373C"));
                    }
                }
                else {
                    menteeButton.setTextColor(Color.parseColor("#36373C"));
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menteeButton.isSelected() && !mentorButton.isSelected()) {
                    Toast.makeText(SignUpDetailsActivity.this, "Please select whether you'd like to be a mentor/ mentee.", Toast.LENGTH_SHORT).show();
                } else {
                    login(username, password);
                }
            }
        });
    }

    private void saveCategoryInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User newUser = new User(currentUser);

        if(artButton.isSelected()) {
            newUser.addCategory("Art");

        }
        if(sportsButton.isSelected()) {
            newUser.addCategory("Sports");
        }
        if(humanitiesButton.isSelected()) {
            newUser.addCategory("Humanities");
        }
        if(languagesButton.isSelected()) {
            newUser.addCategory("Languages");
        }
        if(engineeringButton.isSelected()) {
            newUser.addCategory("Engineering");
        }
        if(sciencesButton.isSelected()) {
            newUser.addCategory("Sciences");
        }

        newUser.saveInBackground();
    }

    public void setDefaultValues() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User newUser = new User(currentUser);

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_profile);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        ParseFile profileImage = new ParseFile(stream.toByteArray());
        newUser.setProfileImage(profileImage);

        newUser.setNumRatings(0);
        newUser.setRelDistance(1.0);
        newUser.saveInBackground();
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if(e == null) {
                    Log.d("LoginActivity", "Log in successful!");
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if(mentorButton.isSelected()) {
                        currentUser.put("isMentor", true);
                    }
                    else {
                        currentUser.put("isMentor", false);
                    }

                    setDefaultValues();
                    saveCategoryInfo();

                    Intent intent = new Intent(SignUpDetailsActivity.this, MainActivity.class);
                    intent.putExtra("fromSignUp", "signUp");
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("LoginActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
