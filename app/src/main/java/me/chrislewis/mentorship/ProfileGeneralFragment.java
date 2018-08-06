package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.chrislewis.mentorship.models.User;

public class ProfileGeneralFragment extends Fragment {
    TextView tvSkills;
    TextView tvSummary;
    TextView tvEdu;
    SharedViewModel model;
    User user;

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
        tvSkills = view.findViewById(R.id.tvSkills);
        tvSummary = view.findViewById(R.id.tvSummary);
        //populateInfo();
    }

    private void populateInfo() {
        if (user.getSkills() != null) {
            tvSkills.setText("Skills: " + user.getSkills());
        }
        if (user.getSummary() != null) {
            tvSummary.setText("Summary: " + user.getSummary());
        }
        if (user.getEducation() != null) {
            tvEdu.setText("Education: " + user.getEducation());
        }
    }
}
