package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class ComposeReviewFragment extends Fragment {
    private SharedViewModel model;
    private TextView tvUser;
    private RatingBar rbCompose;
    private EditText etReviewBody;
    private Button btnSubmitReview;
    private Button btnUploadPhoto;
    private ImageView ivReviewImage;

    User currentUser;
    User reviewedUser;

    File photoFile;
    Bitmap photoBitmap;
    ParseFile parseFile;

    ReviewsFragment reviewsFragment = new ReviewsFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_review, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = ViewModelProviders.of((FragmentActivity) getActivity()).get(SharedViewModel.class);
        currentUser = model.getCurrentUser();
        reviewedUser = model.getUser();

        tvUser = view.findViewById(R.id.tvUser);
        rbCompose = view.findViewById(R.id.rbCompose);
        etReviewBody = view.findViewById(R.id.etReviewBody);
        btnSubmitReview = view.findViewById(R.id.btnSubmitReview);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);
        rbCompose = view.findViewById(R.id.rbCompose);
        ivReviewImage = view.findViewById(R.id.ivReviewImage);


        if (reviewedUser.getUsername() != null ) {
            tvUser.setText(reviewedUser.getUsername());
        } else {
            tvUser.setText("reviewed user's username");
        }


        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = etReviewBody.getText().toString();
                double rating = rbCompose.getRating();
                ParseUser otherUser = reviewedUser.getParseUser();
                String otherId = otherUser.getObjectId();
                Review review = new Review();
                review.setBody(body);
                review.setWriter(currentUser.getParseUser());
                review.setRating(Double.valueOf(rating));
                if (parseFile != null) {
                    review.setReviewPhoto(parseFile);
                }
                //review.setUser(otherUser);
                review.setUserId(otherId);

                review.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Debug Reviews", "Reviews work");
                            Toast.makeText(getActivity(), "You submitted a review for this mentor", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.flContainer, reviewsFragment).commit();
                        }
                        else {
                            Log.d("Debug Reviews", "Faile" + e);
                        }
                    }
                });
                // add currentUser to user's reviewers array
                reviewedUser.addReview(review);
                reviewedUser.addReviewer(currentUser.getParseUser());

            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.fragmentIdentifier = "compose_review";
                MainActivity activity = (MainActivity) getActivity();
                activity.launchPhotos();
            }
        });


    }

    public void processImageBitmap(Bitmap takenImage) {
        photoBitmap = takenImage;

        if (photoBitmap != null) {
            //Glide.with(this).load(takenImage).into(ivReviewImage);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            parseFile = new ParseFile(bytes.toByteArray());
        }
    }
}

