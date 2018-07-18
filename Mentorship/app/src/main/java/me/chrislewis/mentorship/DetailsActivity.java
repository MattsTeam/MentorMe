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
    TextView tvDescription;
    ImageView ivProfile;

    final static String NAME_KEY = "name";
    final static String JOB_KEY = "job";
    final static String PROFILE_IMAGE_KEY = "profileImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvName = findViewById(R.id.tvName);
        tvJob = findViewById(R.id.tvJob);
        tvSkills = findViewById(R.id.tvSkills);
        tvDescription = findViewById(R.id.tvDescription);
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
                    tvJob.setText(user.getString(JOB_KEY));
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
