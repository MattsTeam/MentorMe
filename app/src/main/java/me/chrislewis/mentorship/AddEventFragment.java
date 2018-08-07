package me.chrislewis.mentorship;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.AlarmBroadcastReceiver;
import me.chrislewis.mentorship.models.ParseEvent;
import me.chrislewis.mentorship.models.User;


public class AddEventFragment extends Fragment {

    private ImageView addEventButton;
    private ImageView cancelButton;
    private EditText eventDescription;
    private Boolean isSynced;
    private TextView eventTitle;
    private String todayString;
    private Date date;
    private Spinner findMentors;

    private ImageView startBox;
    private ImageView endBox;
    private TextView startTime;
    private TextView endTime;
    private TextView eventDate;

    private List<User> users = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newEventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SharedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        try {
            if (getArguments() != null) {
                isSynced = getArguments().getBoolean("isSynced");
                todayString = getArguments().getString("dateSelected");
                Log.d("AddEventDialogFragment", "dateSelected: " + todayString);
                date = currFormat.parse(todayString);
                Log.d("AddEventDialogFragment", dateFormat.format(date));
                Log.d("AddEventDialogFragment", "isSynced: " + Boolean.toString(isSynced));
            }

        } catch (ParseException e) {
            Log.d("AddEventDialog", "Failed to parse today string");
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_add_event, parent, false);
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

    private void applyStyLing(TimePickerDialog timePickerDialog){
        Resources system = Resources.getSystem();
        int hourNumberPickerId = system.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = system.getIdentifier("minute", "id", "android");
        int ampmNumberPickerId = system.getIdentifier("amPm", "id", "android");

        NumberPicker hourNumberPicker = (NumberPicker) timePickerDialog.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) timePickerDialog.findViewById(minuteNumberPickerId);
        NumberPicker ampmNumberPicker = (NumberPicker) timePickerDialog.findViewById(ampmNumberPickerId);

        setNumberPickerDividerColour(hourNumberPicker);
        setNumberPickerDividerColour(minuteNumberPicker);
        setNumberPickerDividerColour(ampmNumberPicker);
    }

    private void setNumberPickerDividerColour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();

        for(int i = 0; i < count; i++){

            try{
                Field dividerField = number_picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.orange));
                dividerField.set(number_picker,colorDrawable);

                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTxtClr", e);
            }
        }
    }

    public void getTime(final TextView text, Boolean isStartTime) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(getActivity(), R.style.Theme_TimePicker_Dialog_Alert, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selectedHrStr = Integer.toString(selectedHour);
                if(selectedHrStr.length() == 1) {
                    selectedHrStr = "0" + selectedHrStr;
                }
                String selectedMinStr = Integer.toString(selectedMinute);
                if(selectedMinStr.length() == 1) {
                    selectedMinStr = "0" + selectedMinStr;
                }
                text.setText(selectedHrStr + ":" + selectedMinStr);
            }
        }, hour, minute, true);
        if(isStartTime) {
            mTimePicker.setTitle("Select a start time for your event.");
        }
        else {
            mTimePicker.setTitle("Select an end time for your event.");
        }
        mTimePicker.show();
        applyStyLing(mTimePicker);
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = mTimePicker.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(R.color.orange));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        if(model.getCurrentUser().getFavorites() != null) {
            users.addAll(model.getCurrentUser().getFavorites());
        }
        populateFavoritesInfo(names, ids, users);
        names.add(0, "Select your mentor");
        findMentors = view.findViewById(R.id.findMentor);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, names) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                //Hide instructions on spinner
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    //Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                //parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        findMentors.setAdapter(adapter);

        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);

        eventDate = view.findViewById(R.id.eventDate);
        if(date != null && model.getCreateFromCalendar()) {
            eventDate.setText(dateFormat.format(date));
        }
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newDate = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), R.style.datePicker, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("AddEvent", Integer.toString(year));
                        Log.d("AddEvent", Integer.toString(monthOfYear));
                        Log.d("AddEvent", Integer.toString(dayOfMonth));
                        monthOfYear = monthOfYear + 1;
                        String monthStr = Integer.toString(monthOfYear);
                        if(monthStr.length() == 1) {
                            monthStr = "0" + monthStr;
                        }
                        String dayStr = Integer.toString(dayOfMonth);
                        if(dayStr.length() == 1) {
                            dayStr = "0" + dayStr;
                        }
                        todayString = Integer.toString(year) + "-" + monthStr + "-" + dayStr;
                        try {
                            Date selectedDate = currFormat.parse(todayString);
                            eventDate.setText(dateFormat.format(selectedDate));
                        } catch (ParseException e) {
                            eventDate.setText(todayString);
                            Log.d("AddEventFragment", "Could not format selected day");
                            e.printStackTrace();
                        }
                    }

                }, newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);

        startBox = view.findViewById(R.id.startBox);
        startBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(startTime, true);
            }
        });
        endBox = view.findViewById(R.id.endBox);
        endBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(endTime, false);
            }
        });

        addEventButton = view.findViewById(R.id.doneButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findMentors.getSelectedItem() == null
                        || startTime.getText().toString().equals("--:--")
                        || endTime.getText().toString().equals("--:--")
                        || eventDescription.getText().toString().equals("")
                        || eventTitle.equals("")) {
                    Toast toast = Toast.makeText(getActivity(), "Please fill out all of the required information for the new event.", Toast.LENGTH_LONG);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    v.setGravity(Gravity.CENTER);
                    toast.show();
                }
                else {
                    String title = eventTitle.getText().toString();
                    String description = eventDescription.getText().toString();
                    String invitee = findMentors.getSelectedItem().toString();
                    String inviteeId = ids.get(names.indexOf(invitee) - 1);
                    String startTimeString = startTime.getText().toString();
                    String endTimeString = endTime.getText().toString();
                    model.setNewEventInfo(todayString, startTimeString, endTimeString, description, invitee, inviteeId, title);
                    createParseEvent();
                    if(model.getCreateFromCalendar()) {
                        model.getFragmentManager().findFragmentById(R.id.flContainer);
                        CalendarFragment calendarFragment = (CalendarFragment) model.getFragmentManager().findFragmentByTag("CalendarFragment");
                        calendarFragment.refreshEvents();
                    }
                    else {
                        //TODO switch back to activity details fragment
                    }
                }
            }
        });
    }

    //Create event for on self and invitee's calendar
    public void createParseEvent() {
        ParseEvent newEvent = new ParseEvent();
        ParseEvent otherEvent = new ParseEvent();
        newEvent.setUserIdKey(ParseUser.getCurrentUser().getObjectId());
        otherEvent.setInviteeIdString(model.getCurrentUser().getObjectId());
        otherEvent.setInviteeString(model.getCurrentUser().getName());
        otherEvent.setInviteeImage(model.getCurrentUser().getProfileImage());
        String date = model.getNewEventInfo().get(0);
        String startTime = model.getNewEventInfo().get(1);
        String endTime = model.getNewEventInfo().get(5);
        newEvent.setDateString(date);
        newEvent.setTime(startTime);
        newEvent.setEndTime(endTime);
        otherEvent.setDateString(date);
        otherEvent.setTime(startTime);
        otherEvent.setEndTime(endTime);
        Date newDate = null;
        try {
            newDate = newEventFormat.parse(date + " " + startTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        newEvent.setEventDate(newDate);
        otherEvent.setEventDate(newDate);
        String description = model.getNewEventInfo().get(2);
        newEvent.setEventDescription(description);
        otherEvent.setEventDescription(description);
        String invitee = model.getNewEventInfo().get(3);
        String inviteeId = model.getNewEventInfo().get(4);
        otherEvent.setUserIdKey(inviteeId);
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
        String title = model.getNewEventInfo().get(6);
        newEvent.setEventTitle(title);
        otherEvent.setEventTitle(title);
        newEvent.saveInBackground();
        otherEvent.saveInBackground();

        AlarmBroadcastReceiver.setAlarm(getContext(), date, startTime);

        Toast toast = Toast.makeText(getActivity(), "Added new event!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
        Log.d("AddEventDialogFragment", "Saved new event to Parse");

    }

}
