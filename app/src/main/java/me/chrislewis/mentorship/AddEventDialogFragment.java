package me.chrislewis.mentorship;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddEventDialogFragment extends DialogFragment {

    public interface OnReceivedData {
        public void passNewEvent(String date, String time, String description);
    }

    private Button addEventButton;
    private TimePicker timePicker;
    private EditText eventDescription;
    private Boolean isSynced;
    private TextView selectTime;
    private String todayString;
    private OnReceivedData mData;
    private Date date;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                sendEvent(todayString, time, description);
                dismiss();
            }
        });
    }

    private void sendEvent(String date, String time, String description) {
        mData.passNewEvent(date, time, description);
    }

}
