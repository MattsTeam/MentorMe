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

public class GoogleDayDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private final int color;
    private CalendarDay calendarDay;
    private String googleDateString;
    private Date calendarDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public GoogleDayDecorator(int color, ArrayList<com.google.api.services.calendar.model.Event> googleEvents) {
        this.color = color;
        Collection<CalendarDay> dates = new ArrayList<>();
        for(int i = 0; i < googleEvents.size(); i++) {
            googleDateString = googleEvents.get(i).getStart().getDateTime().toString();
            try {
                calendarDate = dateFormat.parse(googleDateString);
                calendarDay = CalendarDay.from(calendarDate);
            } catch (ParseException e) {
                Log.d("DayDecorator", "Unable to parse date string.");
                e.printStackTrace();
            }
            dates.add(calendarDay);
        }
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return dates.contains(calendarDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
    }

}

