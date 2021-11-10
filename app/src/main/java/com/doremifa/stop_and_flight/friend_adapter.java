package com.doremifa.stop_and_flight;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class friend_adapter extends BaseAdapter implements View.OnClickListener {
    Context mContext=null;
    LayoutInflater mLayoutInflater=null;
    ArrayList<friend_model> friend;
    String picturePath;
    Uri selectedImage;
    Fragment_friend1 friend1;
    private DatabaseReference mDatabase;
    private String UID;
    int count=0;
    int count2=0;
    public friend_adapter(Context context,ArrayList<friend_model> data){
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
    public friend_model getItem(int position) {
        return friend.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mLayoutInflater.inflate(R.layout.friend_item1,null);

        System.out.println("확인 adapter");

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView nickname=(TextView)view.findViewById(R.id.nickname);
        TextView message=(TextView)view.findViewById(R.id.message);
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
            System.out.println("로그 1번");

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
                    System.out.println("프로필 나오지 않음1");

                }
            });}catch (Exception e){
            image.setImageResource(R.drawable.profile);
            System.out.println("프로필 나오지 않음2");
        }


            //닉네임

            nickname.setText(friend.get(position).getNickname());
            message.setText(friend.get(position).getMessage());

        ImageButton accept =(ImageButton)view.findViewById(R.id.accept);
        ImageButton deny =(ImageButton)view.findViewById(R.id.deny);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        //count
        mDatabase.child("users").child(UID).child("friend").child("accept").addValueEventListener(new ValueEventListener() {
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




        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: ACCEPT에 친구 UID 저장 list 삭제
                System.out.println("확인 어댑터에서 count/"+friend.get(position).getUID()+friend.get(position).getCount_myidle()+friend.get(position).getCount_idle());
               mDatabase.child("users").child(UID).child("friend").child("accept").child(String.valueOf(count)).setValue(friend.get(position).getUID());     //내db에서 친구 accept에 등록
               mDatabase.child("users").child(UID).child("friend").child("idle").child(friend.get(position).getCount_idle()).setValue(null);              //내 db에서 친구 대기목록에서 삭제
               mDatabase.child("users").child(friend.get(position).getUID()).child("friend").child("myidle").child(friend.get(position).getCount_myidle()).setValue(null);     //친구 db에서 대기삭제 등록
                mDatabase.child("users").child(friend.get(position).getUID()).child("friend").child("accept").child(String.valueOf(count2)).setValue(UID);        //친구 db에서 accept에 나를 등록
                friend.remove(position);
                notifyDataSetChanged();
            }
        });
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(UID).child("friend").child("myidle").child(friend.get(position).getCount_myidle()).removeValue();              //내 db에서 친구 대기목록에서 삭제
                mDatabase.child("users").child(friend.get(position).getUID()).child("friend").child("idle").child(friend.get(position).getCount_idle()).removeValue();     //친구 db에서 대기삭제 등록
                friend.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
