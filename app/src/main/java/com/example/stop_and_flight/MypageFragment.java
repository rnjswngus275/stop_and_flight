package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stop_and_flight.model.AppInfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MypageFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private static final int REQUEST_CODE = 0;
    private static final int RESULT_CANCELED = 1;
    ImageView imageView;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private Button buttonDeleteID;
    private Button buttonAllowedApps;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String UID;
    private DatabaseReference mDatabase;
    private HashMap<String, Object> UserMap;
    private SwipeRefreshLayout swipeRefreshLayout;
    String picturePath;
    Uri selectedImage;
    String nickname;
//    int point;

    private static final String TAG = MypageFragment.class.getSimpleName();
    public ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        imageView= view.findViewById(R.id.imageView4);

        TextView title_toolbar= (TextView)getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText("MY PAGE");

        String filename = UID + "_ProfileImage";
        getImage(filename);

        TextView Nickname= view.findViewById(R.id.textView7);
        TextView message2=view.findViewById(R.id.textView9);

        swipeRefreshLayout=view.findViewById(R.id.mypage);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatabase.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null)
                        {
                            UserMap = (HashMap<String, Object>) snapshot.getValue();

                            Nickname.setText(String.valueOf(UserMap.get("nickname")));

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                mDatabase.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null)
                        {
                            UserMap = (HashMap<String, Object>) snapshot.getValue();
                            message2.setText(String.valueOf(UserMap.get("message")));

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mDatabase.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null)
                {
                    UserMap = (HashMap<String, Object>) snapshot.getValue();

                    Nickname.setText(String.valueOf(UserMap.get("nickname")));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        mDatabase.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null)
                {
                    UserMap = (HashMap<String, Object>) snapshot.getValue();
                    message2.setText(String.valueOf(UserMap.get("message")));

                }
            }
            
           @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton Nickname_modify= view.findViewById(R.id.imageButton);
        Nickname_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et2 = new EditText(getContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setTitle("닉네임 변경")
                        .setMessage("변경할 닉네임을 입력하세요")
                        .setCancelable(false)
                        .setView(et2)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String value = et2.getText().toString();
                                FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
                                mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
                                mDatabase.child("users").child(UID).child("nickname").setValue(value);

                            }

                        });

                AlertDialog alert = alt_bld.create();

                alert.show();
            }
        });

        ImageButton message=view.findViewById(R.id.imageButton2);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et = new EditText(getContext());

                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());

                alt_bld.setTitle("상태메시지 변경")

                        .setMessage("변경할 상태메시지를 입력하세요")

                        .setCancelable(false)

                        .setView(et)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String value = et.getText().toString();

                                FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
                                mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
                                mDatabase.child("users").child(UID).child("message").setValue(value);

                            }

                        });

                AlertDialog alert = alt_bld.create();

                alert.show();

            }
        });

        Button license= view.findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(FragmentLicense.newInstance(null, null));
            }
        });

        Button picture= view.findViewById(R.id.button3);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"클릭?",Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},777);//세번째 파라매터 int requestcode 실행 후 전달 받을 코드 분기에 쓰이는것같음
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

                String filename=UID+"_ProfileImage";
                getImage(filename);
            }
        });
        Button Logout= view.findViewById(R.id.LOGOUT);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLogout(getContext());
                Toast.makeText(getContext(),"로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), AppGuideActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteID = view.findViewById(R.id.DeleteID);
        buttonDeleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountResign(getContext());
//                Toast.makeText(getContext(), "회원탈퇴되었습니다.", Toast.LENGTH_SHORT).show();

//                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), "계정이 정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(getContext(), "다시 시도 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });

        buttonAllowedApps = view.findViewById(R.id.button8);
        buttonAllowedApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AppInfo> applist = new ArrayList<>();

                installedApplist(applist);

                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.app_dialog_select_sipinner);
                dialog.getWindow().setLayout(1000, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText editText = dialog.findViewById(R.id.app_select_edit_text);
                ListView listView = dialog.findViewById(R.id.app_select_list_view);
                ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, applist);
                listView.setAdapter(adapter);
                dialog.show();

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                Button savebutton = dialog.findViewById(R.id.save_button);
                savebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAppDB(user.getUid());
                        SparseBooleanArray checkeditems = listView.getCheckedItemPositions();
                        int count = adapter.getCount();

                        for(int i = count - 1; i >= 0; i--){
                            if (checkeditems.get(i))
                            {
                                System.out.println(applist.get(i).getName());
                                insertAppDB(user.getUid(), i, applist.get(i).getName());
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }

            private void insertAppDB(String uid, int id, String appname) {
                mDatabase.child("APP").child(uid).child(Integer.toString(id)).setValue(appname);
            }

            public void deleteAppDB(String uid)
            {
                mDatabase.child("APP").child(uid).removeValue();
            }

            private void installedApplist(List<AppInfo> applist) {
                List<PackageInfo> packList = getContext().getPackageManager().getInstalledPackages(0);
                PackageInfo packInfo = null;
                for (int i=0; i < packList.size(); i++)
                {
                    packInfo = packList.get(i);
                    if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                    {
                        AppInfo appinfo = new AppInfo();
                        appinfo.setName(packInfo.packageName);
                        applist.add(appinfo);
                    }
                }
            }
        });

        Button ask= view.findViewById(R.id.ASK);
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"5905kjh@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "[스탑앤플라이트] 문의 메일");
                email.putExtra(Intent.EXTRA_TEXT, "안녕하세요! 오류의 경우 스크린캡쳐와 함께 휴대폰 기종을 같이 보내주시면 감사하겠습니다!");
                startActivity(email);
            }
        });

        return view;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 777: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(),"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(),"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
    @Override public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (data != null && resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    ImageCD();
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor == null || cursor.getCount() < 1) {
                        return; // no cursor or no record. DO YOUR ERROR HANDLING
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    if (columnIndex < 0) // no column index
                        return; // DO YOUR ERROR HANDLING

                    // 선택한 파일 경로
                    picturePath = cursor.getString(columnIndex);
//                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                    imageView.setImageBitmap(bitmap);

                    cursor.close();
                } break;
        }
    }




    public void ImageCD(){
        //파일저장
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference ref=storage.getReference();

        String filename=UID+"_ProfileImage";     //파일명
        Uri file =selectedImage;
        StorageReference ref2=ref.child("ProfileImage/"+filename);
        UploadTask uploadTask =ref2.putFile(file);

        //기존 파일 삭제

        StorageReference ref3=ref.child("ProfileImage/"+filename); //삭제할 파일명
        ref3.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception exception){}
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"프로필 이미지가 변경되었습니다",Toast.LENGTH_SHORT).show();
            }
        });

    }
    // 프로필 이미지 가져오기
    public void getImage(String filename){
        File file=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/ProfileImage");
        if(!file.isDirectory()){
            file.mkdir();
        }
        downloadImg(filename);
    }
    //이미지 다운로드해서 이미지 뷰에 보여주기
    public void downloadImg(String filename){
        FirebaseStorage storage =FirebaseStorage.getInstance();
        StorageReference storageRef=storage.getReference();
        storageRef.child("ProfileImage/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).circleCrop().into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(R.drawable.profile);

            }
        });
    }
    public void signOut(){
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {

                mAuth.signOut();
                if (mGoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {


                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

//                                DebugLog.logD(TAG, "User Logged out");
//                                setResult(ResultCode.SIGN_OUT_SUCCESS);

                            } else {

//                                setResult(ResultCode.SIGN_OUT_FAIL);
                            }

                            hideProgressDialog();
/*
                            finish();
*/
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

//                DebugLog.logD(TAG, "Google API Client Connection Suspended");
//
//                setResult(ResultCode.SIGN_OUT_FAIL);
                hideProgressDialog();

//                finish();
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
//            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public static void accountLogout(Context context){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(context,GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        }
    }
    public void accountResign(final Context context){        //구글연동해제
        if(user!=null){
            //구글연동해제
            try{
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(context,AppGuideActivity.class);

                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(context, "회원탈퇴 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });//firebase인증해제
                GoogleSignIn.getClient(context,GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess(); //Google 계정해제

            }catch(Exception e){
                Toast.makeText(context, "회원탈퇴 실패 catch", Toast.LENGTH_LONG).show();

            }
        }
    }

}
