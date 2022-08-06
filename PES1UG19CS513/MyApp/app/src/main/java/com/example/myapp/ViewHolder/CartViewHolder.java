package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Interface.ItemClickListner;
import com.example.myapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(View itemView)
    {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
    }


    @Override
    public void onClick(View v) {
    itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(){
        this.itemClickListner=itemClickListner;
}
}
