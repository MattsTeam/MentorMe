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

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class MessageListFragment extends Fragment {

    RecyclerView rvPeople;
    PeopleAdapter adapter;

    ArrayList<Chat> chats = new ArrayList<>();

    SharedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        adapter = new PeopleAdapter(view.getContext(), chats, model);

        rvPeople = view.findViewById(R.id.rvPeople);
        rvPeople.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPeople.setAdapter(adapter);

        findChats(model.getCurrentUser());
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
            }
        });
    }
}
