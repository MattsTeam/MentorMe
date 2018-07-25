package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

import static com.parse.Parse.getApplicationContext;

public class DetailsFragment extends Fragment {

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

    boolean isFavorite;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        ivProfile = view.findViewById(R.id.ivProfile);

        currentUser = new User (ParseUser.getCurrentUser());

        SharedViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(SharedViewModel.class);

        userId = model.getUserId();
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
}
