package me.chrislewis.mentorship;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.parse.ParseUser;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    SharedViewModel model;

    public SelectDateFragment() { }

    public static SelectDateFragment newInstance() {
        SelectDateFragment frag = new SelectDateFragment();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), R.style.datepicker,this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        String yearStr = Integer.toString(year);
        String monthStr = Integer.toString(month);
        String dateStr = Integer.toString(date);
        String selectedDate = yearStr + "-" + monthStr + "-" + dateStr;
        Log.d("SelectDateFragment", selectedDate);
        AddEventDialogFragment addEventFragment = new AddEventDialogFragment();
        Bundle addEventBundle = new Bundle();
        addEventBundle.putString("dateSelected", selectedDate);
        addEventBundle.putBoolean("isSynced", ParseUser.getCurrentUser().getBoolean("allowSync"));
        addEventFragment.setArguments(addEventBundle);
        addEventFragment.show(model.getFragmentManager(), null);
    }
}
