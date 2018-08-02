package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.ParseEvent;

public class EventDialogFragment extends DialogFragment{

    private TextView tvDate;
    private List<ParseEvent> eventList = new ArrayList<>();
    private List<com.google.api.services.calendar.model.Event> googleEventList = new ArrayList<>();
    private Date todayDate;
    private RecyclerView rvEvents;
    private CalendarEventsAdapter allEventsAdapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat todayFormat = new SimpleDateFormat("EEE MMM dd, yyyy");

    public EventDialogFragment() { }

    public static EventDialogFragment newInstance() {
        EventDialogFragment frag = new EventDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle mainBundle = getArguments();
        Bundle todayEvents = mainBundle.getBundle("parseBundle");
        eventList = (List<ParseEvent>) todayEvents.getSerializable("todayEvents");
        Bundle googleTodayEvents = mainBundle.getBundle("googleBundle");
        googleEventList = (List<com.google.api.services.calendar.model.Event>) googleTodayEvents.getSerializable("todayGoogleEvents");
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_event_dialog);
        return inflater.inflate(R.layout.fragment_event_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allEventsAdapter = new CalendarEventsAdapter(eventList, googleEventList);
        tvDate = view.findViewById(R.id.tvDate);
        rvEvents = view.findViewById(R.id.rvEvents);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(allEventsAdapter);

        try {
            if(eventList.size() > 0) {
                todayDate = dateFormat.parse(eventList.get(0).getDateString());
            }
            else {
                todayDate = dateFormat.parse(googleEventList.get(0).getStart().getDateTime().toString());
            }
        } catch (ParseException e) {
            Log.d("EventDialog", "Failed to parse date string.");
            e.printStackTrace();
        }
        tvDate.setText(todayFormat.format(todayDate));
    }


}
