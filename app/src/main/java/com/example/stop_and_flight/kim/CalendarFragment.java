package com.example.stop_and_flight.kim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.kwon.ToolBarFragment;

public class CalendarFragment extends ToolBarFragment {

    @NonNull
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        setToolbar();
        return view;
    }

}
