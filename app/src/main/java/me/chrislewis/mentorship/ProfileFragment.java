package me.chrislewis.mentorship;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.Objects;

import me.chrislewis.mentorship.models.Camera;
import me.chrislewis.mentorship.models.User;

import static android.app.Activity.RESULT_OK;
import static me.chrislewis.mentorship.MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static me.chrislewis.mentorship.MainActivity.PICK_IMAGE_REQUEST;

public class ProfileFragment extends Fragment {

    FavoritesAdapter adapter;
    RecyclerView rvFavorites;

    ImageView ivProfile;
    TextView tvName;
    TextView tvJob;
    TextView tvSkills;
    TextView tvSummary;
    TextView tvEdu;

    Button bLogOut;
    ImageButton bEdit;
    Button bSave;
    CalendarFragment calendarFragment;
    EditProfileFragment editProfileFragment;

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
        tvName = view.findViewById(R.id.tvName);
        tvJob = view.findViewById(R.id.tvJob);
        tvSkills = view.findViewById(R.id.tvSkills);
        tvSummary = view.findViewById(R.id.tvSummary);
        tvEdu = view.findViewById(R.id.tvEdu);
        tvEdu.setEnabled(false);
        ivProfile = view.findViewById(R.id.ivProfile);
        calendarFragment = (CalendarFragment) Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .findFragmentByTag("CalendarFragment");

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getCurrentUser();
        camera = new Camera(getContext(), this, model);

        populateInfo();


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
                //TODO revoke gmail credentials
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void populateInfo() {
        tvName.setText(user.getName());
        if (user.getJob() != null) {
            tvJob.setText("Job: " + user.getJob());
        }
        if (user.getSkills() != null) {
            tvSkills.setText("Skills: " + user.getSkills());
        }
        if (user.getSummary() != null) {
            tvSummary.setText("Summary: " + user.getSummary());
        }
        if (user.getEducation() != null) {
            tvEdu.setText("Education: " + user.getEducation());
        }
        if (user.getProfileImage() != null) {
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(user.getProfileImage().getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(ivProfile);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(camera.getPhoto())
                        .apply(new RequestOptions().circleCrop())
                        .into(ivProfile);
            } else {
                Toast.makeText(getContext(),
                        "Picture wasn't taken!",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(camera.getChosenPhoto(data))
                        .apply(new RequestOptions().circleCrop())
                        .into(ivProfile);

            }
        }

    }
}
