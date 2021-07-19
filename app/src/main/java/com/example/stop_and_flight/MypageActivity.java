package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stop_and_flight.model.AppInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonDeleteID;
    private Button buttonAllowedApps;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonDeleteID = (Button) findViewById(R.id.DeleteID);
        buttonDeleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MypageActivity.this, "계정이 정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MypageActivity.this, "다시 시도 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        List<PackageInfo> packlist = new List<PackageInfo>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<PackageInfo> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(PackageInfo packageInfo) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public PackageInfo get(int index) {
                return null;
            }

            @Override
            public PackageInfo set(int index, PackageInfo element) {
                return null;
            }

            @Override
            public void add(int index, PackageInfo element) {

            }

            @Override
            public PackageInfo remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<PackageInfo> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<PackageInfo> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<PackageInfo> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        buttonAllowedApps = (Button) findViewById(R.id.button8);
        buttonAllowedApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ArrayList<AppInfo> applist = new ArrayList<>();

                    installedApplist(applist);

                    Dialog dialog = new Dialog(MypageActivity.this);
                    dialog.setContentView(R.layout.app_dialog_select_sipinner);
                    dialog.getWindow().setLayout(1000, 1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    EditText editText = dialog.findViewById(R.id.app_select_edit_text);
                    ListView listView = dialog.findViewById(R.id.app_select_list_view);
                    ArrayAdapter adapter = new ArrayAdapter<>(MypageActivity.this, android.R.layout.simple_list_item_multiple_choice, applist);
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
                List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
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


    }
}