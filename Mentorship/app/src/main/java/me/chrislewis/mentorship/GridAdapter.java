/*
package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{
    private List<User> mUsers;
    Context context;

    public GridAdapter(List<User> mentors) {
        mUsers = mentors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(gridView);
        return viewHolder;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        super(itemView);

        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);



    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}


*/