package me.chrislewis.mentorship;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;

import java.util.LinkedList;

import me.chrislewis.mentorship.models.Event;
import me.chrislewis.mentorship.models.Message;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application{

    public static final String MY_APP_ID = "TeamMatt";
    public static String SERVER = "http://teammatt-fbu-mentorship.herokuapp.com/parse";
    public static String CLIENT_KEY = "TeamMatt";
    public static final String CHANNEL_NAME = "hi";


    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Event.class);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId(MY_APP_ID)
                .clientKey(CLIENT_KEY)
                .server(SERVER)
                .build();
        Parse.initialize(configuration);


        LinkedList<String> channels = new LinkedList<String>();
        channels.push("channelName");

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "905006370376");
        //TODO how to get device token for mobile, why not installation.getDeviceToken();
        installation.put("channels", channels);
        installation.saveInBackground();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);



        ParseObject testObject = new ParseObject("Test Object");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        ParsePush.subscribeInBackground(CHANNEL_NAME);
    }
}
