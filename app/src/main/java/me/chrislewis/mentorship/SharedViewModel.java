package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import me.chrislewis.mentorship.models.User;

public class SharedViewModel extends ViewModel{
    User user;
    User currentUser;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return user.getObjectId();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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
