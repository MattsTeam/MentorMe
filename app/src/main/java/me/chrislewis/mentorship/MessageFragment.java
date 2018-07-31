package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.chrislewis.mentorship.models.Camera;
import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.Message;
import me.chrislewis.mentorship.models.User;

import static android.app.Activity.RESULT_OK;
import static me.chrislewis.mentorship.MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static me.chrislewis.mentorship.MainActivity.PICK_IMAGE_REQUEST;

public class MessageFragment extends Fragment {

    boolean firstLoad = true;
    boolean firstMessage = true;

    RecyclerView rvMessages;
    ArrayList<Message> mMessages = new ArrayList<>();
    MessageAdapter adapter;

    SharedViewModel model;

    User user;
    ArrayList<User> recipients = new ArrayList<>();

    EditText etMessage;
    Button bSend;
    Button btImage;
    ImageView ivMessage;

    Camera camera;

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

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        recipients = model.getRecipients();
        user = model.getCurrentUser();

        firstMessage = user.firstChat(recipients);

        etMessage = view.findViewById(R.id.etMessage);
        bSend = view.findViewById(R.id.bSend);
        rvMessages = view.findViewById(R.id.rvMessages);
        ivMessage = view.findViewById(R.id.ivMessage);
        btImage = view.findViewById(R.id.btImage);

        camera = new Camera(getContext(), this, model);

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

        btImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.launchCamera();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Glide.with(getContext())
                        .load(camera.getPhoto())
                        .apply(new RequestOptions().circleCrop())
                        .into(ivMessage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                if (resultCode == RESULT_OK) {
                    Glide.with(getContext())
                            .load(camera.getChosenPhoto(data))
                            .apply(new RequestOptions().circleCrop())
                            .into(ivMessage);
                } else {
                    Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

}