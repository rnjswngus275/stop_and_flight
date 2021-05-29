package com.example.stop_and_flight;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);

        buttonJoin = (Button) findViewById(R.id.btn_join);


        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")&&!editTextName.getText().toString().equals("")) {
// 이메일과 비밀번호가 공백이 아닌 경우 + 닉네임
                    createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(),editTextName.getText().toString());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기


                    //닉네임 저장해주기 : 안되면 주현에게 문의
                    DatabaseReference mDatabase ;
                    String uid="";
                    if(user!=null){
                        uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기

                    }
                    else{
                        Toast.makeText(JoinActivity.this,"uid오류!",Toast.LENGTH_SHORT).show();
                    }
                    String Nickname =editTextName.getText().toString();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
                    mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
                    mDatabase.child("users").child(uid).child("nickname").setValue(Nickname);
                } else {
// 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 성공이 아닐때
                            Toast.makeText(JoinActivity.this, "회원가입 조건 충족 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

