package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Event")
public class Event extends ParseObject implements Serializable {

    private final static String USER_ID_KEY = "userId";
    private final static String DATE_KEY = "eventDate";
    private final static String DESCRIPTION_KEY = "description";
    private final static String DATE_STRING_KEY = "dateString";
    private final static String TIME_KEY = "timeString";
    //private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public Event event;
    public String googleDescription;
    public String googleDateString;
    public Date googleDate;

    public Event() { }

    public Event(com.google.api.services.calendar.model.Event googleEvent) throws ParseException {
        this.googleDescription = googleEvent.getSummary();
        this.googleDateString = googleEvent.getStart().getDateTime().toString();
        this.googleDate = format.parse(googleDateString);
    }

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
