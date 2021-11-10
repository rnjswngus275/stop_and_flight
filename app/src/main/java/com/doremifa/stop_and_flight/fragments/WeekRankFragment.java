package com.doremifa.stop_and_flight.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doremifa.stop_and_flight.R;
import com.doremifa.stop_and_flight.model.CurTime;
import com.doremifa.stop_and_flight.model.UserInfo;
import com.doremifa.stop_and_flight.utils.RankingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekRankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UID;
    private Context context;
    private final ArrayList<UserInfo> UserInfos = new ArrayList<UserInfo>();
    private HashMap<String, Object> UserMap = new HashMap<>();
    private HashMap<String, Object> DateMap = new HashMap<>();
    private DatabaseReference mDatabase;
    private RankingAdapter rankingAdapter;
    private RecyclerView WeekRankRecyclerView;

    public WeekRankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekRankFragment newInstance(String param1, String param2) {
        WeekRankFragment fragment = new WeekRankFragment();
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_week_rank, container, false);

        rankingAdapter = new RankingAdapter(context);
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        WeekRankRecyclerView = view.findViewById(R.id.weekRankRecyclerView);

        CurTime curTime = new CurTime();
        getWeekRankingDB(view, curTime);

        WeekRankRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rankingAdapter.setRank(UserInfos);
        WeekRankRecyclerView.setAdapter(rankingAdapter);


        return view;
    }

    // 필요한 DB 정보 - 닉네임 & 이용 시간별 (일간 / 주간)
    private void getWeekRankingDB(View view, CurTime curTime)
    {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfos.clear();
                String mynickname = "";
                if (snapshot != null)
                {
                    for (DataSnapshot UserSnapshot : snapshot.getChildren())
                    {
                        UserMap = (HashMap<String, Object>) UserSnapshot.getValue();
                        String Nickname = String.valueOf(UserMap.get("nickname"));
                        int sum = 0;
                        if (UserSnapshot.child("date").getValue() != null)
                        {
                            DateMap = (HashMap<String, Object>) UserSnapshot.child("date").getValue();
                            for (String day : curTime.getCurWeek()){

                                //ask your app running more modern API as level 24 (Build.VERSION_CODES.N(ougat))
                                int Studytime = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) ?
                                        Integer.parseInt(String.valueOf(DateMap.getOrDefault(day, 0))) :
                                    // if not, then need to solve with similar code of original code in next below
                                    ((DateMap.get(day) != null) ? Integer.parseInt(String.valueOf(DateMap.get(day))) : 0);
                                sum += Studytime;
                            }
                        }
                        if (UserSnapshot.getKey().equals(UID))
                        {
                            TextView weeknickname = view.findViewById(R.id.weekRanknickName);
                            TextView weekRankTime = view.findViewById(R.id.weekRankTime);
                            mynickname = String.valueOf(UserMap.get("nickname"));
                            weeknickname.setText(mynickname);
                            weekRankTime.setText(String.valueOf(sum));
                        }
                        UserInfo userInfo = new UserInfo(Nickname, sum);
                        UserInfos.add(userInfo);
                    }
                }
                Collections.sort(UserInfos, new Comparator<UserInfo>() {
                    @Override
                    public int compare(UserInfo o1, UserInfo o2) {
                        if (o1.getStudytime() > o2.getStudytime())
                            return 1;
                        else if (o1.getStudytime() == o2.getStudytime())
                            return 0;
                        else
                            return -1;
                    }
                });


                Collections.reverse(UserInfos);
                TextView firsttitle = view.findViewById(R.id.firstTitle);
                TextView secondTitle = view.findViewById(R.id.secondTitle);
                TextView thirdTitle = view.findViewById(R.id.thirdTitle);
                TextView weekRankgrade = view.findViewById(R.id.weekRankgrade);
                TextView Totalsize = view.findViewById(R.id.totalsize);

                TextView rankingPercent = view.findViewById(R.id.rankingPercent);


                firsttitle.setText(UserInfos.get(0).getNickname());
                secondTitle.setText(UserInfos.get(1).getNickname());
                thirdTitle.setText(UserInfos.get(2).getNickname());
                Totalsize.setText(String.valueOf(UserInfos.size()));
                float percent = (getArraylistIndex(UserInfos, mynickname) / (float) UserInfos.size()) * 100;
                String percentage = String.format("%.2f", percent) + "%";

                rankingPercent.setText(percentage);

                weekRankgrade.setText(String.valueOf(getArraylistIndex(UserInfos, mynickname)));
                rankingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private int getArraylistIndex(ArrayList<UserInfo> arrayList, String nickname)
    {
        for (int i = 0; i < arrayList.size(); i++)
        {
            if(arrayList.get(i).getNickname() == nickname)
                return i + 1;
        }
        return -1;
    }
}