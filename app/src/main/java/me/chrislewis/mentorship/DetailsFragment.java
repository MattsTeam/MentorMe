package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.User;

import static com.parse.Parse.getApplicationContext;

public class DetailsFragment extends Fragment {

    User currentUser;
    User user;
    TextView tvName;
    TextView tvJob;
    TextView tvSkills;
    TextView tvSummary;
    TextView tvEdu;
    ImageButton btFav;
    Button btMessage;
    ImageView ivProfile;
    Button btReviews;
    Button btComposeReview;

    SharedViewModel model;

    MessageFragment messageFragment = new MessageFragment();
    ReviewsFragment reviewsFragment = new ReviewsFragment();
    ComposeReviewFragment composeReviewFragment = new ComposeReviewFragment();

    boolean isFavorite;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private Button btnSubmit;
    float myRating;
    private TextView tvOverallRating;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        tvJob = view.findViewById(R.id.tvJob);
        tvSkills = view.findViewById(R.id.tvSkills);
        tvSummary = view.findViewById(R.id.tvSummary);
        tvEdu = view.findViewById(R.id.tvEdu);
        btFav = view.findViewById(R.id.btFav);
        btMessage = view.findViewById(R.id.btMessage);
        btReviews = view.findViewById(R.id.btReviews);
        btComposeReview = view.findViewById(R.id.btnComposeReview);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvOverallRating = view.findViewById(R.id.tvRating);

        addListenerOnRatingBar();
        addListenerOnButton();

        final SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        currentUser = model.getCurrentUser();
        user = model.getUser();
        populateInfo(user);

        btFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "You favorited this mentor", Toast.LENGTH_SHORT).show();
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

        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> users = new ArrayList<>();
                users.add(currentUser);
                users.add(user);
                model.setRecipients(users);
                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
            }
        });

        btReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, reviewsFragment).commit();
            }
        });

        btComposeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //model.setReviewedUser(user);
                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, composeReviewFragment).commit();
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
        String oRating = Double.toString(user.getOverallRating());
        if (oRating != null) {
            tvOverallRating.setText("Overall Rating " + oRating);
        }

        List<ParseUser> favUsers = currentUser.getFavorites();
        if (favUsers != null) {
            for (int i = 0; i < favUsers.size(); i++) {
                if ((user.getObjectId()).equals(favUsers.get(i).getObjectId())) {
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

        ratingBar = (RatingBar) getActivity().findViewById(R.id.rb);
        tvRatingValue = (TextView) getActivity().findViewById(R.id.tvRb);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                tvRatingValue.setText("You rated this mentor " + String.valueOf(rating));
            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) getActivity().findViewById(R.id.rb);
        btnSubmit = (Button) getActivity().findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Submitted rating: " +
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
                btnSubmit.setVisibility(View.INVISIBLE);
                //btnEditRating.setVisibility(View.VISIBLE);
            }

        });

    }
}
