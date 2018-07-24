package me.chrislewis.mentorship;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ApiAsyncTask extends AsyncTask<Void, Void, List<String>> {
    private FragmentActivity mActivity;
    private CalendarFragment calendarFragment;
    public AsyncResponse response = null;

    ApiAsyncTask(FragmentActivity activity, AsyncResponse response) {
        Log.d("ApiAsyncTask", "Create new ApiAsyncTask");
        mActivity = activity;
        this.response = response;
        calendarFragment = (CalendarFragment) mActivity.getSupportFragmentManager().findFragmentByTag("CalendarFragment");
    }

    public interface AsyncResponse {
        void processFinish(List<String> output);
    }

    @Override
    protected void onPostExecute(List<String> result) {
        response.processFinish(result);
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<String> ApiData = new ArrayList<>();
        try {
            Log.d("ApiAsyncTask", "Getting data from API");
            ApiData = getDataFromApi();

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            Log.d("ApiAsyncTask", "Google play service availability exception");
            /*calendarFragment.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());*/

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            Log.d("ApiAsyncTask", "User auth exception");
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    calendarFragment.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
            Log.d("ApiAsyncTask", "Error occured");
            Log.d("ApiAsyncTask", e.getMessage());
        }
        return ApiData;
    }

    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Log.d("ApiAsyncTask", "Starting to get API data");
        Events events = calendarFragment.mService.events().list("primary")
                /*.setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)*/
                .execute();
        List<Event> items = events.getItems();
        Log.d("ApiAsyncTask", "Number of events: " + Integer.toString(items.size()));

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
        }
        return eventStrings;
    }

}