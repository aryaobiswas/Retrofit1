package com.example.retro2fit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    MainActivity mainActivity;
    List<userModel> allUserList;

    public UserAdapter(MainActivity mainActivity, List<userModel> allUserList) {
        this.mainActivity = mainActivity;
        this.allUserList = allUserList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.itemTxt.setText(allUserList.get(position).getTitle());
        String imageUrl = allUserList.get(position).getThumbnailUrl();
        loadImage(imageUrl, holder.itemImg);
    }

    private void loadImage(String imageUrl, ImageView imageView) {
        Glide.with(mainActivity)
                .load(imageUrl)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return allUserList.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        TextView itemTxt;
        ImageView itemImg;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            itemTxt = itemView.findViewById(R.id.itemTxt);
            itemImg = itemView.findViewById(R.id.itemImg);

        }
    }
}