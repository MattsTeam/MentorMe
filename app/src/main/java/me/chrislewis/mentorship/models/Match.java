package me.chrislewis.mentorship.models;

import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Match")
public class Match extends ParseObject{

    private final static String MENTEE_KEY = "mentee";
    private final static String MENTOR_KEY = "mentor";

    private final static String DECLINED_KEY = "declined";
    private final static String ACCEPTED_KEY = "accepted";

    private User mentee;
    private User mentor;
    private boolean isAccepted;
    private boolean isDeclined;

    public Match() {}

    public Match(User mentee, User mentor) {
        this.mentee = mentee;
        this.mentor = mentor;
        put(MENTEE_KEY, mentee.getParseUser());
        put(MENTOR_KEY, mentor.getParseUser());
        ParseACL parseACL = new ParseACL();
        parseACL.setWriteAccess(mentee.getParseUser(), true);
        parseACL.setWriteAccess(mentor.getParseUser(), true);
        parseACL.setReadAccess(mentee.getParseUser(), true);
        parseACL.setReadAccess(mentor.getParseUser(), true);
        setACL(parseACL);
    }

    public boolean isAccepted() {
        return getBoolean(ACCEPTED_KEY);
    }

    public void setAccepted(boolean accepted) {
        put(ACCEPTED_KEY, accepted);
    }

    public boolean isDeclined() {
        return getBoolean(DECLINED_KEY);
    }

    public void setDeclined(boolean declined) {
        put(DECLINED_KEY, declined);
    }

    public User getMentee() {
        if (mentee == null) {
            mentee = new User(getParseUser(MENTEE_KEY));
            return mentee;
        } else {
            return mentee;
        }
    }

    public User getMentor() {
        if (mentor == null) {
            mentor = new User(getParseUser(MENTOR_KEY));
            return mentor;
        } else {
            return mentor;
        }
    }

    public static class Query extends ParseQuery<Match> {
        public Query() {
            super(Match.class);
        }

        public Match.Query findMatches(User user){
            if (user.getIsMentor()) {
                whereEqualTo(MENTOR_KEY, user.getParseUser());
                include(MENTEE_KEY);
                return this;
            } else {
                whereEqualTo(MENTEE_KEY, user.getParseUser());
                include(MENTOR_KEY);
                return this;
            }
        }
    }
}
