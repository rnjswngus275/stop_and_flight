package com.example.stop_and_flight;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stop_and_flight.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_friend1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_friend1 extends Fragment implements OnItemClick{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private String UID;
    private String Nickname;
    private String Message;
    private String friend_uid;
    private String UID2;
    private String Nickname2;
    private String Message2;
    private String friend_uid2;

    int num_idle=0;
    int num_myidle=0;
    int count=0;
    int count2=0;
    int count3=0;
    ArrayList<friend_model> friend_list;
    ArrayList<friend_model2>friend_list2;

    FirebaseAuth mAuth;


    public Fragment_friend1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_friend1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_friend1 newInstance(String param1, String param2) {
        Fragment_friend1 fragment = new Fragment_friend1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment_friend1 newInstance(){
        Fragment_friend1 fragment = new Fragment_friend1();
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

        View view = inflater.inflate(R.layout.fragment_friend1, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        System.out.println(UID);
        TextView code = view.findViewById(R.id.code);
        code.setText(UID);
        Button friend_add=view.findViewById(R.id.friend_add);

        ImageButton copy=(ImageButton)view.findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클립보드 사용 코드
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("CODE",UID); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                //복사가 되었다면 토스트메시지 노출
                Toast.makeText(getContext(),"ID가 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
//count 세기위해서
        mDatabase.child("users").child(UID).child("friend").child("idle").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        count++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
        System.out.println("확인 count"+count);

        friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et2 = new EditText(getContext());


                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setTitle("추가 할 친구의 코드를 입력하세요")
                        .setMessage("친구코드 입력")

                        .setCancelable(false)
                        .setView(et2)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String friend_UID = et2.getText().toString();
                                mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
                                //count 세기위해서
                                mDatabase.child("users").child(friend_UID).child("friend").child("idle").addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                                            if (fileSnapshot != null) {
                                                count3++;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
                                    }
                                });
                                mDatabase.child("users").child(UID).child("friend").child("myidle").child(String.valueOf(count)).setValue(friend_UID);          //내 db에 update
                                mDatabase.child("users").child(friend_UID).child("friend").child("idle").child(String.valueOf(count3)).setValue(UID);//친구 db에 update
                                Toast.makeText(getActivity(),"친구신청을 보냈습니다"+friend_UID,Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });
        friend_list=new ArrayList<>();

        ListView listView=(ListView)view.findViewById(R.id.list1);
        final friend_adapter adapter=new friend_adapter(getContext(),friend_list);
        listView.setAdapter(adapter);

        friend_list2=new ArrayList<>();
        ListView listView2=(ListView)view.findViewById(R.id.list2);
        final friend_adapter2 adapter2=new friend_adapter2(getContext(),friend_list2);
        listView2.setAdapter(adapter2);

//친구목록 읽어오기 LIST1
        //1.친구 uid 읽어오기
                mDatabase.child("users").child(UID).child("friend").child("idle").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    friend_list.clear();
                    System.out.println("확인 트라이");

                    for (DataSnapshot fileSnapshot : snapshot.getChildren()) {

                        if (fileSnapshot != null) {
                            friend_uid = fileSnapshot.getValue(String.class);
                            System.out.println("확인 진입1"+friend_uid);



                            //2. 친구 닉네임 읽어오기

                                final DatabaseReference ref2 = mDatabase.child("users").child(friend_uid).child("nickname");
                                ref2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Nickname = snapshot.getValue(String.class);
                                        System.out.println("확인 진입2"+Nickname);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });



                            //3. 친구 상태 메세지 읽어오기
                                final DatabaseReference ref3 = mDatabase.child("users").child(friend_uid).child("message");
                                ref3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        Message = snapshot2.getValue(String.class);
                                        System.out.println("확인 진입3"+Nickname+Message+friend_uid+"/"+count+"/"+count2);

                                        list_add(Nickname,Message,friend_uid,count-1);
                                        adapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }

                                });

                            System.out.println("확인 1번 블록"+Nickname+Message+friend_uid);

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


//LIST2 데이터 ADD
        //1.친구 uid 읽어오기
        mDatabase.child("users").child(UID).child("friend").child("accept").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend_list2.clear();
                System.out.println("확인 트라이");

                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {

                    if (fileSnapshot != null) {
                        friend_uid2 = fileSnapshot.getValue(String.class);
                        System.out.println("2확인 진입1"+friend_uid2);

                        //2. 친구 닉네임 읽어오기

                        final DatabaseReference ref2 = mDatabase.child("users").child(friend_uid2).child("nickname");
                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Nickname2 = snapshot.getValue(String.class);
                                System.out.println("2확인 진입2"+Nickname2);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });



                        //3. 친구 상태 메세지 읽어오기
                        final DatabaseReference ref3 = mDatabase.child("users").child(friend_uid2).child("message");
                        ref3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                Message2 = snapshot2.getValue(String.class);

                                list_add2(Nickname2,Message2,friend_uid2);
                                adapter2.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });

                        System.out.println("2확인 1번 블록"+Nickname2+Message2+friend_uid2);

                    }

                    System.out.println("2확인 2번 블록");
                }
                System.out.println("2확인 3번 블록");

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }

        });

        return view;
    }

    @Override
    public void onClick(String value) {

    }

    public void list_add(String Nickname,String Message,String Friend_Uid,int count){
        //count 세기위해서
        mDatabase.child("users").child(Friend_Uid).child("friend").child("idle").addValueEventListener(new ValueEventListener() {
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

        friend_list.add(new friend_model(Nickname,Message,Friend_Uid,String.valueOf(count),String.valueOf(count2)));
        System.out.println(friend_list.size());
        System.out.println("확인 함수안"+"/"+count+"/"+count2);

    }
    public void list_add2(String Nickname2,String Message2,String Friend_Uid2){

        friend_list2.add(new friend_model2(Nickname2,Message2,Friend_Uid2));


    }
}
