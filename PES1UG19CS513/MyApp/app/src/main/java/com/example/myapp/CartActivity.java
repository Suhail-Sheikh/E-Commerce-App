package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Model.Cart;
import com.example.myapp.Prevalent.Prevalent;
import com.example.myapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private Button NextProcessButton;
private TextView txtTotalAmount;
private int overTotalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView=(RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessButton=(Button)findViewById(R.id.next_process_btn);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);

        NextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               txtTotalAmount.setText("Total Price: "+String.valueOf(overTotalPrice)+"$");
                Toast.makeText(CartActivity.this,"Total Price "+overTotalPrice+"$",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart>options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                cartViewHolder.txtProductQuantity.setText("Quantity: "+cart.getQuantity());
                cartViewHolder.txtProductName.setText(cart.getPname());
                cartViewHolder.txtProductPrice.setText("Price: "+cart.getPrice());

                int oneProductPrice=((Integer.valueOf(cart.getPrice())))*Integer.valueOf(cart.getQuantity());
                overTotalPrice+=oneProductPrice;

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]={"Edit","Delete"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                    finish();
                                }
                                if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful())
                                               {
                                                   Toast.makeText(CartActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                                   Intent intent=new Intent(CartActivity.this,ShopActivity.class);
                                                   startActivity(intent);
                                                   finish();
                                               }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;

            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}