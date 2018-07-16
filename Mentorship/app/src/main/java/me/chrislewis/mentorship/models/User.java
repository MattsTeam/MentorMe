package me.chrislewis.mentorship.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject{
    public String getUsername() {return getString("username"); }
    public void setUsername(String username) {put("username", username); }

    public ParseFile getProfileImage() {
        return getParseFile("profileImage");
    }

    public void setProfileImage(ParseFile image) {
        put("profileImage", image);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }
}
