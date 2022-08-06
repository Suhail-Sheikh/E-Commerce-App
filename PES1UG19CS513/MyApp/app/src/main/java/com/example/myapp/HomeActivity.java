package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private Button LogoutButton,ShopNowButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LogoutButton=(Button)findViewById(R.id.home_logout);
        ShopNowButton=(Button)findViewById(R.id.home_shop_now);
    LogoutButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
        }
    });

    ShopNowButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(HomeActivity.this,ShopActivity.class);
            startActivity(intent);
        }
    });
    }


}
