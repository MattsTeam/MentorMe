package me.chrislewis.mentorship;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.Message;

public class MessageFragment extends Fragment {

    boolean firstLoad;

    RecyclerView rvMessages;
    ArrayList<Message> mMessages;
    MessageAdapter adapter;

    ParseUser user;
    ParseQuery<Message> parseQuery;

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";

    EditText etMessage;
    Button bSend;

    static final int POLL_INTERVAL = 1000;
    Handler myHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firstLoad = true;
        etMessage = view.findViewById(R.id.etMessage);
        rvMessages = view.findViewById(R.id.rvMessages);
        user = ParseUser.getCurrentUser();
        mMessages = new ArrayList<>();
        adapter = new MessageAdapter(view.getContext(), user.getObjectId(), mMessages);
        rvMessages.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(linearLayoutManager);
//        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
//        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
//        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
//        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
//                SubscriptionHandling.HandleEventCallback<Message>() {
//                    @Override
//                    public void onEvent(ParseQuery<Message> query, Message object) {
//                        mMessages.add(0, object);
//                        Log.d("Messages", "Sent");
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                rvMessages.scrollToPosition(0);
//                            }
//                        });
//                    }
//                });
        bSend = view.findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMessage.getText().toString().equals("")) {
                    String data = etMessage.getText().toString();
                    Message message = new Message();
                    message.setBody(data);
                    message.setUserIdKey(ParseUser.getCurrentUser().getObjectId());
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("Messages", "Working");
                            } else {
                                Log.d("Messages", "Fail");
                            }
                        }
                    });
                    etMessage.setText(null);
                }
            }
        });
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    void refreshMessages() {
        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        parseQuery.setLimit(50);
        parseQuery.orderByDescending("createdAt");
        parseQuery.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    adapter.notifyDataSetChanged();
                    if (firstLoad) {
                        rvMessages.scrollToPosition(0);
                        firstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
}
