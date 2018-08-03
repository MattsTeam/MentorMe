package me.chrislewis.mentorship;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.AlarmBroadcastReceiver;
import me.chrislewis.mentorship.models.ParseEvent;
import me.chrislewis.mentorship.models.User;


public class AddEventDialogFragment extends DialogFragment {

    private Button addEventButton;
    private TimePicker timePicker;
    private EditText eventDescription;
    private Boolean isSynced;
    private TextView selectTime;
    private String todayString;
    private Date date;
    private Spinner findMentors;
    private TextView startTime;
    private TextView endTime;
    private List<User> users = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newEventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SharedViewModel model;

    public AddEventDialogFragment() { }

    public static AddEventDialogFragment newInstance() {
        AddEventDialogFragment frag = new AddEventDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isSynced = getArguments().getBoolean("isSynced");
        todayString = getArguments().getString("dateSelected");
        Log.d("AddEventDialogFragment", "dateSelected: " + todayString);
        try {
            date = currFormat.parse(todayString);
            Log.d("AddEventDialogFragment", dateFormat.format(date));
        } catch (ParseException e) {
            Log.d("AddEventDialog", "Failed to parse today string");
            e.printStackTrace();
        }
        Log.d("AddEventDialogFragment", "isSynced: " + Boolean.toString(isSynced));
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_event_dialog);
        return inflater.inflate(R.layout.fragment_add_event, container);
    }

    public void populateFavoritesInfo(List<String> names, List<String> ids, List<User> users) {
        names.clear();
        ids.clear();
        for(int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            ParseUser parseUser = null;
            try {
                parseUser = user.getParseUser().fetchIfNeeded();
                User mUser = new User(parseUser);
                names.add(mUser.getName());
                ids.add(mUser.getObjectId());
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTime() {
        String hour = Integer.toString(timePicker.getHour());
        if(hour.length() == 1) {
            hour = "0" + hour;
        }
        String minute = Integer.toString(timePicker.getMinute());
        if(minute.length() == 1) {
            minute = "0" + minute;
        }
       return hour + ":" + minute;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        users.addAll(model.getCurrentUser().getFavorites());
        populateFavoritesInfo(names, ids, users);
        findMentors = view.findViewById(R.id.findMentor);
        findMentors.setPrompt("Select your mentor.");
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, names);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        findMentors.setAdapter(adapter);
        selectTime = view.findViewById(R.id.tvSelectTime);
        selectTime.setText(dateFormat.format(date));
        timePicker = view.findViewById(R.id.simpleTimePicker);
        eventDescription = view.findViewById(R.id.editDetails);
        startTime = view.findViewById(R.id.tvStartTime);
        endTime = view.findViewById(R.id.tvEndTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddEventFragment", "Clicked start time");
                startTime.setText(getTime());
            }
        });
        endTime = view.findViewById(R.id.tvEndTime);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddEventFragment", "Clicked end time");
                endTime.setText(getTime());
            }
        });
        addEventButton = view.findViewById(R.id.doneButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findMentors.getSelectedItem() == null
                        || startTime.getText().toString().equals("Start Time")
                        || endTime.getText().toString().equals("End Time")
                        || eventDescription.equals("")) {
                    Toast toast = Toast.makeText(getActivity(), "Please fill out all of the required information for the new event.", Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setGravity(Gravity.CENTER);
                    toast.show();
                }
                else {
                    String description = eventDescription.getText().toString();
                    String invitee = findMentors.getSelectedItem().toString();
                    String inviteeId = ids.get(names.indexOf(invitee));
                    String startTimeString = startTime.getText().toString();
                    String endTimeString = endTime.getText().toString();
                    model.setNewEventInfo(todayString, startTimeString, endTimeString, description, invitee, inviteeId);
                    createParseEvent();
                    if(model.getCreateFromCalendar()) {
                        model.getFragmentManager().findFragmentById(R.id.flContainer);
                        CalendarFragment calendarFragment = (CalendarFragment) model.getFragmentManager().findFragmentByTag("CalendarFragment");
                        calendarFragment.refreshEvents();
                    }
                    dismiss();
                }
            }
        });
    }

    public void createParseEvent() {
        ParseEvent newEvent = new ParseEvent();
        newEvent.setUserIdKey(ParseUser.getCurrentUser().getObjectId());
        String date = model.getNewEventInfo().get(0);
        String startTime = model.getNewEventInfo().get(1);
        String endTime = model.getNewEventInfo().get(5);
        newEvent.setDateString(date);
        newEvent.setTime(startTime);
        newEvent.setEndTime(endTime);
        Date newDate = null;
        try {
            newDate = newEventFormat.parse(date + " " + startTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        newEvent.setEventDate(newDate);
        String description = model.getNewEventInfo().get(2);
        newEvent.setEventDescription(description);
        String invitee = model.getNewEventInfo().get(3);
        String inviteeId = model.getNewEventInfo().get(4);
        Log.d("AddEventDialog", "date: " + date);
        Log.d("AddEventDialog", "time: " + startTime);
        Log.d("AddEventDialog", "endTime: " + endTime);
        Log.d("AddEventDialog", "invitee: " + invitee);
        Log.d("AddEventDialog", "inviteeId: " + inviteeId);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", inviteeId);
        try {
            List<ParseUser> inviteeUsers = query.find();
            Log.d("AddEventDialog", "invitees size: " + Integer.toString(inviteeUsers.size()));
            ParseUser inviteeParseUser = inviteeUsers.get(0);
            User inviteeUser = new User(inviteeParseUser);
            ParseFile profileImage = inviteeUser.getProfileImage();
            newEvent.setInviteeImage(profileImage);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        newEvent.setInviteeString(invitee);
        newEvent.setInviteeIdString(inviteeId);
        newEvent.saveInBackground();

        AlarmBroadcastReceiver.setAlarm(getContext(), date, startTime);

        Toast toast = Toast.makeText(getActivity(), "Added new event!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
        Log.d("AddEventDialogFragment", "Saved new event to Parse");

    }

}
