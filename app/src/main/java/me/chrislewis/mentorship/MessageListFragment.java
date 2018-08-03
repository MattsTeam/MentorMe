package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class MessageListFragment extends Fragment {

    RecyclerView rvPeople;
    PeopleAdapter adapter;
    android.support.v7.widget.SearchView sv;

    List<Chat> chats = new ArrayList<>();

    SharedViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);

        adapter = new PeopleAdapter(view.getContext(), chats, model);

        rvPeople = view.findViewById(R.id.rvPeople);
        rvPeople.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPeople.setAdapter(adapter);

        findChats(model.getCurrentUser());

        sv = view.findViewById(R.id.search_view_message_list);

        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    void findChats(User user) {
        chats.clear();
        Chat.Query query = new Chat.Query();
        query.findChats(user);
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> objects, ParseException e) {
                if (e == null) {
                    chats.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("message", "Error Loading Messages " + e);
                }
                model.getCurrentUser().setChats(objects);
            }
        });
    }
}
