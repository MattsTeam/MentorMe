package me.chrislewis.mentorship;

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

public class ProfileCategoriesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        private Button changeButton;
        private String username;
        private String password;

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
        mentorButton = view.findViewById(R.id.mentorButton);
        menteeButton = view.findViewById(R.id.menteeButton);
    }
}
