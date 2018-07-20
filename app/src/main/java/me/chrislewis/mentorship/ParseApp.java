package me.chrislewis.mentorship;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
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

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "976064185196");
        installation.saveInBackground();
        /*
        FileInputStream serviceAccount =
                new FileInputStream("firebase-adminsdk-dpita@mentorship-ee041.iam.gserviceaccount.com.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://mentorship-ee041.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        */
    }
}
