package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.chrislewis.mentorship.models.Match;
import me.chrislewis.mentorship.models.User;

import static com.parse.Parse.getApplicationContext;

public class DetailsFragment2 extends Fragment {

    User currentUser;
    User user;
    CircleImageView profileImage;
    ImageView location;
    TextView name;
    TextView relativeDistance;
    TextView occupation;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    ImageButton favoriteButton;
    boolean isFavorite;
    ImageButton backButton;
    Match match;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        currentUser = model.getCurrentUser();
        user = model.getUser();

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ff8000"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        profileImage = view.findViewById(R.id.profileImage);
        location = view.findViewById(R.id.ivLocation);
        name = view.findViewById(R.id.tvName);
        relativeDistance = view.findViewById(R.id.tvLocation);
        occupation = view.findViewById(R.id.tvOccupation);

        favoriteButton = view.findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite) {
                    currentUser.addFavorite(user);
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "You favorited this mentor", Toast.LENGTH_SHORT).show();
                                match = new Match(currentUser, user);
                                match.saveInBackground();
                                currentUser.addMatch(match);
                                currentUser.saveInBackground();
                            } else {
                                Log.d("Details", "Error" + e);
                            }
                        }
                    });
                    favoriteButton.setBackgroundResource(R.drawable.ic_bookmark_dark);
                    isFavorite = true;
                } else {
//                    currentUser.removeFavorite(user);
//                    currentUser.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                Toast.makeText(getActivity(), "User Unfavorited", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    favoriteButton.setBackgroundResource(R.drawable.ic_bookmark);
//                    isFavorite = false;
                }
            }
        });

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO make fragment go back
            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(model.getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        populateInfo(user);

    }

    public void populateInfo(User user) {
        name.setText(user.getName());
        relativeDistance.setText(Double.toString(user.getRelDistance()) + " mi");
        occupation.setText(user.getJob());
        Glide.with(getApplicationContext())
                .load(user.getProfileImage().getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(profileImage);
        List<User> favUsers = currentUser.getFavorites();
        if (favUsers != null) {
            for (int i = 0; i < favUsers.size(); i++) {
                if ((user.getObjectId()).equals(favUsers.get(i).getObjectId())) {
                    isFavorite = true;
                }
            }
        }
        if (isFavorite) {
            favoriteButton.setBackgroundResource(R.drawable.ic_bookmark_dark);
        } else {
            favoriteButton.setBackgroundResource(R.drawable.ic_bookmark);
        }
    }

}