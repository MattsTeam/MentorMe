package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.chrislewis.mentorship.models.CurrentDayDecorator;
import me.chrislewis.mentorship.models.Event;

public class CalendarFragment extends Fragment implements OnDateSelectedListener{

    SimpleDateFormat todayFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<Event> days = new ArrayList<>();
    ArrayList<Event> todayList = new ArrayList<>();
    MaterialCalendarView calendarView;
    Calendar calendar;
    TextView todayText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, parent, false);

    }


    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshEvents();
        calendarView = view.findViewById(R.id.calendarView);
        todayText = view.findViewById(R.id.tvCurrentDay);
        calendar = Calendar.getInstance();
        calendarView.setDateSelected(calendar.getTime(), true);
        todayText.setText(todayFormat.format(calendar.getTime()));
        calendarView.setOnDateChangedListener(this);
        int eventColor = getIntFromColor(255, 128, 0);
        calendarView.addDecorators(new CurrentDayDecorator(eventColor, days));
    }

    public void refreshEvents() {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.setLimit(15);
        try {
            days.clear();
            List<Event> events = query.find();
            days.addAll(events);

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay calendarDay, boolean b) {
        ParseQuery<Event> selectedDayQuery = new Event.Query();
        String selectedDate = dateFormat.format(calendarDay.getDate());
        Log.d("CalendarFragment", selectedDate);
        selectedDayQuery.whereEqualTo("dateString", selectedDate);
        selectedDayQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, com.parse.ParseException e) {
                if(e == null && objects.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("todayEvents", (Serializable) objects);

                    //Show events for today in modal overlay
                    FragmentManager fragmentManager = getFragmentManager();
                    EventDialogFragment fragment = EventDialogFragment.newInstance();
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "event_dialog_fragment");
                }
            }
        });
    }

    private void showEventDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        EventDialogFragment editNameDialogFragment = EventDialogFragment.newInstance();
        editNameDialogFragment.show(fragmentManager, "event_dialog_fragment");
    }


    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000;
        Green = (Green << 8) & 0x0000FF00;
        Blue = Blue & 0x000000FF;

        return 0xFF000000 | Red | Green | Blue;
    }
}
