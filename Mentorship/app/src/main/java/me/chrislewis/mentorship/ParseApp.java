package me.chrislewis.mentorship;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.chrislewis.mentorship.models.Message;
import me.chrislewis.mentorship.models.User;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("TeamMatt")
                .clientKey("TeamMatt")
                .server("http://teammatt-fbu-mentorship.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
