package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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
    private  Button mentorButton;
    private Button menteeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

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

        engineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engineeringButton.setSelected(!engineeringButton.isSelected());
                if(engineeringButton.isSelected()) {
                    ivEngineering.setImageResource(R.drawable.ic_engineering);
                }
                else {
                    ivEngineering.setImageResource(R.drawable.ic_engineering_pressed);
                }
            }
        });

        artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artButton.setSelected(!artButton.isSelected());
                if(artButton.isSelected()) {
                    ivArt.setImageResource(R.drawable.ic_art);
                }
                else {
                    ivArt.setImageResource(R.drawable.ic_art_pressed);
                }
            }
        });

        humanitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                humanitiesButton.setSelected(!humanitiesButton.isSelected());
                if(humanitiesButton.isSelected()) {
                    ivHumanities.setImageResource(R.drawable.ic_humanities);
                }
                else {
                    ivHumanities.setImageResource(R.drawable.ic_humanities_pressed);
                }
            }
        });

        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportsButton.setSelected(!sportsButton.isSelected());
                if(sportsButton.isSelected()) {
                    ivSports.setImageResource(R.drawable.ic_sport);
                }
                else {
                    ivSports.setImageResource(R.drawable.ic_sport_pressed);
                }
            }
        });

        sciencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sciencesButton.setSelected(!sciencesButton.isSelected());
                if(sciencesButton.isSelected()) {
                    ivSciences.setImageResource(R.drawable.ic_sciences);
                }
                else {
                    ivSciences.setImageResource(R.drawable.ic_sciences_pressed);
                }
            }
        });

        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagesButton.setSelected(!languagesButton.isSelected());
                if(languagesButton.isSelected()) {
                    ivLanguages.setImageResource(R.drawable.ic_languages);
                }
                else {
                    ivLanguages.setImageResource(R.drawable.ic_languages_pressed);
                }
            }
        });
    }
}
