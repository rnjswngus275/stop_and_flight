package com.example.stop_and_flight;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.stop_and_flight.model.Ticket;
import com.example.stop_and_flight.utils.TicketDatabaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class friend_adapter3 extends BaseAdapter implements View.OnClickListener {
    Context mContext=null;
    LayoutInflater mLayoutInflater=null;
    ArrayList<friend_model3> friend;
    String picturePath;
    Uri selectedImage;
    Fragment_friend2 friend1;
    private DatabaseReference mDatabase;
    private String UID;
    int count=0;
    int count2=0;
    int Id;
    private AlarmManager AM;
    private PendingIntent ServicePending;



    public friend_adapter3(Context context,ArrayList<friend_model3> data){
        mContext=context;
        friend=data;
        mLayoutInflater=LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return friend.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public friend_model3 getItem(int position) {
        return friend.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mLayoutInflater.inflate(R.layout.together_friend_item,null);

        System.out.println("확인 adapter");

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView nickname=(TextView)view.findViewById(R.id.nickname);
        TextView arrive=(TextView)view.findViewById(R.id.ticket_arrive);
        TextView depart=(TextView)view.findViewById(R.id.ticket_depart);
        TextView todo=(TextView)view.findViewById(R.id.ticket_todo);
        TextView date=(TextView)view.findViewById(R.id.ticket_date);


        String file_name;


        //프로필 사진 가져오기
        file_name=friend.get(position).getUID();


        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference ref=storage.getReference();

        String filename=file_name+"_ProfileImage";     //파일명
        Uri file =selectedImage;
        StorageReference ref2=ref.child("ProfileImage/"+filename);


        try{

            File file2=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/ProfileImage");
            if(!file2.isDirectory()){
                file2.mkdir();
            }

            StorageReference storageRef=storage.getReference();
            storageRef.child("ProfileImage/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).circleCrop().into(image);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    image.setImageResource(R.drawable.profile);

                }
            });}catch (Exception e){
            image.setImageResource(R.drawable.profile);
        }


        //닉네임

        nickname.setText(friend.get(position).getNickname());
        arrive.setText(friend.get(position).getArrive());
        depart.setText(friend.get(position).getDepart());
        todo.setText(friend.get(position).getTodo());
        date.setText((CharSequence) friend.get(position).getDate());

        ImageButton accept =(ImageButton)view.findViewById(R.id.accept);
        ImageButton deny =(ImageButton)view.findViewById(R.id.deny);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        //count
        mDatabase.child("users").child(UID).child("together").addValueEventListener(new ValueEventListener() {
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
        //count2
        mDatabase.child("users").child(friend.get(position).getUID()).child("friend").child("accept").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        count2++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });




        accept.setOnClickListener(new View.OnClickListener(){ //수락버튼 클릭
            @Override
            public void onClick(View v) {
                //TODO: ACCEPT에 친구 UID 저장 list 삭제
                System.out.println("수락버튼클릭");
                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
                if(user != null){
                    UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
                }

                System.out.println(UID);
                System.out.println("UID!!"+friend.get(position).getUID());
                System.out.println("DATE"+friend.get(position).getDate());
                System.out.println("카운트"+String.valueOf(friend.get(position).getCount()));
                String Depart=friend.get(position).getDepart();

                String[] depart_arr = Depart.split(":");
                System.out.println("출발"+friend.get(position).getDepart());
                System.out.println("도착"+friend.get(position).getArrive());
                System.out.println(friend.get(position).getTodo());
                System.out.println("아이디"+Id);

                TicketDatabaseHandler ticketDatabaseHandler=new TicketDatabaseHandler(mDatabase);
                mDatabase.child("TICKET").child(UID).child(friend.get(position).getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot fileSnapshot : snapshot.getChildren()){
                            /*HashMap TicketMap = (HashMap<String, Object>) fileSnapshot.getValue();*/
                            Id = Integer.parseInt(fileSnapshot.getKey());
                            System.out.println(Id);

                        }
                        Ticket ticket = new Ticket(friend.get(position).getDepart(),friend.get(position).getArrive(),friend.get(position).getTodo(),Id+1,0,0);
                        System.out.println(UID);
                        System.out.println(friend.get(position).getDate());
                        System.out.println(ticket);

                        ticketDatabaseHandler.insert_ticketDB(UID,friend.get(position).getDate(),ticket);
                        SetAlarmManager(friend.get(position).getDate(),Integer.valueOf(depart_arr[0]),Integer.valueOf(depart_arr[1]),0);

                        mDatabase.child("users").child(UID).child("together").child(friend.get(position).getUID()).child(friend.get(position).getDate()).child(String.valueOf(friend.get(position).getCount())).setValue(null);     //together에서 친구삭제
                        friend.remove(position);
                        notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                //db에서 삭제


            }
        });
        deny.setOnClickListener(new View.OnClickListener() { //거절버튼 클릭시
            @Override
            public void onClick(View v) {
                System.out.println("거절버튼클릭");
                mDatabase.child("users").child(UID).child("together").child(friend.get(position).getUID()).child(friend.get(position).getDate()).child(String.valueOf(friend.get(position).getCount())).setValue(null);     //together에서 친구삭제
                Toast.makeText(mContext,"삭제되었습니다.", Toast.LENGTH_LONG).show();

                friend.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void SetAlarmManager(String ticket_Date, int dpth, int dptm, int requestcode) {
        AM = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

//        String date_time= YEAR+"-"+(MONTH+1)+"-"+DAY+" "+ ticket_dpt+":"+00;
//        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//        Date datetime = null;
//        try {
//            datetime = new Date(dateFormat.parse(date_time).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Calendar cal = Calendar.getInstance();
        cal.clear();

        String[] date_time = ticket_Date.split("-");
        int Year = Integer.parseInt(date_time[0]);
        int Month = Integer.parseInt(date_time[1]);
        int Day = Integer.parseInt(date_time[2]);


        cal.set(Year, Month - 1, Day, dpth, dptm);
//        cal.setTime(datetime);
        //Receiver로 보내기 위한 인텐트
        Intent intent_alarm = new Intent(mContext, AlarmReceiver.class);

        ServicePending = PendingIntent.getBroadcast(mContext, requestcode, intent_alarm, PendingIntent.FLAG_ONE_SHOT);

        long calc_time = cal.getTimeInMillis();

        if (Build.VERSION.SDK_INT < 23) {
            // 19 이상
            if (Build.VERSION.SDK_INT >= 19) {
                AM.setExact(AlarmManager.RTC_WAKEUP, calc_time , ServicePending);
            }
            // 19 미만
            else {
                // pass
                // 알람셋팅
                AM.set(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
            }
            // 23 이상
        } else {
            AM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
