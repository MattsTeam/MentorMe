package me.chrislewis.mentorship.models;


import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;


public class CurrentDayDecorator implements DayViewDecorator {

    private Drawable drawable;
    private final HashSet<CalendarDay> dates;
    private final int color;


    //public CurrentDayDecorator(Activity context, Collection<CalendarDay> dates) {
    public CurrentDayDecorator(int color, Collection<CalendarDay> dates) {
        //drawable = ContextCompat.getDrawable(context, R.drawable.ic_calendar);
        this.color = color;
        this.dates = new HashSet<>(dates);
    }


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