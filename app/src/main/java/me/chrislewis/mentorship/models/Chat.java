package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;

@ParseClassName("Chat")
public class Chat extends ParseObject{

    private final static String USERS_KEY = "users";
    private final static String OBJECT_ID_KEY = "objectId";

    ArrayList<User> recipients;

    String objectId;

    public Chat() {}

    public Chat(ArrayList<User> users) {
        for(User i : users) {
            i.getParseUser().revert();
            addUnique(USERS_KEY, i.getParseUser());
        }
        objectId = getString(OBJECT_ID_KEY);
    }

    public ArrayList<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<User> recipients) {
        this.recipients = recipients;
    }

    public ArrayList<User> getUsers() {
        ArrayList<ParseUser> parseUsers = (ArrayList<ParseUser>) get(USERS_KEY);
        ArrayList<User> users = new ArrayList<>();
        for (ParseUser i : parseUsers) {
            users.add(new User(i));
        }
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        for(User i : users) {
            i.getParseUser().revert();
            addUnique(USERS_KEY, i.getParseUser());
        }
    }

    public static class Query extends ParseQuery<Chat> {
        public Query() {
            super(Chat.class);
        }

        public Query findChats(User user){
            whereContainsAll(USERS_KEY, Collections.singleton(user.getParseUser()));
            return this;
        }
    }
}
