package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Review")
public class Review extends ParseObject{
    private final static String WRITER_KEY = "writer";
    private final static String WRITER_ID_KEY = "writerId";
    private final static String BODY_KEY = "body";
    private final static String USER_KEY = "user";
    private final static String USER_ID_KEY = "userId";
    private final static String CREATED_AT_KEY = "createdAt";
    private final static String RATING_KEY = "rating";
    private final static String PHOTO_KEY = "reviewPhoto";

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseUser getUser() {
        return getParseUser(USER_ID_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public ParseUser getWriter() {
        return getParseUser(WRITER_KEY);
    }

    public void setWriter(ParseUser writer) {

        put(WRITER_KEY, writer);
    }

    public void setWriterId(String writerId) { put(WRITER_ID_KEY, writerId); }

    public float getRating() {
        return (float) getDouble(RATING_KEY);
    }
    public void setRating(Double rating) { put(RATING_KEY, rating); }

    public ParseFile getReviewPhoto() {return getParseFile(PHOTO_KEY); }
    public void setReviewPhoto(ParseFile file) {put(PHOTO_KEY, file);}

    public static class Query extends ParseQuery<Review> {
        public Query() {
            super(Review.class);
        }
        public Query getTop() {
            orderByDescending(CREATED_AT_KEY);
            setLimit(50);
            return this;
        }
        /*
        public Query withUser() {
            include(USER_KEY);
            return this;
        }
        */
    }
}
