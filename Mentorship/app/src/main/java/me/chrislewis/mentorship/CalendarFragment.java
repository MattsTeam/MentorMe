package me.chrislewis.mentorship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
