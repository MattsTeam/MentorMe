package me.chrislewis.mentorship.models;

import com.parse.ParseACL;
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
    private final static String LAST_MESSAGE_KEY = "lastMessage";

    String lastMessage;

    ArrayList<User> recipients;

    String objectId;

    public Chat() {}

    public Chat(ArrayList<User> users) {
        ParseACL parseACL = new ParseACL();
        for(User i : users) {
            i.getParseUser().revert();
            addUnique(USERS_KEY, i.getParseUser());
            parseACL.setWriteAccess(i.getParseUser(), true);
            parseACL.setReadAccess(i.getParseUser(), true);
        }
        setACL(parseACL);
        objectId = getString(OBJECT_ID_KEY);
    }

    public ArrayList<User> getUsers() {
        if (recipients == null) {
            ArrayList<ParseUser> parseUsers = (ArrayList<ParseUser>) get(USERS_KEY);
            ArrayList<User> users = new ArrayList<>();
            for (ParseUser i : parseUsers) {
                users.add(new User(i));
            }
            return users;
        } else {
            return recipients;
        }
    }

    public ArrayList<ParseUser> getParseUsers() {
        return (ArrayList<ParseUser>) get(USERS_KEY);
    }

    public void setUsers(ArrayList<User> users) {
        recipients = users;
        for(User i : users) {
            i.getParseUser().revert();
            addUnique(USERS_KEY, i.getParseUser());
        }
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public static class Query extends ParseQuery<Chat> {
        public Query() {
            super(Chat.class);
        }

        public Query findChats(User user){
            whereContainsAll(USERS_KEY, Collections.singleton(user.getParseUser()));
            include(USERS_KEY);
            return this;
        }
    }
}
