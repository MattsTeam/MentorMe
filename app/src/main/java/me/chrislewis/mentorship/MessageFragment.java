package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.Message;
import me.chrislewis.mentorship.models.User;

public class MessageFragment extends Fragment {

    boolean firstLoad = true;

    Chat chat;

    RecyclerView rvMessages;
    ArrayList<Message> mMessages = new ArrayList<>();
    MessageAdapter adapter;

    SharedViewModel model;

    User currentUser;
    ArrayList<User> recipients = new ArrayList<>();

    EditText etMessage;
    ImageButton bSend;
    TextView tvName;

    static final int POLL_INTERVAL = 2000;
    Handler myHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        container.startAnimation(fadeIn);
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);
        recipients = model.getRecipients();
        currentUser = model.getCurrentUser();

        chat = currentUser.findChat(recipients);

        etMessage = view.findViewById(R.id.etMessage);
        bSend = view.findViewById(R.id.bSend);
        rvMessages = view.findViewById(R.id.rvMessages);
        tvName = view.findViewById(R.id.tvName);
        for(User user : recipients) {
            if (!currentUser.getObjectId().equals(user.getObjectId())) {
                try {
                    user.fetchIfNeed();
                    tvName.setText(user.getName());
                    tvName.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        adapter = new MessageAdapter(view.getContext(), currentUser.getParseUser(), mMessages);
        rvMessages.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(linearLayoutManager);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        rvMessages.setLayoutAnimation(animation);

        bSend = view.findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMessage.getText().toString().equals("")) {
                    final String data = etMessage.getText().toString();
                    Message message = new Message(data, currentUser, recipients);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("Messages", "Working");
                                HashMap<String, String> payload = new HashMap<>();
                                payload.put("customData", "New message: " + data);
                                ParseCloud.callFunctionInBackground("pingReply", payload);
                                Toast.makeText(getActivity(),
                                        "A notification was sent to your mentor",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Messages", "Fail "+ e);
                            }
                        }
                    });
                    try {
                        Log.d("Messages", "TEst");
                    } catch (Exception e) {

                    }
                    try {
                        chat.setLastMessage(data);
                        chat.setRelativeTime(System.currentTimeMillis());
                        chat.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d("Messages", "CHAT Working");
                                } else {
                                    Log.d("Messages", "CHAT Fail " + e);
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.d("Chats", "Error" + e);
                    }

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
                        runLayoutAnimation(rvMessages);
                    }
                } else {
                    Log.e("message", "Error Loading Messages " + e);
                }
            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}