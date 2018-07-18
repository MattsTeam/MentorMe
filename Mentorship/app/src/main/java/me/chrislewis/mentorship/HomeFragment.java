package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    ParseUser currentUser;
    String currentCategory;
    GridAdapter gridAdapter;
    ArrayList<ParseUser> users;
    RecyclerView rvUsers;
    ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;
    private DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseUser.getCurrentUser().fetchInBackground();
        currentUser = ParseUser.getCurrentUser();
        mDrawerLayout = view.findViewById(R.id.drawer_layout);

        NavigationView navigationView = view.findViewById(R.id.drawer_view);
        Menu menu = navigationView.getMenu();
        MenuItem cat_1 = menu.findItem(R.id.cat_1);


        currentCategory = currentUser.getString("category");
        cat_1.setTitle(currentCategory);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                // swap fragment here...
                return true;
            }
        });

        rvUsers = (RecyclerView) view.findViewById(R.id.rvGrid);

        users = new ArrayList<ParseUser>();
        gridAdapter = new GridAdapter(users);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvUsers.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        rvUsers.setAdapter(gridAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        getUsers();

    }

    public void getUsers() {
        pb.setVisibility(ProgressBar.VISIBLE);

        ParseUser.getCurrentUser().fetchInBackground();
        currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("isMentor", true);
        query.whereNotEqualTo("objectId", currentUser.getObjectId());
        query.whereEqualTo("category", currentCategory);

        try {
            List<ParseUser> sameCategoryUsers = query.find();
            for (int i = 0; i < sameCategoryUsers.size(); i++) {
                ParseUser user = sameCategoryUsers.get(i);
                double rankNum = calculateRank(user);
                user.put("rank", rankNum);
                user.saveInBackground();
            }
            Collections.sort(sameCategoryUsers, new Comparator<ParseUser>() {
                public int compare(ParseUser user1, ParseUser user2) {
                    Double distance1 = user1.getDouble("rank");
                    Double distance2 = user2.getDouble("rank");
                    return distance1.compareTo(distance2);
                }
            });
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }


/*
        ParseUser.getCurrentUser().fetchInBackground();
        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        query2.whereEqualTo("isMentor", true);
        query2.whereNotEqualTo("objectId", currentUser.getObjectId());
        currentCategory = currentUser.getString("category");
        query2.whereEqualTo("category", currentCategory);
        query2.orderByAscending("rank");

        query2.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {gridAdapter.clear();
                    users.clear();
                    Log.d("size is", String.valueOf(objects.size()));

                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser user = objects.get(i);
                        double rankNum = calculateRank(user);
                        user.put("rank", rankNum);
                        user.saveInBackground();
                        users.add(user);
                    }

                    gridAdapter.addAll(objects);
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    swipeContainer.setRefreshing(false);

                } else {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);
    }

*/

    public double calculateRank(ParseUser user) {
        double organizationRank = 0;
        double educationRank = 0;
        double distanceRank = 0.2 * user.getDouble("relativeDistance");

        if (currentUser.getString("education").equals(user.getString("education"))) {
            educationRank = 2;
        }
        if (currentUser.getString("organization").equals(user.getString("organization"))) {
            organizationRank = 3;
        }
        return organizationRank + educationRank + distanceRank;
    }





}
