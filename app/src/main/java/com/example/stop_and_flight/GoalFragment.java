package com.example.stop_and_flight;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import  androidx.recyclerview.widget.RecyclerView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int count = 0;
    private int edit_count=0;
    private int id_count = 0;
    private int current_id=0;
    private View.OnClickListener editConfirmListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know

        }
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerview;
    private Context context;

    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance(String param1, String param2) {
        GoalFragment fragment = new GoalFragment();
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


        //Spiner
        /*
        Spinner tagSpinner = (Spinner)getView().findViewById(R.id.tag);
        Spinner editTagSpinner = (Spinner)getView().findViewById(R.id.edit_tag);

        ArrayAdapter tagAdapter = ArrayAdapter.createFromResource( getContext(),

                R.array.tag_value, android.R.layout.simple_spinner_item);

        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagSpinner.setAdapter(tagAdapter);
        editTagSpinner.setAdapter(tagAdapter);

         */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_goal,container,false);

        LinearLayout linear = (LinearLayout)rootView.findViewById(R.id.linear);
        LinearLayout addLinear = (LinearLayout)rootView.findViewById(R.id.addLayout);
        LinearLayout editLinear = (LinearLayout)rootView.findViewById(R.id.editLayout);

        Button addBtn = (Button)rootView.findViewById(R.id.addGoal);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(count%2==0) {
                    addLinear.setVisibility(rootView.VISIBLE);
                    count++;
                }else{
                    addLinear.setVisibility(rootView.GONE);
                    count++;
                }
            }
        });

        EditText goalText = (EditText)rootView.findViewById(R.id.text);
        EditText editGoalText = (EditText)rootView.findViewById(R.id.edit_text);

        Button confirmBtn = (Button)rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = new Button(getContext());
                btn.setText(goalText.getText());
                btn.setId(id_count+1);
                /*목표버튼 하나하나에 달리는 리스너!*/
                btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               if (edit_count % 2 == 0) {
                                                   editLinear.setVisibility(rootView.VISIBLE);
                                                   edit_count++;

                                                   editGoalText.setText(btn.getText());
                                                   current_id = btn.getId();
                                               } else {
                                                   editLinear.setVisibility(rootView.GONE);
                                                   edit_count++;
                                               }
                                           }
                                       });
                linear.addView(btn);
                id_count++;
                goalText.setText("목표를 임력하세요");

                if(count%2==0) {
                    addLinear.setVisibility(rootView.VISIBLE);
                    count++;
                }else{
                    addLinear.setVisibility(rootView.GONE);
                    count++;
                }
            }
        });//confirmBtn 리스너 끝(add의 Confirm)

        Button EditConfirmBtn = (Button)rootView.findViewById(R.id.edit_confirm_button);
        EditConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = (Button)rootView.findViewById(current_id);
                btn.setText(editGoalText.getText());

                if(edit_count%2==0) {
                    editLinear.setVisibility(rootView.VISIBLE);
                    edit_count++;
                }else{
                    editLinear.setVisibility(rootView.GONE);
                    edit_count++;
                }
            }
        });

        Button deleteBtn = (Button)rootView.findViewById(R.id.edit_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = (Button)rootView.findViewById(current_id);
                btn.setVisibility(rootView.GONE);

                if(edit_count%2==0) {
                    editLinear.setVisibility(rootView.VISIBLE);
                    edit_count++;
                }else{
                    editLinear.setVisibility(rootView.GONE);
                    edit_count++;
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;

        //View rootView = (View)inflater.inflate(R.layout.fragment_search,container,false);

    }


}