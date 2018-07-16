package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class HomeFragment extends Fragment {
    ParseUser currentUser;
    me.vanessahlyan.parstagram.GridAdapter gridAdapter;
    ArrayList<User> users;
    RecyclerView rvUsers;
    ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = ParseUser.getCurrentUser();
        rvUsers = (RecyclerView) view.findViewById(R.id.rvGrid);

        users = new ArrayList<>();
        gridAdapter = new me.vanessahlyan.parstagram.GridAdapter(users);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvUsers.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
        rvUsers.setAdapter(gridAdapter);

        getUsers();

    }

    public void getUsers() {
        pb.setVisibility(ProgressBar.VISIBLE);

        ParseQuery<User> query = ParseQuery.getQuery(User   .class);
        query.whereEqualTo("user", currentUser);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    gridAdapter.clear();
                    Collections.reverse(objects);
                    gridAdapter.addAll(objects);
                    gridAdapter.notifyDataSetChanged();
                    pb.setVisibility(ProgressBar.INVISIBLE);

                } else {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);
    }



}
