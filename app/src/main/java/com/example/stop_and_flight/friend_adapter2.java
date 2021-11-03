package com.example.stop_and_flight;

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

public class friend_adapter2 extends BaseAdapter {

    Context mContext=null;
    LayoutInflater mLayoutInflater=null;
    ArrayList<friend_model2> friend;
    String picturePath;
    Uri selectedImage;

    FriendFragment friend2;
    private DatabaseReference mDatabase;
    private String UID;


    public friend_adapter2(Context context,ArrayList<friend_model2> data){
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
    public friend_model2 getItem(int position) {
        return friend.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mLayoutInflater.inflate(R.layout.friend_item2,null);

        System.out.println("확인 adapter2");

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView nickname=(TextView)view.findViewById(R.id.nickname);
        TextView message=(TextView)view.findViewById(R.id.message);
        String file_name;


        //프로필 사진 가져오기
        file_name=friend.get(position).getUID();


        FirebaseStorage storage=FirebaseStorage.getInstance();

        String filename=file_name+"_ProfileImage";     //파일명
        Uri file =selectedImage;



        try{
            System.out.println("로그 0번");

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

//        ImageButton delete =(ImageButton)view.findViewById(R.id.delete);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


         return view;
    }




}


