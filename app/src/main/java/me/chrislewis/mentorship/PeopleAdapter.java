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
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    Context context;
    List<ParseUser> users;
    User user;

    public PeopleAdapter(Context context, List<ParseUser> users, User mUser) {
        this.context = context;
        this.users = users;
        this.user = user;
    }

    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_people, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder viewHolder, int i) {
        User person = new User(users.get(i));

        viewHolder.tvName.setText(person.getName());
        Glide.with(context)
                .load(user.getProfileImage().getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(viewHolder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
