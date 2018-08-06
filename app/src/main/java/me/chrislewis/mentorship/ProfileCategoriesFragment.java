package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class ProfileCategoriesFragment extends Fragment {

    SharedViewModel model;
    User user;
    private ImageButton languagesButton;
    private ImageView ivLanguages;
    private ImageButton artButton;
    private ImageView ivArt;
    private ImageButton engineeringButton;
    private ImageView ivEngineering;
    private ImageButton sportsButton;
    private ImageView ivSports;
    private ImageButton humanitiesButton;
    private ImageView ivHumanities;
    private ImageView sciencesButton;
    private ImageView ivSciences;
    private Button mentorButton;
    private Button menteeButton;
    private Button changeCategoriesButton;
    List<String> categories;
    ArrayList<String> newCategories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getCurrentUser();
        categories = user.getCategories();

        setupButtons(view);

    }

    public void setupButtons(View view) {
        engineeringButton = view.findViewById(R.id.engineeringButton);
        ivEngineering = view.findViewById(R.id.ivEngineering);
        artButton = view.findViewById(R.id.artButton);
        ivArt = view.findViewById(R.id.ivArt);
        sportsButton = view.findViewById(R.id.sportsButton);
        ivSports = view.findViewById(R.id.ivSport);
        sciencesButton = view.findViewById(R.id.scienceButton);
        ivSciences = view.findViewById(R.id.ivSciences);
        humanitiesButton = view.findViewById(R.id.humanitiesButton);
        ivHumanities = view.findViewById(R.id.ivHumanities);
        languagesButton = view.findViewById(R.id.languagesButton);
        ivLanguages = view.findViewById(R.id.ivLanguages);
        changeCategoriesButton = view.findViewById(R.id.btnCategories);
        changeCategoriesButton.setVisibility(View.INVISIBLE);

        if (categories != null) {
            if (categories.contains("Engineering")) {
                engineeringButton.setSelected(true);
                ivEngineering.setImageResource(R.drawable.ic_engineering_pressed);
            }
            if (categories.contains("Art")) {
                artButton.setSelected(true);
                ivArt.setImageResource(R.drawable.ic_art_pressed);
            }
            if (categories.contains("Sports")) {
                sportsButton.setSelected(true);
                ivSports.setImageResource(R.drawable.ic_sport_pressed);
            }
            if (categories.contains("Sciences")) {
                sciencesButton.setSelected(true);
                ivSciences.setImageResource(R.drawable.ic_sciences_pressed);
            }
            if (categories.contains("Humanities")) {
                humanitiesButton.setSelected(true);
                ivHumanities.setImageResource(R.drawable.ic_humanities_pressed);
            }
            if (categories.contains("Languages")) {
                languagesButton.setSelected(true);
                ivLanguages.setImageResource(R.drawable.ic_languages_pressed);
            }

        }

        engineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engineeringButton.setSelected(!engineeringButton.isSelected());
                if(engineeringButton.isSelected()) {
                    ivEngineering.setImageResource(R.drawable.ic_engineering_pressed);
                }
                else {
                    ivEngineering.setImageResource(R.drawable.ic_engineering);
                }
                changeCategoriesButton.setVisibility(View.VISIBLE);
            }
        });

        artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artButton.setSelected(!artButton.isSelected());
                if(artButton.isSelected()) {
                    ivArt.setImageResource(R.drawable.ic_art_pressed);
                }
                else {
                    ivArt.setImageResource(R.drawable.ic_art);
                }
            }
        });

        humanitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                humanitiesButton.setSelected(!humanitiesButton.isSelected());
                if(humanitiesButton.isSelected()) {
                    ivHumanities.setImageResource(R.drawable.ic_humanities_pressed);
                }
                else {
                    ivHumanities.setImageResource(R.drawable.ic_humanities);
                }

                changeCategoriesButton.setVisibility(View.VISIBLE);
            }
        });

        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportsButton.setSelected(!sportsButton.isSelected());
                if(sportsButton.isSelected()) {
                    ivSports.setImageResource(R.drawable.ic_sport_pressed);
                }
                else {
                    ivSports.setImageResource(R.drawable.ic_sport);
                }

                changeCategoriesButton.setVisibility(View.VISIBLE);
            }
        });

        sciencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sciencesButton.setSelected(!sciencesButton.isSelected());
                if(sciencesButton.isSelected()) {
                    ivSciences.setImageResource(R.drawable.ic_sciences_pressed);
                }
                else {
                    ivSciences.setImageResource(R.drawable.ic_sciences);
                }

                changeCategoriesButton.setVisibility(View.VISIBLE);
            }
        });

        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagesButton.setSelected(!languagesButton.isSelected());
                if(languagesButton.isSelected()) {
                    ivLanguages.setImageResource(R.drawable.ic_languages_pressed);
                }
                else {
                    ivLanguages.setImageResource(R.drawable.ic_languages);
                }

                changeCategoriesButton.setVisibility(View.VISIBLE);
            }
        });

        changeCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategoryInfo();
            }
        });


    }

    private void saveCategoryInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User newUser = new User(currentUser);

        newCategories = new ArrayList<String>();

        if(artButton.isSelected()) {
            newCategories.add("Art");
        }
        if(sportsButton.isSelected()) {
            newCategories.add("Sports");
        }
        if(humanitiesButton.isSelected()) {
            newCategories.add("Humanities");
        }
        if(languagesButton.isSelected()) {
            newCategories.add("Languages");
        }
        if(engineeringButton.isSelected()) {
            newCategories.add("Engineering");
        }
        if(sciencesButton.isSelected()) {
            newCategories.add("Sciences");
        }

        categories.retainAll(newCategories);
        for (String category : newCategories) {
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }
        newUser.setCategories(categories);
        newUser.saveInBackground();
        Toast.makeText(getParentFragment().getActivity(), "Your categories have been updated", Toast.LENGTH_SHORT).show();
        changeCategoriesButton.setVisibility(View.INVISIBLE);
    }
}
