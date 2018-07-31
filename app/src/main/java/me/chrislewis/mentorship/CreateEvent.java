package me.chrislewis.mentorship;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CreateEvent extends AsyncTask<Void, Void, Void> {
    Calendar mService;
    String eventDay;
    String eventTime;
    String description;
    String inviteeName;
    Date eventDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    CreateEvent(Calendar mService, String date, String time, String description, String invitee) throws ParseException {
        this.mService = mService;
        this.description = description;
        eventDay = date;
        eventTime = time;
        inviteeName = invitee;
        eventDate = dateFormat.parse(eventDay + " " + eventTime);
    }

    @Override
    protected Void doInBackground(Void... params) {
        addCalendarEvent();
        return null;
    }

    public void addCalendarEvent() {
        Event event = new Event()
                .setSummary(description)
                .setLocation("Not specified")
                .setDescription("");

        DateTime startDateTime = new DateTime(eventDate);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventDate);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        //String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        //event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(inviteeName)
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            Log.d("CreateEventTask", "Added an event");
            mService.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            Log.d("CreateEventTask", "Failed to create a new event.");
            e.printStackTrace();
        }
    }
}