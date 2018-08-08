package me.chrislewis.mentorship;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements Filterable {

    private List<ParseUser> mUsers;
    ArrayList<ParseUser> users, filterList;
    SearchFilter filter;
    private Context context;
    private User currentUser;
    private Location currentLocation = new Location("currentLocation");

    SharedViewModel model;
    DetailsFragment detailsFragment = new DetailsFragment();
    DetailsFragment2 detailsFragment2 = new DetailsFragment2();

    public GridAdapter(ArrayList<ParseUser> users, SharedViewModel model) {
        mUsers = users;
        this.users = users;
        this.model = model;
        this.filterList = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ParseUser.getCurrentUser().fetchInBackground();
        currentUser = new User(ParseUser.getCurrentUser());
        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(gridView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        User user = new User(mUsers.get(position));
        String name = user.getName();
        if (name != null) {
            holder.tvName.setText(name);
        }
        if(user.getProfileImage() != null) {
            Glide.with(context).load(user.getProfileImage().getUrl()).into(holder.ivProfileImage);
        }

        Double rating = user.getOverallRating();
        if (rating != null) {
            holder.tvRating.setText("Rating: " + String.format("%.2f", rating));
        } else {
            holder.tvRating.setText("Rating: N/A");
        }

        Double relDistance = user.getRelDistance();
        if (relDistance != null) {
            holder.tvDistance.setText(Double.toString(relDistance) + " mi");
        }

        if (user.getCategories() != null) {
            for(int i = 0; i < user.getCategories().size(); i++) {
                if(user.getCategories().get(i).equals("Art")) {
                    holder.artIcon.setImageResource(R.drawable.ic_art_pressed);
                }
                else if(user.getCategories().get(i).equals("Engineering")) {
                    holder.engineeringIcon.setImageResource(R.drawable.ic_engineering_pressed);
                }
                else if(user.getCategories().get(i).equals("Sports")) {
                    holder.sportsIcon.setImageResource(R.drawable.ic_sport_pressed);
                }
                else if(user.getCategories().get(i).equals("Sciences")) {
                    holder.scienceIcon.setImageResource(R.drawable.ic_sciences_pressed);
                }
                else if(user.getCategories().get(i).equals("Languages")) {
                    holder.languagesIcon.setImageResource(R.drawable.ic_languages_pressed);
                }
                else if(user.getCategories().get(i).equals("Humanities")) {
                    holder.humanitiesIcon.setImageResource(R.drawable.ic_humanities_pressed);
                }
            }
        }
        user.saveInBackground();
        currentUser.saveInBackground();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvDistance;
        public TextView tvRating;
        public ImageView artIcon;
        public ImageView engineeringIcon;
        public ImageView sportsIcon;
        public ImageView scienceIcon;
        public ImageView languagesIcon;
        public ImageView humanitiesIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            artIcon = (ImageView) itemView.findViewById(R.id.artIcon);
            engineeringIcon = (ImageView) itemView.findViewById(R.id.engineeringIcon);
            sportsIcon= (ImageView) itemView.findViewById(R.id.sportsIcon);
            scienceIcon = (ImageView) itemView.findViewById(R.id.scienceIcon);
            languagesIcon = (ImageView) itemView.findViewById(R.id.languagesIcon);
            humanitiesIcon = (ImageView) itemView.findViewById(R.id.humanitiesIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseUser user = mUsers.get(position);
                model.setUser(new User(user));

                FragmentTransaction fragmentTransaction = model.getFragmentTransaction();
                fragmentTransaction = model.getFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
                fragmentTransaction.replace(R.id.flContainer, detailsFragment2).commit();
            }
        }
    }

    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ParseUser> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SearchFilter(filterList, this);
        }
        return filter;
    }


}