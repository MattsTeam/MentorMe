package me.chrislewis.mentorship.models;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {
    final static String NAME_KEY = "name";
    final static String JOB_KEY = "job";
    final static String PROFILE_IMAGE_KEY = "profileImage";
    final static String SKILLS_KEY = "skills";
    final static String SUMMARY_KEY = "summary";
    final static String EDUCATION_KEY = "education";
    final static String FAVORITE_KEY = "favorites";

    boolean liked;

    public void Post() {
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getJob() {
        return getString(JOB_KEY);
    }

    public void setJob(String job) {
        put(JOB_KEY, job);
    }

    public ParseFile getProfileImage() {
        return getParseFile(PROFILE_IMAGE_KEY);
    }

    public void setProfileImage(ParseFile file) {
        put(PROFILE_IMAGE_KEY, file);
    }

    public String getSkills() {
        return getString(SKILLS_KEY);
    }

    public void setSkills(String skills) {
        put(SKILLS_KEY, skills);
    }

    public String getSummary() {
        return getString(SUMMARY_KEY);
    }

    public void setSummary(String summary) {
        put(SUMMARY_KEY, summary);
    }

    public String getEducation() {
        return getString(EDUCATION_KEY);
    }

    public void setEducation(String education) {
        put(EDUCATION_KEY, education);
    }

    public List<ParseUser> getFavorites() {
        return (List<ParseUser>) get(FAVORITE_KEY);
    }

    public void addFavorite(ParseUser user) {
        addUnique(FAVORITE_KEY, user);
    }

    public void removeFavorite(ParseUser user) {
        removeAll(FAVORITE_KEY, Collections.singleton(user));
    }

    public String getRelativeTimeAgo() {
        String relativeDate;
        long dateMillis = getCreatedAt().getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        return relativeDate;
    }

    public static class Query extends ParseQuery<ParseUser> {
        public Query() {
            super(ParseUser.class);
        }

    }

}