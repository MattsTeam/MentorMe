package me.chrislewis.mentorship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import me.chrislewis.mentorship.models.User;

public class ProfileFragment extends Fragment {

    FavoritesAdapter adapter;
    RecyclerView rvFavorites;

    ImageView ivProfile;
    EditText etName;
    EditText etJob;
    EditText etSkills;
    EditText etSummary;
    EditText etEdu;
    ParseFile parseFile;


    Button bLogOut;
    Button bUploadProfileImage;
    Button bTakePhoto;
    File photoFile;
    Bitmap photoBitmap;
    Button bSave;
    CalendarFragment calendarFragment;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etName = view.findViewById(R.id.etName);
        etJob = view.findViewById(R.id.etJob);
        etSkills = view.findViewById(R.id.etSkills);
        etSummary = view.findViewById(R.id.etSummary);
        etEdu = view.findViewById(R.id.etEdu);
        ivProfile = view.findViewById(R.id.ivProfile);
        calendarFragment = (CalendarFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CalendarFragment");

        user = new User(ParseUser.getCurrentUser());
        populateInfo();

        bLogOut = view.findViewById(R.id.bLogOut);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                //TODO revoke gmail credentials
                //calendarFragment.mService = null;
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        bTakePhoto = view.findViewById(R.id.bTakePhoto);
        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.launchCamera();
            }
        });

        bUploadProfileImage = view.findViewById(R.id.bUploadProfileImage);
        bUploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.launchPhotos();

            }
        });

        bSave = view.findViewById(R.id.bSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String skills = etSkills.getText().toString();
                String job = etJob.getText().toString();
                String summary = etSummary.getText().toString();
                String edu = etEdu.getText().toString();

                User mUser = new User(ParseUser.getCurrentUser());
                mUser.setName(name);
                mUser.setSkills(skills);
                mUser.setJob(job);
                mUser.setSummary(summary);
                mUser.setEducation(edu);
                if (parseFile != null) {
                    mUser.setProfileImage(parseFile);
                }
                mUser.saveInBackground();
                Toast.makeText(getActivity(), "Updated Profile", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new FavoritesAdapter(view.getContext(), user.getFavorites());

        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvFavorites.setAdapter(adapter);
    }

    public void processImageString(String uri) {
        Bitmap takenImage = BitmapFactory.decodeFile(uri);
        photoFile = new File(uri);
        Glide.with(this).load(takenImage).into(ivProfile);
        parseFile = new ParseFile(photoFile);
    }

    public void processImageBitmap(Bitmap takenImage) {
        photoBitmap = takenImage;
        Glide.with(this).load(takenImage).into(ivProfile);

        if (photoBitmap != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            parseFile = new ParseFile(bytes.toByteArray());
        }
    }

    private void populateInfo() {
        etName.setText(user.getName());
        if (user.getJob() != null ) {
            etJob.setText(user.getJob());
        }
        if (user.getSkills() != null ) {
            etSkills.setText(user.getSkills());
        }
        if (user.getSummary() != null ) {
            etSummary.setText(user.getSummary());
        }
        if (user.getEducation() != null ) {
            etEdu.setText(user.getEducation());
        }

        if (user.getProfileImage() != null) {
            Glide.with(getContext())
                    .load(user.getProfileImage().getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(ivProfile);
        }
    }



}
