package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.Calendar;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;


public class SharedViewModel extends ViewModel{

    ArrayList<User> recipients;
    ArrayList<User> reviewers;
    User otherUser;
    User currentUser;
    User reviewedUser;
    Chat currentChat;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    GoogleAccountCredential credential;
    com.google.api.services.calendar.Calendar mService;

    String fragmentIdentifier;
    ParseGeoPoint parseLocation;

    public Calendar getService() {
        return mService;
    }

    public void setService(Calendar mService) {
        this.mService = mService;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public ArrayList<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<User> recipients) {
        this.recipients = recipients;
    }

    public ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
        for(User user : recipients) {
            recipientIds.add(user.getObjectId());
        }
        return recipientIds;
    }

    public void setReviewers(ArrayList<User> reviewers) {
        this.reviewers = reviewers;
    }

    public ArrayList<String> getReviewerIds() {
        ArrayList<String> reviewerIds = new ArrayList<>();
        for(User user : reviewers) {
            reviewerIds.add(user.getObjectId());
        }
        return reviewerIds;
    }

    public String getFragmentIdentifier() {return fragmentIdentifier; }
    public void setFragmentIdentifier(String fragmentIdentifier) {this.fragmentIdentifier = fragmentIdentifier; }

    public User getUser() {
        return otherUser;
    }

    public void setUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getReviewedUser() { return reviewedUser; }
    public void setReviewedUser(User reviewedUser) {this.reviewedUser = reviewedUser; }


    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public FragmentTransaction getFragmentTransaction() {
        return fragmentTransaction;
    }

    public void setFragmentTransaction(FragmentTransaction fragmentTransaction) {
        this.fragmentTransaction = fragmentTransaction;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ParseGeoPoint getLocation() { return parseLocation; }
    public void setLocation (ParseGeoPoint location) {this.parseLocation = location; }
}
