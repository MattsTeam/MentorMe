package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject{
    private final static String USER_ID_KEY = "userId";
    private final static String BODY_KEY = "body";


    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setUserIdKey(String userId) {
        put(USER_ID_KEY, userId);
    }
}
