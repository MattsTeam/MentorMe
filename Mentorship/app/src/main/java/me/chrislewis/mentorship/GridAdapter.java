package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<User> mUsers;
    Context context;


    public GridAdapter(List<User> users) {
        mUsers = users;
    }


    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext(); // context is UserFragment
        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(gridView);
        return new ViewHolder(gridView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.tvUsername.setText(user.getUsername());
        Glide.with(context).load(user.getProfileImage().getUrl()).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        }
    }


    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<User> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }


}
