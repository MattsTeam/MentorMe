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


public class ApiAsyncTask extends AsyncTask<Void, Void, List<Event>> {
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
        void processFinish(List<Event> output);
    }

    @Override
    protected void onPostExecute(List<Event> result) {
        response.processFinish(result);
    }

    @Override
    protected List<Event> doInBackground(Void... params) {
        List<Event> ApiData = new ArrayList<>();
        try {
            Log.d("ApiAsyncTask", "Getting data from API");
            ApiData = getDataFromApi();

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            Log.d("ApiAsyncTask", "Google play service availability exception");

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

    private List<Event> getDataFromApi() throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = calendarFragment.mService.events().list("primary")
                /*.setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)*/
                .execute();
        Log.d("ApiAsyncTask", "Finished getting API data");
        Log.d("ApiAsyncTask", "size: " + Integer.toString(events.size()));
        List<Event> items = events.getItems();
        return items;
    }

}