package com.example.stop_and_flight.lee.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

import android.widget.DatePicker;

import com.example.stop_and_flight.singleton.CalendarSingle;

import static java.lang.String.valueOf;


public class CalendarModel {
    private DatabaseReference databaseReference;
    private DatePicker.OnDateChangedListener onDataChangedListener;
    private List<CalendarSingle> mCalendars = new ArrayList<>();

    public void addCalendarModel(CalendarSingle calendarSingle) {
        mCalendars.add(calendarSingle);
    }
    public List<CalendarSingle> getmCalendars() {
        return this.mCalendars;
    }


    public CalendarModel(String postType) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(postType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (onDataChangedListener != null) {
                    /*onDataChangedListener.onDateChanged();*/
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    int getYear = ds.getValue(CalendarSingle.class).getYear();
                    Log.i("가져옴", valueOf(getYear));
                    int getMonth = ds.getValue(CalendarSingle.class).getMonth();
                    Log.i("가져옴", valueOf(getMonth));
                    int getDay = ds.getValue(CalendarSingle.class).getDay();
                    Log.i("가져옴", valueOf(getDay));
                    String getNote = ds.getValue(CalendarSingle.class).getNote();

                    CalendarSingle calendarSingle = new CalendarSingle(getNote, getYear, getMonth, getDay, "캘린더");
                    addCalendarModel(calendarSingle);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void writeCalendar( String postType, int y, int m, int d, String note) {
        databaseReference.child(postType).push().setValue(CalendarSingle.newCalendar(note,y,m,d,postType));
    }//등록

}
