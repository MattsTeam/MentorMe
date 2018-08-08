package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.LinearLayout;
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
        //Add tab dividers
        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

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
                                Toast.makeText(getActivity(), "You sent this mentor an invitation to connect", Toast.LENGTH_SHORT).show();
                                match = new Match(currentUser, user);
                                match.saveInBackground();
                                currentUser.addMatch(match);
                                currentUser.saveInBackground();
                            } else {
                                Log.d("Details", "Error" + e);
                            }
                        }
                    });
                    favoriteButton.setBackgroundResource(R.drawable.add_user_2_24);
                    isFavorite = true;
                }
            }
        });

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                model.getFragmentManager().beginTransaction().replace(R.id.flContainer, searchFragment).commit();
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
            favoriteButton.setBackgroundResource(R.drawable.add_user_2_24);
        } else {
            favoriteButton.setBackgroundResource(R.drawable.ic_add);
        }
    }

}
