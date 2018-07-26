package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class SharedViewModel extends ViewModel{

    ArrayList<User> recipients;
    User otherUser;
    User currentUser;
    Chat currentChat;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

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
}
