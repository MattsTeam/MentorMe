package me.chrislewis.mentorship;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.User;


public class AddEventDialogFragment extends DialogFragment {

    public interface OnReceivedData {
        public void passNewEvent(String date, String time, String description, String invitee, String inviteeId);
    }

    private Button addEventButton;
    private TimePicker timePicker;
    private EditText eventDescription;
    private Boolean isSynced;
    private TextView selectTime;
    private String todayString;
    private OnReceivedData mData;
    private Date date;
    private Spinner findMentors;
    private List<User> users = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SharedViewModel model;

    public AddEventDialogFragment() { }

    public void setUp(OnReceivedData data) {
        mData = data;
    }

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
            Log.d("AddEventDialogFragment", "Name: " + users.get(i).getName());
            Log.d("AddEventDialogFragment", "Id: " + users.get(i).getObjectId());
        }
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
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_suggestion, names);
        adapter.setDropDownViewResource(R.layout.search_suggestion);
        findMentors.setAdapter(adapter);
        selectTime = view.findViewById(R.id.tvSelectTime);
        selectTime.setText(dateFormat.format(date));
        timePicker = view.findViewById(R.id.simpleTimePicker);
        eventDescription = view.findViewById(R.id.editDetails);
        addEventButton = view.findViewById(R.id.doneButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hour = Integer.toString(timePicker.getHour());
                if(hour.length() == 1) {
                    hour = "0" + hour;
                }
                String minute = Integer.toString(timePicker.getMinute());
                if(minute.length() == 1) {
                    minute = "0" + minute;
                }
                String time = hour + ":" + minute;
                String description = eventDescription.getText().toString();
                String invitee = findMentors.getSelectedItem().toString();
                String inviteeId = ids.get(names.indexOf(invitee));
                Log.d("AddEventDialogFragment", invitee);
                Log.d("AddEventDialogFragment", inviteeId);
                model.setNewEventInfo(todayString, time, description, invitee, inviteeId);
                model.getFragmentManager().findFragmentById(R.id.flContainer);
                CalendarFragment calendarFragment = (CalendarFragment) model.getFragmentManager().findFragmentByTag("CalendarFragment");
                calendarFragment.createParseEvent();
                //getActivity().startActivityForResult(getActivity().getIntent(), 10);
                //sendEvent(todayString, time, description, invitee, inviteeId);
                dismiss();
            }
        });
    }

    private void sendEvent(String date, String time, String description, String invitee, String inviteeId) {
        Log.d("AddEventDialogFragment", date);
        Log.d("AddEventDialogFragment", time);
        Log.d("AddEventDialogFragment", description);
        Log.d("AddEventDialogFragment", invitee);
        Log.d("AddEventDialogFragment", inviteeId);
        mData.passNewEvent(date, time, description, invitee, inviteeId);
    }

}
