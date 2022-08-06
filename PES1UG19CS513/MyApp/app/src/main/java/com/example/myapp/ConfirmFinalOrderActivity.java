package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
private EditText nameEditText,phoneEditText,addressEditText;
private Button confirmOrder;
private String totalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        confirmOrder=(Button)findViewById(R.id.confirm_final__order_btn);
        nameEditText=(EditText)findViewById(R.id.shippment_name);
        phoneEditText=(EditText)findViewById(R.id.shippment_phone);
        addressEditText=(EditText)findViewById(R.id.shippment_adress);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }


        });

    }
    private void Check() {
    if(TextUtils.isEmpty(nameEditText.getText().toString()))
    {
        Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
    }
    else  if(TextUtils.isEmpty(phoneEditText.getText().toString()))
    {
        Toast.makeText(this, "Please provide your Phone number", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
        }
    else{
        ConfirmOrder();
        }
    }
private void ConfirmOrder(){
       final String saveCurrentDate,saveCurrentTime;
    Calendar calForDate=Calendar.getInstance();
    SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
    saveCurrentDate=currentDate.format(calForDate.getTime());

    SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
    saveCurrentTime=currentDate.format(calForDate.getTime());

    final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
            .child("Orders")
            .child(Prevalent.currentOnlineUser.getPhone());
    final HashMap<String,Object> ordersMap=new HashMap<>();
    ordersMap.put("totalAmount",totalAmount);
    ordersMap.put("name",nameEditText.getText().toString());
    ordersMap.put("phone",phoneEditText.getText().toString());
    ordersMap.put("date",saveCurrentDate);
    ordersMap.put("time",saveCurrentTime);
    ordersMap.put("address",addressEditText.getText().toString());
    ordersMap.put("state","Not Shipped");

    ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
       if(task.isSuccessful())
       {
           FirebaseDatabase.getInstance().getReference()
                   .child("Cart List")
                   .child("User View")
                   .child(Prevalent.currentOnlineUser.getPhone())
                   .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Order placed Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ConfirmFinalOrderActivity.this,ShopActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
       }
        }
    });
}

}

