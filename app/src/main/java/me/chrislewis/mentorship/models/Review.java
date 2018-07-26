package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Review")
public class Review extends ParseObject{
    private final static String WRITER_KEY = "writer";
    private final static String WRITER_ID_KEY = "userId";
    private final static String BODY_KEY = "body";
    private final static String USER_KEY = "recipients";
    private final static String CREATED_AT_KEY = "createdAt";
    private final static String RATING_KEY = "rating";

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }

    public void setUser(ParseUser user) {
        put(USER_KEY, user);
    }

    public ParseUser getWriter() {
        return getParseUser(WRITER_KEY);
    }
    public Double getRating() {
        return getDouble(RATING_KEY);
    }
    public void setRating(Double rating) { put(RATING_KEY, rating); }



    public static class Query extends ParseQuery<Review> {
        public Query() {
            super(Review.class);
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
