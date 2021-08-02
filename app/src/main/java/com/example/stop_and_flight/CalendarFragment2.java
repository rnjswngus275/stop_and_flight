package com.example.stop_and_flight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CalendarFragment2 extends ToolBarFragment {

    @NonNull
    public static CalendarFragment2 newInstance() {
        return new CalendarFragment2();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        setToolbar();
        return view;
    }

}
