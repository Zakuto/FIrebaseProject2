package com.example.aryantotaharuddin.firebaseproject;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView textDetails;
    private EditText iName,iMail;
    private Button buttonSave;
    private DatabaseReference dbReference;
    private FirebaseDatabase fDatabaseInstance;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        textDetails = (TextView)findViewById(R.id.txt_user);
        iName = (EditText) findViewById(R.id.name);
        iMail =(EditText) findViewById(R.id.email);
        buttonSave = (Button) findViewById(R.id.btn_save);

        fDatabaseInstance = FirebaseDatabase.getInstance();
        dbReference = fDatabaseInstance.getReference("users");

        fDatabaseInstance.getReference("app_title").setValue("TEST REALTIME DATABASE");
        fDatabaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"data Updated");
                Log.e("Testing","Data Tested");
                String appTitle = dataSnapshot.getValue(String.class);

                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"Fail to update title value.", databaseError.toException());
            }
        });

        //update user
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iName.getText().toString();
                String email = iMail.getText().toString();

                if(TextUtils.isEmpty(userId)){
                    createUser(name,email);
                }else{
                    updateUser(name,email);
                }
            }
        });

        toggleButton();
    }

    private void toggleButton(){
        if(TextUtils.isEmpty(userId)){
            buttonSave.setText("Save");
        }else{
            buttonSave.setText("Update");
        }
    }

    public void createUser(String name, String email){
        if(TextUtils.isEmpty(userId)){
            userId = dbReference.push().getKey();
        }

        User user = new User(name,email);

        dbReference.child(userId).setValue(user);

        addUserChangeListener();
    }

private void addUserChangeListener(){
    dbReference.child(userId).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);

            if(user==null){
                Log.e(TAG,"user is null!");
                return;
            }

            Log.e(TAG,"User data is changed... " + user.getName() + " , " +user.getEmail());

            textDetails.setText(user.getName() + " , "+user.getEmail());

            iName.setText("");
            iMail.setText("");

            toggleButton();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG,"Failed to read user",databaseError.toException());
        }


    });
}

public void updateUser(String name,String email){
    if(!TextUtils.isEmpty(name))
        dbReference.child(userId).child("name").setValue(name);
    if(!TextUtils.isEmpty(email))
        dbReference.child(userId).child("email").setValue(email);
}

public void deleteUser(String name,String email){

}
    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }
}
