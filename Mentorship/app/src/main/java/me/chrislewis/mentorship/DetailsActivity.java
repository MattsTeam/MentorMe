package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    ParseUser currentUser;
    ParseUser user;
    String userId;
    TextView tvName;
    TextView tvJob;
    TextView tvSkills;
    TextView tvSummary;
    TextView tvEdu;
    Button btFav;
    Button btMessage;
    ImageView ivProfile;

    boolean isFavorite;

    final static String NAME_KEY = "name";
    final static String JOB_KEY = "job";
    final static String PROFILE_IMAGE_KEY = "profileImage";
    final static String SKILLS_KEY = "skills";
    final static String SUMMARY_KEY = "summary";
    final static String EDUCATION_KEY = "education";
    final static String FAVORITE_KEY = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        tvName = findViewById(R.id.tvName);
        tvJob = findViewById(R.id.tvJob);
        tvSkills = findViewById(R.id.tvSkills);
        tvSummary = findViewById(R.id.tvSummary);
        tvEdu = findViewById(R.id.tvEdu);
        btFav = findViewById(R.id.btFav);
        btMessage = findViewById(R.id.btMessage);
        ivProfile = findViewById(R.id.ivProfile);

        currentUser = ParseUser.getCurrentUser();

        userId = getIntent().getStringExtra("UserObjectId");
        ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = objects.get(0);
                    populateInfo(user);
                } else {
                    e.printStackTrace();
                }
            }
        });

        btFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Details", "test");
                if (!isFavorite) {
                    currentUser.addUnique(FAVORITE_KEY, user.getObjectId());
                    currentUser.saveInBackground();
                    isFavorite = true;
                } else {
                    currentUser.removeAll(FAVORITE_KEY, Collections.singleton(user.getObjectId()));
                    currentUser.saveInBackground();
                    isFavorite = false;
                }
            }
        });
    }

    private void populateInfo(ParseUser user) {
        tvName.setText(user.getString(NAME_KEY));
        if (user.getString(JOB_KEY) != null ) {
            tvJob.setText(String.format("Job: %s", user.getString(JOB_KEY)));
        }
        if (user.getString(SKILLS_KEY) != null ) {
            tvSkills.setText(String.format("Skills: %s", user.getString(SKILLS_KEY)));
        }
        if (user.getString(SUMMARY_KEY) != null ) {
            tvSummary.setText(String.format("Summary: %s", user.getString(SUMMARY_KEY)));
        }
        if (user.getString(EDUCATION_KEY) != null ) {
            tvSummary.setText(String.format("Education: %s", user.getString(EDUCATION_KEY)));
        }
        JSONArray favArray = ParseUser.getCurrentUser().getJSONArray(FAVORITE_KEY);
        if (favArray != null) {
            for (int i = 0; i < favArray.length(); i++) {
                try {
                    if ((user.getObjectId()).equals(favArray.getString(i))) {
                        isFavorite = true;
                        btFav.setText("FAVORITED");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Glide.with(getApplicationContext())
                .load(user.getParseFile(PROFILE_IMAGE_KEY).getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(ivProfile);
    }
}
