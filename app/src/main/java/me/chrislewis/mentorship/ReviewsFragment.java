package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.ParseUser;

import java.util.ArrayList;

import me.chrislewis.mentorship.models.User;

public class ReviewsFragment extends Fragment {
    private User currentUser;
    private SharedViewModel model;

    private ReviewAdapter adapter;
    private ArrayList<ParseUser> reviewers;
    private RecyclerView rvReviews;

    private ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = view.findViewById(R.id.pbLoading);
        ParseUser.getCurrentUser().fetchInBackground();
        currentUser = new User(ParseUser.getCurrentUser());


        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        adapter = new ReviewAdapter(currentUser.getReviewers(), model);

        rvReviews = view.findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvReviews.setAdapter(adapter);

        /*

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReviews();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
                */

        //getReviews();
    }
    /*
    public void getReviews() {
        pb.setVisibility(ProgressBar.VISIBLE);
        adapter.clear();
        ParseUser.getCurrentUser().fetchInBackground();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("isMentor", true);
        query.whereNotEqualTo("objectId", currentUser.getObjectId());
    }
    */

}
