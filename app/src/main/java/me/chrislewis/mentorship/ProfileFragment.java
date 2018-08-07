package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Objects;

import me.chrislewis.mentorship.models.Camera;
import me.chrislewis.mentorship.models.User;

public class ProfileFragment extends Fragment {
    ImageView ivProfile;
    TextView tvName;
    TextView tvJob;
    TextView tvRating;

    ImageButton bLogOut;
    ImageButton bEdit;
    Button bSave;
    CalendarFragment calendarFragment;
    EditProfileFragment editProfileFragment;
    BottomNavigationView bottomNavigationView;
    int checkedItemId;

    User user;
    private SharedViewModel model;

    private Camera camera;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getCurrentUser();
        calendarFragment = (CalendarFragment) Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .findFragmentByTag("CalendarFragment");

        ivProfile = view.findViewById(R.id.ivProfile);

        tvName = view.findViewById(R.id.tvName);
        tvJob = view.findViewById(R.id.tvJob);
        tvRating = view.findViewById(R.id.tvRating);
        String name = user.getName();
        if (name != null) {
            tvName.setText(name);
        }
        String job = user.getJob();
        if (job != null) {
            tvJob.setText(job);
        }

        double rating = user.getOverallRating();
        if (rating != 0) {
            tvRating.setText(String.valueOf(rating));
        }

        bEdit = view.findViewById(R.id.bEdit);

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                editProfileFragment = new EditProfileFragment();
                fragmentTransaction.replace(R.id.flContainer, editProfileFragment).commit();
            }
        });


        bLogOut = view.findViewById(R.id.bLogOut);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        bottomNavigationView = view.findViewById(R.id.nb_profile);

        if (checkedItemId == R.id.nb_categories) {
            ProfileCategoriesFragment profileCategoriesFragment = new ProfileCategoriesFragment();
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flProfile, profileCategoriesFragment).commit();
        } else if (checkedItemId == R.id.nb_reviews) {
            ProfileReviewsFragment profileReviewsFragment = new ProfileReviewsFragment();
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flProfile, profileReviewsFragment).commit();
        } else {
            ProfileGeneralFragment profileGeneralFragment = new ProfileGeneralFragment();
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flProfile, profileGeneralFragment).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction;
                switch (menuItem.getItemId()) {
                    case R.id.nb_general:
                        ProfileGeneralFragment profileGeneralFragment = new ProfileGeneralFragment();
                        fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flProfile, profileGeneralFragment).commit();
                        checkedItemId = R.id.nb_general;
                        return true;
                    case R.id.nb_categories:
                        ProfileCategoriesFragment profileCategoriesFragment = new ProfileCategoriesFragment();
                        fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flProfile, profileCategoriesFragment).commit();
                        checkedItemId = R.id.nb_categories;
                        return true;
                    case R.id.nb_reviews:
                        ProfileReviewsFragment profileReviewsFragment = new ProfileReviewsFragment();
                        fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flProfile, profileReviewsFragment).commit();
                        checkedItemId = R.id.nb_reviews;
                        return true;
                }
                return false;
            }
        });

    }


}
