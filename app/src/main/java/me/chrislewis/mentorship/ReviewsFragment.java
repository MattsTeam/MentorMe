package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class ReviewsFragment extends Fragment {
    private User currentUser;
    private User user;
    private SharedViewModel model;

    private ReviewAdapter adapter;
    private List<Review> reviews;
    private RecyclerView rvReviews;

    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = view.findViewById(R.id.pbLoading);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getUser();

        reviews = new ArrayList<>();
        adapter = new ReviewAdapter(reviews);

        getUserReviews();

        rvReviews = view.findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvReviews.setAdapter(adapter);
    }

    public void getUserReviews() {
        pb.setVisibility(ProgressBar.VISIBLE);

        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.whereEqualTo("userId", user.getObjectId());
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    if (objects.size() != 0) {
                        adapter.addAll(objects);
                        adapter.notifyDataSetChanged();
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                    else {
                        Toast.makeText(getActivity(), "This user has no reviews.", Toast.LENGTH_SHORT).show();
                        DetailsFragment detailsFragment = new DetailsFragment();
                        FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);
    }


}
