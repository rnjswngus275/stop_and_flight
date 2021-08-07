package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.app.Activity.RESULT_OK;

public class MypageFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private static final int REQUEST_CODE = 0;
    private static final int RESULT_CANCELED = 1;
    ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private Button buttonDeleteID;
    private Button buttonAllowedApps;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String UID=user.getUid();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String picturePath;
    Uri selectedImage;
    int point;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView=(ImageView)view.findViewById(R.id.imageView4);

        String filename=UID+"_ProfileImage";
        getImage(filename);

        TextView Nickname=(TextView)view.findViewById(R.id.textView7);
        TextView Point=(TextView)view.findViewById(R.id.textView11);

        final DatabaseReference ref = mDatabase.child("users").child(UID).child("nickname");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nickname.setText(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        final DatabaseReference ref2 = mDatabase.child("users").child(UID).child("point");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                point= snapshot.getValue(int.class);

                Point.setText(Integer.toString(point)+"점");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageButton Nickname_modify=(ImageButton)view.findViewById(R.id.imageButton);
        Nickname_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(FragmentNicknameModify.newInstance(null, null));
            }
        });

        Button license=(Button)view.findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(FragmentLicense.newInstance(null, null));
            }
        });

        Button picture=(Button)view.findViewById(R.id.button3);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"클릭?",Toast.LENGTH_SHORT).show();


                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},777);//세번째 파라매터 int requestcode 실행 후 전달 받을 코드 분기에 쓰이는것같음
                        Toast.makeText(getContext(),"else문 안입니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_CODE);

                String filename=UID+"_ProfileImage";
                getImage(filename);



            }
        });
        Button Logout=(Button)view.findViewById(R.id.LOGOUT);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Toast.makeText(getContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),AppGuide.class);
                startActivity(intent);
            }
        });

        buttonDeleteID = (Button) view.findViewById(R.id.DeleteID);
        buttonDeleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               firebaseAuth.getCurrentUser().delete();
                Toast.makeText(getContext(), "회원탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),AppGuide.class);
                startActivity(intent);

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

        buttonAllowedApps = (Button) view.findViewById(R.id.button8);
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

        Button ask=(Button)view.findViewById(R.id.ASK);
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
                                           String permissions[], int[] grantResults) {
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
                Glide.with(getContext()).load(uri).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void signOut(){
        // Firebase sign out
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
        mAuth.signOut();

    }

}