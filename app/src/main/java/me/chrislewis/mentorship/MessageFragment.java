package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.Message;
import me.chrislewis.mentorship.models.User;

public class MessageFragment extends Fragment {

    boolean firstLoad = true;
    boolean firstMessage = true;

    RecyclerView rvMessages;
    ArrayList<Message> mMessages;
    MessageAdapter adapter;

    User user;
    ArrayList<String> recipients = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(SharedViewModel.class);
        recipients = model.getRecipientIds();
        user = model.getCurrentUser();

        firstMessage = user.firstChat(recipients);

        etMessage = view.findViewById(R.id.etMessage);
        bSend = view.findViewById(R.id.bSend);
        rvMessages = view.findViewById(R.id.rvMessages);
        mMessages = new ArrayList<>();

        adapter = new MessageAdapter(view.getContext(), user.getParseUser(), mMessages);
        rvMessages.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(linearLayoutManager);

        bSend = view.findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMessage.getText().toString().equals("")) {
                    String data = etMessage.getText().toString();
                    Message message = new Message();
                    message.setBody(data);
                    message.setUser(user.getParseUser());
                    message.addRecipient(recipients);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("Messages", "Working");
                                if(firstMessage) {
                                    Chat chat = new Chat(recipients);
                                    chat.saveInBackground();
                                }
                            } else {
                                Log.d("Messages", "Fail "+ e);
                            }
                        }
                    });

                    HashMap<String, String> payload = new HashMap<>();
                    payload.put("customData", "New message: " + data);
                    ParseCloud.callFunctionInBackground("pingReply", payload);
                    Toast.makeText(getActivity(), "A notification was sent to your mentor", Toast.LENGTH_LONG).show();

                    etMessage.setText(null);
                }
            }
        });
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    void refreshMessages() {
        Message.Query query = new Message.Query();
        query.getTop();
        query.withUser();
        query.recipients(recipients);
        query.findInBackground(new FindCallback<Message>() {
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
                    Log.e("message", "Error Loading Messages " + e);
                }
            }
        });
    }

}
