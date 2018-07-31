package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private Context mContext;
    private List<User> favorites;

    SharedViewModel model;
    private FragmentTransaction fragmentTransaction;
    private DetailsFragment detailsFragment = new DetailsFragment();
    private MessageFragment messageFragment = new MessageFragment();

    FavoritesAdapter(List<User> favorites, SharedViewModel model) {
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
        final User user = favorites.get(i);
        viewHolder.tvName.setText(user.getName());
        Glide.with(mContext)
                .load(user.getProfileImage().getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(viewHolder.ivProfile);
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
        Button btMessage;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            btMessage = itemView.findViewById(R.id.btMessage);
            btMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = favorites.get(position);

                        model.setRecipients(new ArrayList<>(Arrays.asList(user, model.getCurrentUser())));
                        fragmentTransaction = model.getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User user = favorites.get(position);
                model.setUser(user);

                fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
            }
        }
    }
}
