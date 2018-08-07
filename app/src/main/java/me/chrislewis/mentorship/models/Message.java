package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Message")
public class Message extends ParseObject{
    private final static String USER_KEY = "user";
    private final static String BODY_KEY = "body";
    private final static String RECIPIENT_KEY = "recipients";
    private final static String CREATED_AT_KEY = "createdAt";

    public Message() {}

    public Message(String body, User sender, ArrayList<User> recipients) {
        setUser(sender);
        setBody(body);
        addRecipient(recipients);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    private void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }

    private void setUser(User user) {
        put(USER_KEY, user.getParseUser());
    }

    public ParseUser getRecicpent() {
        return getParseUser(RECIPIENT_KEY);
    }

    private void addRecipient(ArrayList<User> users) {
        for (User i : users) {
            i.getParseUser().revert();
            addUnique(RECIPIENT_KEY, i.getParseUser());
        }
    }

    public static class Query extends ParseQuery<Message> {
        public Query() {
            super(Message.class);
        }
        public Query getTop() {
            orderByDescending(CREATED_AT_KEY);
            setLimit(50);
            return this;
        }
        public Query withUser() {
            include(USER_KEY);
            return this;
        }

        public Query recipients(List<User> users) {
            ArrayList<ParseUser> holder = new ArrayList<>();
            for (User i : users) {
                holder.add(i.getParseUser());
            }
            try {
                whereContainsAll(RECIPIENT_KEY, holder);
                return this;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
