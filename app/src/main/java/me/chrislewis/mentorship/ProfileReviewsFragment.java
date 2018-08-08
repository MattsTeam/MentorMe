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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class ProfileReviewsFragment extends Fragment {
    private User user;
    private SharedViewModel model;

    private ReviewAdapter adapter;
    private List<Review> reviews;
    RecyclerView rvProfileReviews;
    private ProgressBar pb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = view.findViewById(R.id.pbLoading);
        model = ViewModelProviders.of(getParentFragment().getActivity()).get(SharedViewModel.class);
        user = model.getUser();

        reviews = new ArrayList<>();
        adapter = new ReviewAdapter(reviews);

        getUserReviews();


        rvProfileReviews = view.findViewById(R.id.rvProfileReviews);
        rvProfileReviews.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvProfileReviews.setAdapter(adapter);
    }

    public void getUserReviews() {
        pb.setVisibility(ProgressBar.VISIBLE);

        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        ParseUser parseUser = ParseUser.getCurrentUser();
        query.whereEqualTo("userId", parseUser.getObjectId());
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        adapter.clear();
                        Collections.reverse(objects);
                        adapter.addAll(objects);
                        adapter.notifyDataSetChanged();
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                    else {
                        Toast.makeText(getParentFragment().getActivity(), "You currently have no reviews.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "unable to retrieve reviews from server", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);
    }

}
