package me.chrislewis.mentorship;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.chrislewis.mentorship.models.Event;
import me.chrislewis.mentorship.models.Message;

public class ParseApp extends Application{

    public static final String MY_APP_ID = "TeamMatt";
    public static String SERVER = "http://teammatt-fbu-mentorship.herokuapp.com/parse";
    public static String CLIENT_KEY = "TeamMatt";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Event.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId(MY_APP_ID)
                .clientKey(CLIENT_KEY)
                .server(SERVER)
                .build();
        Parse.initialize(configuration);

    }
}
