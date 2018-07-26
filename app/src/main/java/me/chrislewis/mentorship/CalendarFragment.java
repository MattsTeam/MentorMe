package me.chrislewis.mentorship;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.CurrentDayDecorator;
import me.chrislewis.mentorship.models.Event;
import me.chrislewis.mentorship.models.GoogleDayDecorator;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment implements OnDateSelectedListener, ApiAsyncTask.AsyncResponse, AddEventDialogFragment.OnReceivedData{

    SimpleDateFormat todayFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat newEventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<Event> days = new ArrayList<>();
    List<com.google.api.services.calendar.model.Event> googleTodayEvents = new ArrayList<>();
    ArrayList<com.google.api.services.calendar.model.Event> googleEvents = new ArrayList<>();
    com.google.api.services.calendar.model.Event googleToday;
    int orange = getIntFromColor(255, 128, 0);
    int black = getIntFromColor(0,0,0);
    MaterialCalendarView calendarView;
    Calendar calendar;
    TextView todayText;
    ImageButton addEventButton;
    String selectedDate = "";

    ImageButton permissionButton;
    com.google.api.services.calendar.Calendar mService;
    GoogleAccountCredential credential;
    HttpTransport transport;
    JsonFactory jsonFactory;
    SharedPreferences settings;
    Boolean allowSync;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR};

    String newEventDate;
    String newEventTime;
    String newEventDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //get stuff from bundle in addeventdialogfragment
        permissionButton = view.findViewById(R.id.permissionButton);
        addEventButton = view.findViewById(R.id.addEventButton);
        calendarView = view.findViewById(R.id.calendarView);
        todayText = view.findViewById(R.id.tvCurrentDay);
        calendar = Calendar.getInstance();
        calendarView.setDateSelected(calendar.getTime(), true);
        todayText.setText(todayFormat.format(calendar.getTime()));
        calendarView.setOnDateChangedListener(this);

        transport = AndroidHttp.newCompatibleTransport();
        jsonFactory = GsonFactory.getDefaultInstance();
        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        allowSync = ParseUser.getCurrentUser().getBoolean("allowSync");
        refreshEvents();
        authorize();

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().put("allowSync", true);
                ParseUser.getCurrentUser().saveInBackground();
                authorize();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEventDialogFragment addEventFragment = AddEventDialogFragment.newInstance();
                addEventFragment.setUp(CalendarFragment.this);
                Bundle addEventBundle = new Bundle();
                addEventBundle.putString("dateSelected", selectedDate);
                addEventBundle.putBoolean("isSynced", ParseUser.getCurrentUser().getBoolean("allowSync"));
                addEventFragment.setArguments(addEventBundle);
                addEventFragment.show(getFragmentManager(), null);
            }
        });
    }


    public void authorize() {
        if(ParseUser.getCurrentUser().getBoolean("allowSync")) {
            Log.d("CalendarFragment", "Setting up oAuth");
            credential = GoogleAccountCredential.usingOAuth2(
                    getActivity(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff())
                    .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
            if(isGooglePlayServicesAvailable()) {
                refreshResults();
            }
        }
    }

    public void refreshEvents() {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        query.setLimit(15);
        try {
            Log.d("CalendarFragment", "Refreshed from parse");
            days.clear();
            List<Event> events = query.find();
            days.addAll(events);
            calendarView.addDecorators(new CurrentDayDecorator(orange, days));

        } catch (com.parse.ParseException e) {
            Log.d("CalendarFragment", "Failed to refresh from parse");
            e.printStackTrace();
        }
    }

    public String dateTimeDate(String dateTime) {
        return dateTime.substring(0,10);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay calendarDay, boolean b) {
        selectedDate = dateFormat.format(calendarDay.getDate());
        Log.d("CalendarFragment", selectedDate);
        googleTodayEvents.clear();
        for(int i = 0; i < googleEvents.size(); i++) {
            String googleDateString = googleEvents.get(i).getStart().getDateTime().toString();
            if(selectedDate.equals(dateTimeDate(googleDateString))) {
                googleTodayEvents.add(googleEvents.get(i));
            }
        }
        Log.d("CalendarFragment", "googleObjects: " + Integer.toString(googleTodayEvents.size()));
        Bundle googleBundle = new Bundle();
        googleBundle.putSerializable("todayGoogleEvents", (Serializable) googleTodayEvents);
        final FragmentManager fragmentManager = getFragmentManager();
        final EventDialogFragment fragment = EventDialogFragment.newInstance();

        final List<Event> todayEvents = new ArrayList<>();
        ParseQuery<Event> selectedDayQuery = new Event.Query();
        selectedDayQuery.whereEqualTo("dateString", selectedDate);
        todayEvents.clear();
        try {
            todayEvents.addAll(selectedDayQuery.find());
            Log.d("CalendarFragment", "objects: " + Integer.toString(todayEvents.size()));
        }
        catch (ParseException e) {
            Log.d("CalendarFragment", "Selected day does not have events on Parse");
            e.printStackTrace();
        }
        if(todayEvents.size() > 0 || googleTodayEvents.size() > 0) {
            Bundle mainBundle = new Bundle();
            Bundle parseBundle = new Bundle();
            parseBundle.putSerializable("todayEvents", (Serializable) todayEvents);
            mainBundle.putBundle("googleBundle", googleBundle);
            mainBundle.putBundle("parseBundle", parseBundle);
            fragment.setArguments(mainBundle);
            fragment.show(fragmentManager, "event_dialog_fragment");
        }

    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000;
        Green = (Green << 8) & 0x0000FF00;
        Blue = Blue & 0x000000FF;
        return 0xFF000000 | Red | Green | Blue;
    }

    public void addCalendarEvent() {
        //new CreateEvent(mService).execute();
    }

    /*@Override
    public void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable() && allowSync) {
            Log.d("CalendarFragment", "Google play services is available");
            refreshResults();
        }
        else {
            Log.d("CalendarFragment", "Google play services is not available");
        }
    }*/

    //Attempts to get data from Google Calendar API
    private void refreshResults() {
        Log.d("CalendarFragment", "Refreshing results");
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        }
        else {
            if (isDeviceOnline()) {
                Log.d("CalendarFragment", "Device is online.");
                new ApiAsyncTask(getActivity(), new ApiAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(List<com.google.api.services.calendar.model.Event> output) {
                        for(int i = 0; i < output.size(); i++) {
                            googleEvents.add(output.get(i));
                        }
                        calendarView.addDecorators(new GoogleDayDecorator(black, googleEvents));
                    }
                }).execute();
            }
            else {
                Log.d("CalendarFragment", "No network connection.");
            }
        }
    }

    private void chooseAccount() {
        Log.d("CalendarFragment", "Choosing account.");
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        Log.d("CalendarFragment", "Is device online");
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            Log.d("CalendarFragment", "No Google play services.");
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            Log.d("CalendarFragment", "No Google play services 2.");
            return false;
        }
        Log.d("CalendarFragment", "Google play services is available.");
        return true;
    }

    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Log.d("CalendarFragment", "Showing Google play services error");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        getActivity(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GOOGLE_PLAY_SERVICES) {
            Log.d("CalendarFragment", "Request Google play services");
            if (resultCode != RESULT_OK) {
                isGooglePlayServicesAvailable();
            }
        }

        else if(requestCode == REQUEST_ACCOUNT_PICKER) {
            Log.d("CalendarFragment", "Request account picker");
            if (resultCode == RESULT_OK && data != null &&
                    data.getExtras() != null) {
                Log.d("CalendarFragment", "Got account name");
                String accountName =
                        data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                refreshResults();
                if (accountName != null) {
                    Log.d("CalendarFragment", "Selected account name");
                    credential.setSelectedAccountName(accountName);
                    SharedPreferences settings =
                            getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.commit();
                    refreshResults();
                }
            }
        }

        /*else if(requestCode == REQUEST_AUTHORIZATION) {
            Log.d("CalendarFragment", "Request authorization");
            if (resultCode != RESULT_OK) {
                chooseAccount();
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(List<com.google.api.services.calendar.model.Event> output) {
    }

    @Override
    public void passNewEvent(String date, String time, String description) {
        //Add new event to Google Calendar
        if(ParseUser.getCurrentUser().getBoolean("allowSync")) {
            try {
                new CreateEvent(mService, date, time, description).execute();
                refreshResults();
            }
            catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        //Add new event to Parse
        else {
            Event newEvent = new Event();
            newEvent.setUserIdKey(ParseUser.getCurrentUser().getObjectId());
            try {
                Date newDate = newEventFormat.parse(date + " " + time);
                newEvent.setEventDate(newDate);
                newEvent.setEventDescription(description);
                newEvent.setTime(time);
                newEvent.setDateString(date);
                newEvent.saveInBackground();
                refreshEvents();
                Log.d("CalendarFragment", "Saved new event to Parse");
            }
            catch (java.text.ParseException e) {
                Log.d("CalendarFragment", "Failed to save new event to Parse");
                e.printStackTrace();
            }

        }
    }
}
