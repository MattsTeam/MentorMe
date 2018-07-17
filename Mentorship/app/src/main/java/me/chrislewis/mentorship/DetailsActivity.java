package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    ParseUser user;
    String userId;
    TextView tvName;
    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvName = findViewById(R.id.tvName);
        ivProfile = findViewById(R.id.ivProfile);
        userId = getIntent().getStringExtra("UserObjectId");
        Log.d("Details", userId);
        ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = objects.get(0);
                    tvName.setText(user.getUsername());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
