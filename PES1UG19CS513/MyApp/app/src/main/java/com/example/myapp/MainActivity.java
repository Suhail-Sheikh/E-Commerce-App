package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp.Model.Users;
import com.example.myapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinNowButton=(Button)findViewById(R.id.main_join_now_button);
        loginButton=(Button)findViewById(R.id.main_login_button);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent =new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPasswordKey!="" && UserPhoneKey!="")     // if user has already logged in
            //Paper library remembers the user as the details are  stored in the memory
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) )
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);      //If yes , then AllowAccess();
                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait......");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();//get Reference from DB


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())  //check if number exists
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                        //get current users password
                    if (usersData.getPhone().equals(phone))
                    {
                        //check for password

                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"Already Logged in",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUser=usersData;
                            startActivity(intent);

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                // This number doesn't exist
                else
                {
                    Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}



