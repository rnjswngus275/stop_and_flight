package com.doremifa.stop_and_flight.fragments;

        import android.content.Context;
        import android.os.Build;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.RecyclerView;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;

        import com.doremifa.stop_and_flight.R;
        import com.doremifa.stop_and_flight.friend_adapter2;
        import com.doremifa.stop_and_flight.friend_model2;
        import com.doremifa.stop_and_flight.utils.FriendSelectAdapter;
        import com.doremifa.stop_and_flight.utils.TaskDatabaseHandler;
        import com.doremifa.stop_and_flight.utils.TodoDatabaseHandler;
        import com.doremifa.stop_and_flight.model.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private String mParam3;

    private DatabaseReference mDatabase;
    private String UID;
    private String Nickname2;
    private String Message2;
    private String friend_uid2;
    private int updateId;
    private Fragment fragment;

    ArrayList<friend_model2> accept_friend_list;

    private RecyclerView selectFriendRecyclerView;
    private FriendSelectAdapter friendSelectAdapter;

    private Context context;
    private Task getTask;
    private Task getTodo;
    private TaskDatabaseHandler db;
    private TodoDatabaseHandler tododb;
    private HashMap<String, Object> TodoMap;
    private HashMap<String, Object> TaskMap;

    public SelectFriendFragment(Context context) {
        this.context = context;
        this.UID = UID;
        this.updateId = updateId;
        this.fragment = fragment;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFriendFragment newInstance(int param1, String param2, String param3, Context context) {
        SelectFriendFragment fragment = new SelectFriendFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if (user != null) {
            UID = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_friend, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if (user != null) {
            UID = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        ListView accept_friend_listView2=(ListView)v.findViewById(R.id.selectFriendListView);//리사이클뷰 정의(친구목록 보이게 하는곳)

        accept_friend_list = new ArrayList<>();

        final friend_adapter2 adapter2=new friend_adapter2(getContext(),accept_friend_list);//리스트뷰와 리스트를 연결하기 위한 어댑터
        accept_friend_listView2.setAdapter(adapter2);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        accept_friend_listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                final friend_model2 selected_frinend_UID = accept_friend_list.get(position);

                TicketingFragment ticketingFragment = new TicketingFragment(context);
                Bundle bundle = new Bundle();
                bundle.putInt("Id", mParam1);
                bundle.putString("Todo", mParam3);
                bundle.putString("Friend", selected_frinend_UID.getUID());
                ticketingFragment.setArguments(bundle);
                ((TicketingBottomSheetDialog) getParentFragment()).DialogReplaceFragment(ticketingFragment);

            }
        });

        friendSelectAdapter = new FriendSelectAdapter(getContext(),accept_friend_list);

        //1.친구 uid 읽어오기
        mDatabase.child("users").child(UID).child("friend").child("accept").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accept_friend_list.clear();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        friend_uid2 = fileSnapshot.getValue(String.class);
                        //2. 친구 닉네임 읽어오기
                        final DatabaseReference ref2 = mDatabase.child("users").child(friend_uid2).child("nickname");
                        ref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Nickname2 = snapshot.getValue(String.class);
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
                                list_add2(Nickname2, Message2, friend_uid2);
                                adapter2.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
        Collections.reverse(accept_friend_list);
        return v;
    }

    public void list_add2(String Nickname2, String Message2, String Friend_Uid2) {
        accept_friend_list.add(new friend_model2(Nickname2, Message2, Friend_Uid2));
    }
}