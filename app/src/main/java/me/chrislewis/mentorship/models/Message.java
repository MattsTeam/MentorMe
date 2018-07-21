package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Message")
public class Message extends ParseObject{
    private final static String USER_KEY = "user";
    private final static String USER_ID_KEY = "userId";
    private final static String BODY_KEY = "body";
    private final static String CREATED_AT_KEY = "createdAt";


    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public void setUserIdKey(String userId) {
        put(USER_ID_KEY, userId);
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
    }
}
