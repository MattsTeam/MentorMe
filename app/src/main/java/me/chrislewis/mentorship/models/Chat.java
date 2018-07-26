package me.chrislewis.mentorship.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

@ParseClassName("Chat")
public class Chat extends ParseObject{

    private final static String USERS_KEY = "users";
    private final static String OBJECT_ID_KEY = "objectId";

    ArrayList<User> recipients;

    String objectId;

    public Chat() {}

    public Chat(ArrayList<String> users) {
        for(String i : users) {
            addUnique(USERS_KEY, i);
        }
        objectId = getString(OBJECT_ID_KEY);
    }

    public ArrayList<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<User> recipients) {
        this.recipients = recipients;
    }

    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get(USERS_KEY);
    }

    public void setUsers(ArrayList<User> users) {
        for(User i : users) {
            addUnique("test", "word");
        }
        Log.d("Chat", "testing");
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("Chat", "Error " + e);
                }
            }
        });
    }

    public static class Query extends ParseQuery<Chat> {
        public Query() {
            super(Chat.class);
        }

        public Query findChats(User user){
            whereContains(USERS_KEY, user.getObjectId());
            return this;
        }
    }
}
