package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class MessageListFragment extends Fragment {

    List<ParseUser> people;
    RecyclerView rvPeople;
    PeopleAdapter adapter;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = new User(ParseUser.getCurrentUser());

        adapter = new PeopleAdapter(view.getContext(), user.getFavorites(), user);

        rvPeople = view.findViewById(R.id.rvPeople);
        rvPeople.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPeople.setAdapter(adapter);
    }
}
