package me.chrislewis.mentorship.models;


import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


public class CurrentDayDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private final int color;
    private CalendarDay calendarDay;
    private String dateString;
    private Date calendarDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CurrentDayDecorator(int color, ArrayList<ParseEvent> events) {
        this.color = color;
        Collection<CalendarDay> dates = new ArrayList<>();
        for(int i = 0; i < events.size(); i++) {
            dateString = events.get(i).getDateString();
            Log.d("DayDecorator", dateString);
            try {
                Log.d("DayDecorator", "Parsed date string!");
                calendarDate = dateFormat.parse(dateString);
                calendarDay = CalendarDay.from(calendarDate);
            } catch (ParseException e) {
                Log.d("DayDecorator", "Unable to parse date string.");
                e.printStackTrace();
            }
            dates.add(calendarDay);
        }
        this.dates = new HashSet<>(dates);
    }

/*    public CurrentDayDecorator(int color, ArrayList<com.google.api.services.calendar.model.Event> events) {
        this.color = color;
        Collection<CalendarDay> dates = new ArrayList<>();
        for(int i = 0; i < events.size(); i++) {
            googleDate = events.get(i).getStart();
            Log.d("DayDecorator", dateString);
            try {
                Log.d("DayDecorator", "Parsed date string!");
                calendarDate = dateFormat.parse(dateString);
                calendarDay = CalendarDay.from(calendarDate);
            } catch (ParseException e) {
                Log.d("DayDecorator", "Unable to parse date string.");
                e.printStackTrace();
            }
            dates.add(calendarDay);
        }
        this.dates = new HashSet<>(dates);
    }*/

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.setSelectionDrawable(drawable);
        view.addSpan(new DotSpan(10, color));
    }
}