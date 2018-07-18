package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    ParseUser user;
    String userId;
    TextView tvName;
    TextView tvJob;
    TextView tvSkills;
    TextView tvSummary;
    ImageView ivProfile;

    final static String NAME_KEY = "name";
    final static String JOB_KEY = "job";
    final static String PROFILE_IMAGE_KEY = "profileImage";
    final static String SKILLS_KEY = "skills";
    final static String SUMMARY_KEY = "summary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvName = findViewById(R.id.tvName);
        tvJob = findViewById(R.id.tvJob);
        tvSkills = findViewById(R.id.tvSkills);
        tvSummary = findViewById(R.id.tvSummary);
        ivProfile = findViewById(R.id.ivProfile);
        userId = getIntent().getStringExtra("UserObjectId");
        Log.d("Details", userId);
        ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = objects.get(0);
                    tvName.setText(user.getString(NAME_KEY));
                    if (user.getString(JOB_KEY) != null ) {
                        tvJob.setText("Job: " + user.getString(JOB_KEY));
                    } else {
                        tvJob.setText("Job: UNLISTED");
                    }
                    if (user.getString(SKILLS_KEY) != null ) {
                        tvSkills.setText("Skills: " + user.getString(SKILLS_KEY));
                    } else {
                        tvSkills.setText("Skills: UNLISTED");
                    }
                    if (user.getString(SUMMARY_KEY) != null ) {
                        tvSummary.setText("Summary: " + user.getString(SUMMARY_KEY));
                    } else {
                        tvSummary.setText("Summary: UNLISTED");
                    }
                    Glide.with(getApplicationContext())
                            .load(user.getParseFile(PROFILE_IMAGE_KEY).getUrl())
                            .apply(new RequestOptions().circleCrop())
                            .into(ivProfile);

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
