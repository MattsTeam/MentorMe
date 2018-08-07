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

public class ProfileGeneralFragment extends Fragment {
    TextView tvSummary;
    TextView tvEdu;
    TextView tvDescription;
    TextView tvOrganization;
    SharedViewModel model;
    User user;

    RecyclerView rvSkills;
    SkillsAdapter skillsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getParentFragment().getActivity()).get(SharedViewModel.class);
        user = model.getCurrentUser();

        tvEdu = view.findViewById(R.id.tvEdu);
        rvSkills = view.findViewById(R.id.rvSkills);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvOrganization = view.findViewById(R.id.tvOrganization);
        populateInfo();
    }

    private void populateInfo() {
        List<String> skills = user.getSkillsList();
        if(skills != null) {
            skillsAdapter = new SkillsAdapter(skills);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            rvSkills.setLayoutManager(flexboxLayoutManager);
            rvSkills.setAdapter(skillsAdapter);
        }
        if (user.getEducation() != null) {
            tvEdu.setText(user.getEducation());
        }
        if (user.getDescription() != null) {
            tvDescription.setText(user.getDescription());
        }
        if (user.getOrganization() != null) {
            tvOrganization.setText(user.getOrganization());
        }
    }
}
