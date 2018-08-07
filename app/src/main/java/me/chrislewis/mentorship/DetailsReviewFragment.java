package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class DetailsReviewFragment extends Fragment {

    SharedViewModel model;
    User user;

    ReviewAdapter adapter;
    List<Review> reviews;
    RecyclerView rvReviews;

    Button writeReviewButton;
    ComposeReviewFragment composeReviewFragment = new ComposeReviewFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getUser();

        reviews = new ArrayList<>();
        adapter = new ReviewAdapter(reviews);

        getUserReviews();

        rvReviews = view.findViewById(R.id.rvDetailsReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvReviews.setAdapter(adapter);

        writeReviewButton = view.findViewById(R.id.writeReviewButton);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeReviewFragment composeReviewFragment = ComposeReviewFragment.newInstance();
                composeReviewFragment.show(getFragmentManager(), null);
            }
        });

    }

    public void getUserReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.whereEqualTo("userId", user.getObjectId());
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    if (objects.size() != 0) {
                        Collections.reverse(objects);
                        adapter.addAll(objects);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getActivity(), "This user has no reviews.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

}
