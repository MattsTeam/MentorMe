package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

@ParseClassName("Chat")
public class Chat extends ParseObject{

    private final static String USERS_KEY = "users";

    User recipient;

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public ArrayList<String> getUsers() {
        return (ArrayList<String>) get(USERS_KEY);
    }

    public void setUsers(ArrayList<User> users) {
        for(User i : users) {
            addUnique(USERS_KEY, i.getObjectId());
        }
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
