package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import me.chrislewis.mentorship.models.User;

public class FavoritesFragment extends Fragment {

    SharedViewModel model;
    User user;

    FavoritesAdapter adapter;
    RecyclerView rvFavorites;
    SearchView sv;

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

        adapter = new FavoritesAdapter(user.getFavorites(), model);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvFavorites.setAdapter(adapter);

        sv = view.findViewById(R.id.search_view);

        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.equals("")) {
                    adapter.clear();
                    adapter.addAll(user.getFavorites());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getFilter().filter(query);
                }
                return false;
            }
        });
    }
}
