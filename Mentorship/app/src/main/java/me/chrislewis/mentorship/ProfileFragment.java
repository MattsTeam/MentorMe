package me.chrislewis.mentorship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class ProfileFragment extends Fragment {
    ImageView ivProfileImage;
    Button bLogOut;
    Button bUploadProfileImage;
    Button bTakePhoto;
    File photoFile;
    Bitmap photoBitmap;
    ParseFile parseFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
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
    }

    // method that takes a string file path
    public void processImageString(String uri) {
        Bitmap takenImage = BitmapFactory.decodeFile(uri);
        photoFile = new File(uri);
        Glide.with(this).load(takenImage).into(ivProfileImage);
    }

    public void processImageBitmap(Bitmap takenImage) {
        photoBitmap = takenImage;
        Glide.with(this).load(takenImage).into(ivProfileImage);
    }

}
