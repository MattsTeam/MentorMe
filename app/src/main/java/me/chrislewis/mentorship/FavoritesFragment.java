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

import java.util.List;
import java.util.Objects;

import me.chrislewis.mentorship.models.Match;
import me.chrislewis.mentorship.models.User;

public class FavoritesFragment extends Fragment {

    SharedViewModel model;
    User user;
    boolean isMentor;

    FavoritesAdapter adapter;
    RecyclerView rvFavorites;
    List<User> favorites;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);

        user = model.getCurrentUser();
        favorites = user.getFavorites();

        isMentor = user.getIsMentor();

        adapter = new FavoritesAdapter(user.getFavorites(),user.getMatches(), model);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvFavorites.setAdapter(adapter);

        if(isMentor) {
          searchInvites();
        }
    }

    void searchInvites() {
        Match.Query query = new Match.Query();
        query.findMatches(user);
        query.findInBackground(new FindCallback<Match>() {
            @Override
            public void done(List<Match> objects, ParseException e) {
                user.clearMatch();
                adapter.clear();
                for(int i = 0; i < objects.size(); i++) {
                    user.addMatch(objects.get(i));
                    adapter.notifyItemInserted(objects.size() - 1);
                }
            }
        });
    }
}
