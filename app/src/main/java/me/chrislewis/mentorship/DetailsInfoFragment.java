package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class DetailsInfoFragment extends Fragment {

    User user;
    SharedViewModel model;

    TextView detailsDescription;
    TextView detailsEducation;
    TextView detailsOrganization;

    RecyclerView rvSkills;
    SkillsAdapter skillsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getUser();

        detailsDescription = view.findViewById(R.id.tvDescription);
        detailsDescription.setText(user.getDescription());
        detailsEducation = view.findViewById(R.id.tvEducation);
        detailsEducation.setText(user.getEducation());
        detailsOrganization = view.findViewById(R.id.tvOrganization);
        detailsOrganization.setText(user.getOrganization());

        List<String> skills = user.getSkillsList();
        if(skills != null) {
            rvSkills = view.findViewById(R.id.rvSkills);
            skillsAdapter = new SkillsAdapter(skills);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            rvSkills.setLayoutManager(flexboxLayoutManager);
            rvSkills.setAdapter(skillsAdapter);
        }
    }


}
