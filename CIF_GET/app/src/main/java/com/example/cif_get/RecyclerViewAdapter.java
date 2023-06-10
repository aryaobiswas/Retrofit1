package com.example.cif_get;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    private ArrayList<ModelClass> Data;
    private ArrayList<ModelClass> cartItems;


    RecyclerViewAdapter(Context context, ArrayList<ModelClass> Data){
        this.context=context;
        this.Data=Data;
        cartItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_row, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass model = Data.get(position);
        holder.title.setText(model.title);
        holder.brand.setText(model.brand);
        holder.price.setText(model.price);
        Glide.with(context)
                .load(model.thumbnail)
                .into(holder.itemImg);
        holder.description.setText(model.description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("item", model);
                context.startActivity(intent);
            }
        });

        holder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ModelClass cart = Data.get(adapterPosition);
                    Intent intent = new Intent(context, CartActivity.class);
                    intent.putExtra("cart", cart);
                    context.startActivity(intent);

                }
            }
        });

//        Log.d("Aryarecycler", "onResponse: "+"CartWorking");
    }

    public ArrayList<ModelClass> getCartItems() {
        return cartItems;
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public void removeItem(int position) {
        Data.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, price, brand, description;
        ImageView itemImg;
        Button cartButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.text_title);
            brand = itemView.findViewById(R.id.text_brand);
            price = itemView.findViewById(R.id.text_price);
            description = itemView.findViewById(R.id.description);
            cartButton = itemView.findViewById(R.id.CartButton);

        }
    }

}
