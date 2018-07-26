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
import android.widget.TimePicker;


public class AddEventDialogFragment extends DialogFragment {

    public interface OnReceivedData {
        public void passNewEvent(String description);
    }

    private Button addEventButton;
    private TimePicker timePicker;
    private EditText eventDescription;
    private Boolean isSynced;
    private String todayString;
    private OnReceivedData mData;

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
        Log.d("AddEventDialogFragment", "isSynced: " + Boolean.toString(isSynced));
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_event_dialog);
        return inflater.inflate(R.layout.fragment_add_event, container);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData = (OnReceivedData) getActivity();
        timePicker = view.findViewById(R.id.simpleTimePicker);
        eventDescription = view.findViewById(R.id.editDetails);
        addEventButton = view.findViewById(R.id.doneButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = timePicker.getHour() + ":" + timePicker.getMinute();
                String description = eventDescription.getText().toString();
                if(time.length() == 4) {
                    time = "0" + time;
                }
                Log.d("AddEventDialogFragment", "Time: " + time);
                if(isSynced) {
                    Bundle addBundle = new Bundle();
                    addBundle.putString("eventTime", time);
                    addBundle.putString("eventDecription", description);
                    //sendEvent(description);
                    dismiss();
                }
                else {
                    //add to parse calendar
                }
            }
        });
    }

    private void sendEvent(String description) {
        mData.passNewEvent(description);
    }

}
