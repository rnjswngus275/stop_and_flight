package com.example.stop_and_flight;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassportFragment extends Fragment {

    ViewPager2 viewPager2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassportFragment newInstance(String param1, String param2) {
        PassportFragment fragment = new PassportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_passport,container,false);
        viewPager2 = rootView.findViewById(R.id.viewPager2);

        ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage(1,"2021-05-23"));
        list.add(new DataPage(2, "2021-05-26"));
        list.add(new DataPage(3, "2021-05-27"));
        viewPager2.setAdapter(new ViewPagerAdapter(list));

        // Inflate the layout for this fragment
        return rootView;
    }
}