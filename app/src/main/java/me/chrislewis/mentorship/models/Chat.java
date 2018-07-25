package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;

@ParseClassName("Chat")
public class Chat extends ParseObject{

    private final static String USERS_KEY = "users";

    ArrayList<User> users;

    public JSONArray getUsers() {
        return (JSONArray) get(USERS_KEY);
    }

    public void setUsers(ArrayList<User> users) {
        addAllUnique(USERS_KEY, users);
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
