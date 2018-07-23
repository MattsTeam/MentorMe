package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModel;

import me.chrislewis.mentorship.models.User;

public class SharedViewModel extends ViewModel{
    User user;
    User currentUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
