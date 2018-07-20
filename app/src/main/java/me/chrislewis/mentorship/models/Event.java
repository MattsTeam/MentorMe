package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject implements Serializable {

    private final static String USER_ID_KEY = "userId";
    private final static String DATE_KEY = "eventDate";
    private final static String DESCRIPTION_KEY = "description";
    private final static String DATE_STRING_KEY = "dateString";
    private final static String TIME_KEY = "timeString";

    public Event() { }

    public String getDateString() { return getString(DATE_STRING_KEY); }

    public Date getEventDate() { return (Date) get(DATE_KEY); }

    public void setEventDate(Date date) {
        put(DATE_KEY, date);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public void setUserIdKey(String userId) {
        put(USER_ID_KEY, userId);
    }

    public String getEventDescription() { return getString(DESCRIPTION_KEY); }

    public void setEventDescription(String description) { put(DESCRIPTION_KEY, description); }

    public String getEventTime() { return getString(TIME_KEY); }

    public void setTime(String time) { put(TIME_KEY, time); }

    public static class Query extends ParseQuery<Event> {
        public Query() {
            super(Event.class);
        }
        public Query getTop() {
            setLimit(20);
            return this;
        }
        public Query withUser() {
            include("user");
            include("comments");
            return this;
        }
    }

}
