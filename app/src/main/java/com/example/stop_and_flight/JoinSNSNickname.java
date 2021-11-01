 package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinSNSNickname extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_sns_nickname);

        EditText nickname=(EditText)findViewById(R.id.Nickname);

        Button button= (Button)findViewById(R.id.sns_signup_complete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase ;
                String uid="";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
                if(user!=null){
                    uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기

                }
                String Nickname =nickname.getText().toString();
                System.out.println("확인"+Nickname);
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
                mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
                mDatabase.child("users").child(uid).child("nickname").setValue(Nickname);

                //비상탈출 시간, point 초기화
                mDatabase.child("users").child(uid).child("emergency_time").setValue(0);
//                mDatabase.child("users").child(uid).child("point").setValue(0);
                mDatabase.child("users").child(uid).child("message").setValue("상태메시지");
                Intent intent = new Intent(JoinSNSNickname.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

}