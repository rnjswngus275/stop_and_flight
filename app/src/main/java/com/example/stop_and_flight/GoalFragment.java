package com.example.stop_and_flight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
    private String UID;
    private DatabaseReference mDatabase;


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
            UID = getArguments().getString("UID", "0");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        // + 버튼을 눌렀을 때
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count % 2 == 0) {
                    addLinear.setVisibility(rootView.VISIBLE);
                    count++;
                }else{
                    addLinear.setVisibility(rootView.GONE);
                    count++;
                }
            }
        });
        EditText goalText = (EditText)rootView.findViewById(R.id.goal_text);
        EditText editGoalText = (EditText)rootView.findViewById(R.id.edit_text);
        Button confirmBtn = (Button)rootView.findViewById(R.id.confirm_button);

        // 목표를 설정하고 확인버튼을 눌렀을 때
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = new Button(getContext());
                btn.setText(goalText.getText());
                btn.setId(id_count);
                insert_GOALDB(goalText, id_count);
                // 리스트를 다시 눌렀을 때
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
                btn.setId(id_count);
                id_count++;
                if(count % 2==0) {
                    addLinear.setVisibility(rootView.VISIBLE);
                    count++;
                }else{
                    addLinear.setVisibility(rootView.GONE);
                    count++;
                }
            }
        });

        Button EditConfirmBtn = (Button)rootView.findViewById(R.id.edit_confirm_button);
        // 수정을 한 후 확인버튼을 눌렀 때
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
                update_GOALDB(editGoalText.getText().toString(), current_id);
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
                delete_GOALDB(current_id);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private void insert_GOALDB(EditText goalText, int id_count) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String goal_title = goalText.getText().toString();
        Goal goal = new Goal(goal_title, id_count);

        mDatabase.child("GOAL").child(UID).child(Integer.toString(id_count)).setValue(goal);
    }

    private void update_GOALDB(String text, int id_count) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.child("GOAL").child(UID).child(Integer.toString(id_count)).getKey();
        Goal goal = new Goal(text, id_count);

        Map<String, Object> postValues = goal.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/GOAL/" + UID + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void delete_GOALDB(int id_count)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("GOAL").child(UID).child(Integer.toString(id_count)).removeValue();
    }
}