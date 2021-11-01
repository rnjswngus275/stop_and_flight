package com.example.stop_and_flight.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stop_and_flight.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketingBottomSheetDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketingBottomSheetDialog extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private String Todo = null;
    private String Friend = null;
    private int updateId;

    public TicketingBottomSheetDialog(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketingBottomSheetDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketingBottomSheetDialog newInstance(String param1, String param2, Context context) {
        TicketingBottomSheetDialog fragment = new TicketingBottomSheetDialog(context);
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
            Todo = getArguments().getString("Todo");
            Friend = getArguments().getString("Friend");
            updateId = getArguments().getInt("Id");

        }
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentTransaction childFt = getChildFragmentManager().beginTransaction(); //fragment 매니저
        TicketingFragment ticketingFragment = new TicketingFragment(context);

        // update가 필요한 경우 calenderfragment에서 bundle를 통해 값을 전달하기 때문에 Todo라는 키값을 확인해서 값이 있으면 ticket으로 전달.
        if (Todo != null)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("Id", updateId);
            bundle.putString("Todo", Todo);
            bundle.putString("Friend", Friend);
            ticketingFragment.setArguments(bundle);
        }
        childFt.replace(R.id.dialog_container, ticketingFragment);
        childFt.addToBackStack(null);
        childFt.commit();
        return inflater.inflate(R.layout.fragment_ticketing_bottom_sheet_dialog, container, false);
    }

    public void DialogReplaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.dialog_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}