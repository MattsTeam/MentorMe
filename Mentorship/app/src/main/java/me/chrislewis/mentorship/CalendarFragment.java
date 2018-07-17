package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import me.chrislewis.mentorship.models.CurrentDayDecorator;

public class CalendarFragment extends Fragment implements OnDateSelectedListener{

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
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
        Collection<CalendarDay> days = new ArrayList<>();
        calendarView = view.findViewById(R.id.calendarView);
        todayText = view.findViewById(R.id.tvCurrentDay);
        calendar = Calendar.getInstance();
        calendarView.setDateSelected(calendar.getTime(), true);
        String todayString = dateFormat.format(calendar.getTime());
        Log.d("CalendarFragment", todayString);
        todayText.setText(todayString);
        CalendarDay day1 = CalendarDay.from(2018, 5, 25);
        CalendarDay day2 = CalendarDay.from(2018, 6, 30);
        days.add(day1);
        days.add(day2);
        calendarView.setOnDateChangedListener(this);
        calendarView.addDecorators(new CurrentDayDecorator(R.color.loginGreen, days));
        //calendarView.addDecorators(new CurrentDayDecorator(getActivity(), days));
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calendarDay, boolean b) {
        Log.d("CalendarFragment", "Selected something");
    }
}
