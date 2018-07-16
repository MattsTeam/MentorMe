package me.chrislewis.mentorship;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("TeamMatt")
                .clientKey("TeamMatt")
                .server("http://teammatt-fbu-mentorship.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
