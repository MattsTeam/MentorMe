package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    Context context;
    List<ParseUser> users;
    User user;
    SharedViewModel model;
    MessageFragment messageFragment = new MessageFragment();


    public PeopleAdapter(Context context, List<ParseUser> users, SharedViewModel model) {
        this.context = context;
        this.users = users;
        this.model = model;
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
    public void onBindViewHolder(@NonNull final PeopleAdapter.ViewHolder viewHolder, int i) {
        final User person = new User(users.get(i));
        try {
            person.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    viewHolder.tvName.setText(person.getName());
                    Glide.with(context)
                            .load(person.getProfileImage().getUrl())
                            .apply(new RequestOptions().circleCrop())
                            .into(viewHolder.ivProfileImage);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        } else {
            return users.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivProfileImage;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseUser user = users.get(position);
                model.setUser(new User(user));
                ((FragmentActivity) view.getContext()).getFragmentManager().beginTransaction()
                        .replace(R.id.flContainer, messageFragment)
                        .commit();
            }
        }
    }
}
