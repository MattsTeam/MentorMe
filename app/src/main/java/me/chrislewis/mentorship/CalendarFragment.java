package me.chrislewis.mentorship;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.data.IPopup;
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
import java.util.List;

import me.chrislewis.mentorship.models.CurrentDayDecorator;
import me.chrislewis.mentorship.models.CustomDecoration;
import me.chrislewis.mentorship.models.GoogleDayDecorator;
import me.chrislewis.mentorship.models.ParseEvent;
import me.chrislewis.mentorship.models.Popup;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment implements OnDateSelectedListener, ApiAsyncTask.AsyncResponse {

    SimpleDateFormat todayFormat = new SimpleDateFormat("MMM dd, yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<ParseEvent> days = new ArrayList<>();
    List<com.google.api.services.calendar.model.Event> googleTodayEvents = new ArrayList<>();
    ArrayList<com.google.api.services.calendar.model.Event> googleEvents = new ArrayList<>();
    int orange = getIntFromColor(128, 128, 128);
    int black = getIntFromColor(0,0,0);
    MaterialCalendarView calendarView;
    Calendar calendar;
    TextView todayText;
    ImageButton addEventButton;
    String selectedDate = "";

    Switch permissionToggle;
    com.google.api.services.calendar.Calendar mService;
    GoogleAccountCredential credential;
    HttpTransport transport;
    JsonFactory jsonFactory;
    SharedPreferences settings;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR};

    SharedViewModel model;
    com.framgia.library.calendardayview.CalendarDayView dayView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        permissionToggle = view.findViewById(R.id.syncSwitch);
        dayView = view.findViewById(R.id.dayView);
        dayView.setDecorator(new CustomDecoration(getActivity()));

        if(ParseUser.getCurrentUser().getBoolean("allowSync")) {
            permissionToggle.setOnCheckedChangeListener (null);
            permissionToggle.setChecked(true);
        }
        else {
            permissionToggle.setOnCheckedChangeListener (null);
            permissionToggle.setChecked(false);
        }

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        addEventButton = view.findViewById(R.id.addEventButton);
        calendarView = view.findViewById(R.id.calendarView);
        todayText = view.findViewById(R.id.tvCurrentDay);
        calendar = Calendar.getInstance();
        calendarView.setDateSelected(calendar.getTime(), true);
        selectedDate = dateFormat.format(calendar.getTime());
        todayText.setText(todayFormat.format(calendar.getTime()));
        calendarView.setOnDateChangedListener(this);

        transport = AndroidHttp.newCompatibleTransport();
        jsonFactory = GsonFactory.getDefaultInstance();
        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        refreshEvents();
        if(ParseUser.getCurrentUser().getBoolean("allowSync")) {
            if(model.getCredential() == null) {
                authorize();
                if(isGooglePlayServicesAvailable()) {
                    chooseAccount();
                }
            }
            else {
                refreshResults();
            }
        }

        permissionToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    Log.d("CalendarFragment", "Allowed sync");
                    ParseUser.getCurrentUser().put("allowSync", true);
                    ParseUser.getCurrentUser().saveInBackground();
                    if(model.getCredential() == null) {
                        authorize();
                    }
                    if(isGooglePlayServicesAvailable()) {
                        chooseAccount();
                    }
                }
                else {
                    Log.d("CalendarFragment", "Disallowed sync");
                    ParseUser.getCurrentUser().put("allowSync", false);
                    ParseUser.getCurrentUser().saveInBackground();
                    calendarView.removeDecorators();
                    refreshEvents();
                }
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEventFragment addEventFragment = new AddEventFragment();
                model.setCreateFromCalendar(true);
                Bundle addEventBundle = new Bundle();
                addEventBundle.putString("dateSelected", selectedDate);
                addEventBundle.putBoolean("isSynced", ParseUser.getCurrentUser().getBoolean("allowSync"));
                addEventFragment.setArguments(addEventBundle);
                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, addEventFragment).commit();
                Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                view.startAnimation(fadeOut);
            }
        });
    }

    public void authorize() {
        Log.d("CalendarFragment", "Setting up oAuth for first time syncer");
        credential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
        model.setCredential(credential);
        model.setService(mService);
    }

    public void refreshEvents() {
        ParseQuery<ParseEvent> query = ParseQuery.getQuery(ParseEvent.class);
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        try {
            days.clear();
            List<ParseEvent> events = query.find();
            days.addAll(events);
            calendarView.addDecorators(new CurrentDayDecorator(orange, days));
            //populateDayView(events);
            Log.d("CalendarFragment", "Refreshed from parse");

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

        final List<ParseEvent> todayEvents = new ArrayList<>();
        ParseQuery<ParseEvent> selectedDayQuery = new ParseEvent.Query();
        selectedDayQuery.whereEqualTo("dateString", selectedDate);
        selectedDayQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
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

            //populate dayView
            fragment.show(fragmentManager, "event_dialog_fragment");
        }
        populateDayView(todayEvents);

    }

    public int getHour(String time) {
        String hour = time.substring(0,2);
        if(hour.substring(0,1) == "0") {
            hour = hour.substring(1,2);
            return Integer.valueOf(hour);
        }
        return Integer.valueOf(hour);
    }

    public int getMinute(String time) {
        String minute = time.substring(3,5);
        if(minute.substring(0,1) == "0") {
            minute = minute.substring(1,2);
            return Integer.valueOf(minute);
        }
        return Integer.valueOf(minute);
    }

    public void populateDayView(List<ParseEvent> todayEvents) {
        ArrayList<IEvent> dayViewEvents = new ArrayList<>();
        ArrayList<IPopup> dayViewPopups = new ArrayList<>();
        model.inviteeImages.clear();
        Log.d("CalendarFragment", "Day view objects: " + Integer.toString(todayEvents.size()));
        model.inviteeNum = todayEvents.size() - 1;
        model.resetInviteeIndex();
        for(int i = 0; i < todayEvents.size(); i++) {
            ParseEvent currentEvent = todayEvents.get(i);
            model.addInviteeImg(currentEvent.getInviteeImage());
            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, getHour(currentEvent.getEventTime()));
            timeStart.set(Calendar.MINUTE,getMinute(currentEvent.getEventTime()));
            Calendar timeEnd = (Calendar) Calendar.getInstance();
            timeEnd.set(Calendar.HOUR_OF_DAY, getHour(currentEvent.getEndTime()));
            timeEnd.set(Calendar.MINUTE,getMinute(currentEvent.getEndTime()));
            //String location = "N/A";
            String description = currentEvent.getEventDescription();
            Popup popup = new Popup();
            popup.setStartTime(timeStart);
            popup.setEndTime(timeEnd);
            popup.setTitle(description);
            popup.setDescription(currentEvent.getInviteeString());
            popup.setQuote("");
            dayViewPopups.add(popup);
        }
        dayView.setPopups(dayViewPopups);
    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000;
        Green = (Green << 8) & 0x0000FF00;
        Blue = Blue & 0x000000FF;
        return 0xFF000000 | Red | Green | Blue;
    }

    //Attempts to get data from Google Calendar API
    private void refreshResults() {
        Log.d("CalendarFragment", "Refreshing results");
        if(model.getCredential().getSelectedAccountName() == null) {
            chooseAccount();
        }
        else {
            if (isDeviceOnline()) {
                googleEvents.clear();
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
                model.getCredential().newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
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
                    //credential.setSelectedAccountName(accountName);
                    model.getCredential().setSelectedAccountName(accountName);
                    SharedPreferences settings =
                            getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.commit();
                    refreshResults();
                }
            }
            else {
                ParseUser.getCurrentUser().put("allowSync", false);
                ParseUser.getCurrentUser().saveInBackground();
                permissionToggle.setChecked(false);
                Log.d("CalendarFragment", "Pressed cancel and did not allow syncing.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(List<com.google.api.services.calendar.model.Event> output) {
    }

//    @Override
//    public void passNewEvent(String date, String time, String description, String invitee, String inviteeId) {
//        //Add new event to Google Calendar
//        if(ParseUser.getCurrentUser().getBoolean("allowSync")) {
//            try {
//                new CreateEvent(mService, date, time, description, invitee).execute();
//                refreshResults();
//            }
//            catch (java.text.ParseException e) {
//                e.printStackTrace();
//            }
//        }
        //Add new event to Parse
        /*else {
            Event newEvent = new Event();
            newEvent.setUserIdKey(ParseUser.getCurrentUser().getObjectId());
            try {
                Log.d("CalendarFragment", "date: " + date + " time: " + time);
                Date newDate = newEventFormat.parse(date + " " + time);
                newEvent.setEventDate(newDate);
                newEvent.setEventDescription(description);
                newEvent.setTime(time);
                newEvent.setDateString(date);
                Log.d("CalendarFragment", invitee);
                Log.d("CalendarFragment", inviteeId);
                newEvent.setInviteeString(invitee);
                newEvent.setInviteeIdString(inviteeId);
                newEvent.saveInBackground();

                AlarmBroadcastReceiver.setAlarm(getContext(), date, time);

                Toast toast = Toast.makeText(getActivity(), "Added new event!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
                toast.show();
                refreshEvents();
                Log.d("CalendarFragment", "Saved new event to Parse");
            }
            catch (java.text.ParseException e) {
                Log.d("CalendarFragment", "Failed to save new event to Parse");
                e.printStackTrace();
            }

        }*/
    //}

}
