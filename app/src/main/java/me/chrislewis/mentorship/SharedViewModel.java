package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.Calendar;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

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
    List<String> newEventInfo = new ArrayList<>();
    List<ParseFile> inviteeImages = new ArrayList<>();
    int inviteeIndex = 0;
    int inviteeNum = 0;
    Boolean createFromCalendar = false;
    String fragmentIdentifier;
    ParseGeoPoint parseLocation;

    public void setCreateFromCalendar(Boolean b) { createFromCalendar = b; }

    public Boolean getCreateFromCalendar() { return createFromCalendar; }

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

    public void setNewEventInfo(String date, String startTime, String endTime, String description, String invitee, String inviteeId, String title) {
        newEventInfo.clear();
        newEventInfo.add(date);
        newEventInfo.add(startTime);
        newEventInfo.add(description);
        newEventInfo.add(invitee);
        newEventInfo.add(inviteeId);
        newEventInfo.add(endTime);
        newEventInfo.add(title);
    }

    public void addInviteeImg(ParseFile img) {
        inviteeImages.add(img);
    }

    public List<ParseFile> getInviteeImages() {
        return inviteeImages;
    }

    public int getInviteeNum() {
        return inviteeNum;
    }

    public int getInviteeIndex() {
        return inviteeIndex;
    }

    public void nextInviteeIndex() {
        inviteeIndex++;
    }

    public void resetInviteeIndex() {
        inviteeIndex = 0;
    }

    public List<String> getNewEventInfo() { return newEventInfo; }

    public ParseGeoPoint getLocation() { return parseLocation; }
    public void setLocation (ParseGeoPoint location) {this.parseLocation = location; }
}
