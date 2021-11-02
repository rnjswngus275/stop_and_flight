package com.example.stop_and_flight;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stop_and_flight.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_friend2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_friend2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private String UID;
    private String sent_friend_uid2;
    private String Nickname;
    private String Arrive;
    private String Depart;
    private String Todo;
    private String Nickname2;
    private String ticketDate = null;
    private String ticketTodo = null;
    private String ticketArrive = null;
    private String ticketDepart = null;
    private int ticketId = 0;
    ArrayList<friend_model3> friend_list3;


    private HashMap<String, Object> Map;
    int count=0;
    int count2=0;
    int count3=0;
    Ticket ticket=new Ticket();

    int num=1;
    public Fragment_friend2() {
        // Required empty public constructor
    }
    public static Fragment_friend2 newInstance(){
        Fragment_friend2 fragment = new Fragment_friend2();
        return fragment;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_friend2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_friend2 newInstance(String param1, String param2) {
        Fragment_friend2 fragment = new Fragment_friend2();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_friend2, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        ArrayList<friend_model3> friend_list3 =new ArrayList<>();
        ListView listView=(ListView)view.findViewById(R.id.list1);
        final friend_adapter3 adapter3=new friend_adapter3(getContext(),friend_list3);
        listView.setAdapter(adapter3);

        // 1.친구 uid 읽어오기
        mDatabase.child("users").child(UID).child("together").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_list3.clear();
                System.out.println("확인 트라이");

                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {

                    if (fileSnapshot != null) {
                        sent_friend_uid2 = fileSnapshot.getKey();
                        System.out.println("요청보낸친구: "+sent_friend_uid2);



                        //2. 친구 닉네임 읽어오기

                        final DatabaseReference ref2 = mDatabase.child("users").child(sent_friend_uid2).child("nickname");
                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Nickname = snapshot.getValue(String.class);
                                System.out.println("확인 진입2"+Nickname);
                                adapter3.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                    }

                    System.out.println("확인 2번 블록");
                }
                System.out.println("확인 3번 블록");

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }

        });
        // 2. 티켓정보 가져오기
        mDatabase.child("users").child(UID).child("together").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        String friend_uid2 = fileSnapshot.getKey();
                        System.out.println("together"+friend_uid2);

                        final DatabaseReference ref2 = mDatabase.child("users").child(UID).child("together").child(friend_uid2);
                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot fileSnapshot2 : snapshot.getChildren()){
                                    if (fileSnapshot2 != null){
                                        String date =fileSnapshot2.getKey();
                                        System.out.println("date!!!!!!"+date);
                                        ticketDate=date;

                                        for(DataSnapshot fileSnapshot3 : fileSnapshot2.getChildren()){
                                            System.out.println(fileSnapshot3);
                                            ticket=fileSnapshot3.getValue(Ticket.class);

                                            ticketTodo=ticket.getTodo();
                                            ticketArrive=ticket.getArrive_time();
                                            ticketDepart=ticket.getDepart_time();
                                            ticketId=ticket.getId();


                                        }

                                    }
                                }


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        final DatabaseReference ref3 = mDatabase.child("users").child(friend_uid2).child("nickname");
                        ref3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Nickname2 = snapshot.getValue(String.class);
                                System.out.println("2확인 진입2"+Nickname2);
                                list_add(Nickname,sent_friend_uid2,ticketDate,ticketArrive,ticketDepart,ticketTodo,ticketId);
                                adapter3.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                }
            }
            private void list_add(String Nickname, String sent_friend_uid2,String Date,String Arrive, String Depart, String Todo, int num) {
                //count 세기위해서
                mDatabase.child("users").child(sent_friend_uid2).child("friend").child("idle").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                            if (fileSnapshot != null) {
                                count2++;
                                System.out.println("확인 진입 count2"+count2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
                    }
                });

                friend_list3.add(new friend_model3(Nickname,sent_friend_uid2,Date,Arrive,Depart,Todo,num));
                System.out.println("리스트사이즈 : "+friend_list3.size());
                System.out.println("확인 함수안"+"/"+count+"/"+count2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });



        return view;
    }
}