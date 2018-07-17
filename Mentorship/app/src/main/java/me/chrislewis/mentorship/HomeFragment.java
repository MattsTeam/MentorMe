package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ParseUser currentUser;
    GridAdapter gridAdapter;
    ArrayList<ParseUser> users;
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

        users = new ArrayList<ParseUser>();
        gridAdapter = new GridAdapter(users);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvUsers.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
        rvUsers.setAdapter(gridAdapter);

        getUsers();

    }

    public void getUsers() {
        pb.setVisibility(ProgressBar.VISIBLE);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("isMentor", false);
        query.whereNotEqualTo("user", currentUser);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    gridAdapter.clear();
                    users.clear();
                    Log.d("size is", String.valueOf(objects.size()));

                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser user = objects.get(i);
                        users.add(user);
                        gridAdapter.notifyItemInserted(i);
                    }
                    pb.setVisibility(ProgressBar.INVISIBLE);

                } else {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);
    }



}
