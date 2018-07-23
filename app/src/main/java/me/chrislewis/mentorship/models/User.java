package me.chrislewis.mentorship.models;

import android.text.format.DateUtils;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

public class User{
    private final static String NAME_KEY = "name";
    private final static String USERNAME_KEY = "username";
    private final static String JOB_KEY = "job";
    private final static String PROFILE_IMAGE_KEY = "profileImage";
    private final static String SKILLS_KEY = "skills";
    private final static String SUMMARY_KEY = "summary";
    private final static String EDUCATION_KEY = "education";
    private final static String DESCRIPTION_KEY = "description";
    private final static String CATEGORY_KEY = "category";
    private final static String ORGANIZATION_KEY = "organization";
    private final static String RANK_KEY = "rank";
    private final static String FAVORITE_KEY = "favorites";
    private final static String LOCATION_KEY = "location";
    private final static String DISTANCE_KEY = "distance";
    private final static String REL_DISTANCE_KEY = "relativeDistance";
    private final static String CATEGORIES_KEY = "categories";
    private final static String PASSWORD_KEY = "password";
    private final static String EMAIL_KEY = "email";

    public String name;
    public String username;
    public String job;
    public String profileImage;
    public String skills;
    public String summary;
    public String education;
    public String description;
    public String location;
    public String distance;
    public String relativeDistance;

    private ParseUser user;

    public User(ParseUser user){
        this.user = user;
    }

    public ParseUser getParseUser() {
        return user;
    }

    public String getObjectId() {
        return user.getObjectId();
    }

    public String getUsername() {
        return user.getString(USERNAME_KEY);
    }

    public void setUsername(String username) {
        user.put(USERNAME_KEY, username);
    }

    public String getName() {
        return user.getString(NAME_KEY);
    }

    public void setName(String name) {
        user.put(NAME_KEY, name);
    }

    public String getPassword() { return user.getString(PASSWORD_KEY); }

    public void setPassword(String password) {
        user.put(PASSWORD_KEY, password);
    }

    public String getEmail() { return user.getString(EMAIL_KEY); }

    public void setEmail(String email) {
        user.put(EMAIL_KEY, email);
    }



    public String getJob() {
        return user.getString(JOB_KEY);
    }

    public void setJob(String job) {
        user.put(JOB_KEY, job);
    }

    public ParseFile getProfileImage() {
        return user.getParseFile(PROFILE_IMAGE_KEY);
    }

    public void setProfileImage(ParseFile file) {
        user.put(PROFILE_IMAGE_KEY, file);
    }

    public String getSkills() {
        return user.getString(SKILLS_KEY);
    }

    public void setSkills(String skills) {
        user.put(SKILLS_KEY, skills);
    }

    public String getSummary() {
        return user.getString(SUMMARY_KEY);
    }

    public void setSummary(String summary) {
        user.put(SUMMARY_KEY, summary);
    }

    public String getEducation() {
        return user.getString(EDUCATION_KEY);
    }

    public void setEducation(String education) {
        user.put(EDUCATION_KEY, education);
    }

    public String getDescription() {
        return user.getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description) {
        user.put(DESCRIPTION_KEY, description);
    }

    public String getOrganization() {
        return user.getString(ORGANIZATION_KEY);
    }

    public void setOrganization(String organization) {
        user.put(ORGANIZATION_KEY, organization);
    }

    public double getRank() {
        return user.getDouble(RANK_KEY);
    }

    public void setRank(double rank) {
        user.put(RANK_KEY, rank);
    }

    public List<ParseUser> getFavorites() {
        return (List<ParseUser>) user.get(FAVORITE_KEY);
    }

    public void addFavorite(ParseUser user) {
        this.user.addUnique(FAVORITE_KEY, user);
    }

    public void removeFavorite(ParseUser user) {
        this.user.removeAll(FAVORITE_KEY, Collections.singleton(user));
    }


    public List<String> getCategories() {
        return (List<String>) user.get(CATEGORIES_KEY);
    }
    public void addCategory(String category) {this.user.addUnique(CATEGORIES_KEY, category);}

    public ParseGeoPoint getCurrentLocation() {
        return user.getParseGeoPoint(LOCATION_KEY);
    }


    public void setLocation(ParseGeoPoint location) {
        user.put(LOCATION_KEY, location);
    }

    public String getDistance() {
        return user.getString(DISTANCE_KEY);
    }

    public void setDistance(String distance) {
        user.put(DISTANCE_KEY, distance);
    }

    public Double getRelDistance() {
        return user.getDouble(REL_DISTANCE_KEY);
    }

    public void setRelDistance(Double distance) {
        user.put(REL_DISTANCE_KEY, distance);
    }

    public void saveInBackground() {
        user.saveInBackground();
    }

    public void fetchInBackground(GetCallback<ParseObject> callback) throws ParseException { user.fetchInBackground(callback); }

    public String getRelativeTimeAgo() {
        String relativeDate;
        long dateMillis = user.getCreatedAt().getTime();
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