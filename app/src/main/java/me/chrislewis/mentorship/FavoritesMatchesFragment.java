package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.chrislewis.mentorship.models.Match;
import me.chrislewis.mentorship.models.User;

public class FavoritesMatchesFragment extends Fragment {

    SharedViewModel model;
    User user;
    boolean isMentor;
    List<User> favorites;


    MatchAdapter matchAdapter;
    RecyclerView rvMatches;
    List<Match> matches;
    private SwipeRefreshLayout scMatches;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("FavoriteMatches", "onViewCreated FavoriteMatches");
        model = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);
        user = model.getCurrentUser();
        favorites = new ArrayList<>();

        isMentor = user.getIsMentor();

        scMatches = view.findViewById(R.id.scMatches);
        scMatches.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchMatches();
                scMatches.setRefreshing(false);
            }
        });
        scMatches.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        matches = new ArrayList<>();
        matchAdapter = new MatchAdapter(matches, model);
        rvMatches = view.findViewById(R.id.rvMatches);
        rvMatches.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMatches.setAdapter(matchAdapter);
        model.setMatchAdapter(matchAdapter);
        searchMatches();

    }

    void searchMatches() {
        Match.Query query = new Match.Query();
        query.findMatches(user);
        query.whereEqualTo("accepted", true);
        query.findInBackground(new FindCallback<Match>() {
            @Override
            public void done(List<Match> objects, ParseException e) {
                matches.clear();
                matchAdapter.clear();
                favorites.clear();
                user.clearFavorites();
                for(int i = 0; i < objects.size(); i++) {
                    matches.add(objects.get(i));
                    matchAdapter.notifyItemInserted(objects.size() - 1);
                    if (isMentor) {
                        favorites.add(objects.get(i).getMentee());
                        user.addFavorite(objects.get(i).getMentee());
                    } else {
                        favorites.add(objects.get(i).getMentor());
                        user.addFavorite(objects.get(i).getMentor());
                    }
                }
            }
        });
    }
}
