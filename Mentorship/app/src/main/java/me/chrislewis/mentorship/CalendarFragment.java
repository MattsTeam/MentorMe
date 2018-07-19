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

    // adding event into Google Calendar
    /*
    //add an event to calendar
    private void addEvent() {
        ContentValues l_event = new ContentValues();
        l_event.put("calendar_id", m_selectedCalendarId);
        l_event.put("title", "roman10 calendar tutorial test");
        l_event.put("description", "This is a simple test for calendar api");
        l_event.put("eventLocation", "@home");
        l_event.put("dtstart", System.currentTimeMillis());
        l_event.put("dtend", System.currentTimeMillis() + 1800*1000);
        l_event.put("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_event.put("eventStatus", 1);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        l_event.put("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        l_event.put("transparency", 0);
        //0~ false; 1~ true
        l_event.put("hasAlarm", 1);
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8 ) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = this.getContentResolver().insert(l_eventUri, l_event);
        Log.v("++++++test", l_uri.toString());
    }
     */

    // adding event through intent, doesn't require any permissions
    /*
    private void addEvent2() {
        Intent l_intent = new Intent(Intent.ACTION_EDIT);
        l_intent.setType("vnd.android.cursor.item/event");
        //l_intent.putExtra("calendar_id", m_selectedCalendarId);  //this doesn't work
        l_intent.putExtra("title", "roman10 calendar tutorial test");
        l_intent.putExtra("description", "This is a simple test for calendar api");
        l_intent.putExtra("eventLocation", "@home");
        l_intent.putExtra("beginTime", System.currentTimeMillis());
        l_intent.putExtra("endTime", System.currentTimeMillis() + 1800*1000);
        l_intent.putExtra("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_intent.putExtra("eventStatus", 1);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        l_intent.putExtra("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        l_intent.putExtra("transparency", 0);
        //0~ false; 1~ true
        l_intent.putExtra("hasAlarm", 1);
        try {
            startActivity(l_intent);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
        }
    }
    */

}
