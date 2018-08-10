package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

    SharedViewModel model;
    SwipeRefreshLayout swipeContainer;

    boolean firstLoad = true;

    User currentUser;

    static final int POLL_INTERVAL = 1000000;
    Handler myHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            findChats();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        container.startAnimation(fadeIn);
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);
        swipeContainer = view.findViewById(R.id.scMessageList);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findChats();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        currentUser = model.getCurrentUser();

        adapter = new PeopleAdapter(view.getContext(), currentUser.getChats(), model);

        rvPeople = view.findViewById(R.id.rvPeople);
        rvPeople.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvPeople.setAdapter(adapter);

        sv = view.findViewById(R.id.search_view_message_list);

        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.equals("")) {
                    findChats();
                } else {
                    adapter.getFilter().filter(query);
                }
                return false;
            }
        });

        findChats();
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    void findChats() {
        Chat.Query query = new Chat.Query();
        query.findChats(currentUser);
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    currentUser.addChats(objects);
                    adapter.addAll(objects);
                    if (firstLoad) {
                        rvPeople.scrollToPosition(0);
                        firstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages " + e);
                }
                model.getCurrentUser().setChats((ArrayList<Chat>) objects);
            }
        });
    }
}
