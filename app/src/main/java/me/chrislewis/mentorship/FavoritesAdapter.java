package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private Context mContext;
    private String favoriteKey;
    private List<ParseUser> favorites;

    SharedViewModel model;
    DetailsFragment detailsFragment = new DetailsFragment();

    public FavoritesAdapter(List<ParseUser> favorites, SharedViewModel model) {
        this.favorites = favorites;
        this.model = model;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_favorite, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ParseUser user = favorites.get(i);
        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                try {
                    ParseUser temp = user.fetchIfNeeded();
                    User mUser = new User(temp);
                    viewHolder.tvName.setText(mUser.getName());

                    if(mUser.getProfileImage() != null) {
                        Glide.with(mContext)
                                .load(mUser.getProfileImage().getUrl())
                                .into(viewHolder.ivProfile);
                    }

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if (favorites == null) {
            return 0;
        } else {
            return favorites.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfile;
        TextView tvName;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "adapter clicked", Toast.LENGTH_SHORT).show();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseUser user = favorites.get(position);
                model.setUser(new User(user));

                FragmentTransaction fragmentTransaction = model.getFragmentTransaction();
                fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
            }
        }
    }
}
