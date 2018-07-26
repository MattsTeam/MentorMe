package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class DetailsActivity extends AppCompatActivity {

    User currentUser;
    User user;
    String userId;
    TextView tvName;
    TextView tvJob;
    TextView tvSkills;
    TextView tvSummary;
    TextView tvEdu;
    ImageButton btFav;
    Button btMessage;
    ImageView ivProfile;

    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private Button btnSubmit;

    boolean isFavorite;

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

        ratingBar = findViewById(R.id.rb);
        tvRatingValue = findViewById(R.id.tvRb);

        currentUser = new User (ParseUser.getCurrentUser());


        userId = getIntent().getStringExtra("UserObjectId");
        ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = new User (objects.get(0));
                    populateInfo(user);
                } else {
                    e.printStackTrace();
                }
            }
        });

        addListenerOnRatingBar();
        addListenerOnButton();

        btFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite) {
                    currentUser.addFavorite(user.getParseUser());
                    currentUser.saveInBackground();
                    btFav.setBackgroundResource(R.drawable.favorite_save_active);
                    isFavorite = true;
                } else {
                    currentUser.removeFavorite(user.getParseUser());
                    currentUser.saveInBackground();
                    btFav.setBackgroundResource(R.drawable.favorite_save);
                    isFavorite = false;
                }
            }
        });
    }

    private void populateInfo(User user) {
        tvName.setText(user.getName());
        if (user.getJob() != null ) {
            tvJob.setText(String.format("Job: %s", user.getJob()));
        }
        if (user.getSkills() != null ) {
            tvSkills.setText(String.format("Skills: %s", user.getSkills()));
        }
        if (user.getSummary() != null ) {
            tvSummary.setText(String.format("Summary: %s", user.getSummary()));
        }
        if (user.getEducation() != null ) {
            tvEdu.setText(String.format("Education: %s", user.getEducation()));
        }
        List<ParseUser> favUsers = currentUser.getFavorites();
        if (favUsers != null) {
            for (int i = 0; i < favUsers.size(); i++) {
                if ((userId).equals(favUsers.get(i).getObjectId())) {
                    isFavorite = true;
                }
            }
        }
        if (isFavorite) {
            btFav.setBackgroundResource(R.drawable.favorite_save_active);
        } else {
            btFav.setBackgroundResource(R.drawable.favorite_save);
        }
        Glide.with(getApplicationContext())
                .load(user.getProfileImage().getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(ivProfile);
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.rb);
        tvRatingValue = (TextView) findViewById(R.id.tvRb);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                tvRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.rb);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(DetailsActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();

            }

        });

    }
}
