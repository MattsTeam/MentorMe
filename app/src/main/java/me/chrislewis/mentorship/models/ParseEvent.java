package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.Date;

@ParseClassName("ParseEvent")
public class ParseEvent extends ParseObject implements Serializable {

    private final static String USER_ID_KEY = "userId";
    private final static String DATE_KEY = "eventDate";
    private final static String DESCRIPTION_KEY = "description";
    private final static String DATE_STRING_KEY = "dateString";
    private final static String TIME_KEY = "timeString";
    private final static String DATE_STRING = "dateString";
    private final static String INVITEE_STRING = "invitee";
    private final static String INVITEE_ID_STRING = "inviteeId";
    private final static String END_TIME_KEY = "endTimeString";
    private final static String PROFILE_IMAGE_KEY = "inviteeImage";
    private final static String EVENT_TITLE = "title";


    public ParseEvent() { }

    public void setEventTitle(String title) { put(EVENT_TITLE, title); }

    public ParseFile getInviteeImage() { return getParseFile(PROFILE_IMAGE_KEY); }

    public void setInviteeImage(ParseFile img) { put(PROFILE_IMAGE_KEY, img); }

    public void setEndTime(String time) { put(END_TIME_KEY, time); }

    public String getEndTime() { return getString(END_TIME_KEY); }

    public String getDateString() { return getString(DATE_STRING_KEY); }

    public void setDateString(String eventDate) { put(DATE_STRING_KEY, eventDate);}

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

    public String getInviteeString() { return getString(INVITEE_STRING); }

    public void setInviteeString(String invitee) { put(INVITEE_STRING, invitee); }

    public void setInviteeIdString(String inviteeId) { put(INVITEE_ID_STRING, inviteeId); }

    public static class Query extends ParseQuery<ParseEvent> {
        public Query() {
            super(ParseEvent.class);
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
