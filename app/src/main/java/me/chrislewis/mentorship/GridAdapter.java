package me.chrislewis.mentorship;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<ParseUser> mUsers;
    private Context context;
    private User currentUser;
    private Location currentLocation = new Location("currentLocation");


    public GridAdapter(ArrayList<ParseUser> users) {
        mUsers = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ParseUser.getCurrentUser().fetchInBackground();
        currentUser = new User(ParseUser.getCurrentUser());
        ParseGeoPoint ParseCurrentLocation = currentUser.getCurrentLocation();
        currentLocation.setLatitude(ParseCurrentLocation.getLatitude());
        currentLocation.setLongitude(ParseCurrentLocation.getLongitude());

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
        String description = user.getDescription();
        if (description != null) {
            holder.tvDescription.setText(description);
        }

        if(user.getProfileImage() != null) {
            Glide.with(context).load(user.getProfileImage().getUrl()).into(holder.ivProfileImage);
        }

        ParseGeoPoint ParseLocation = user.getCurrentLocation();
        if (ParseLocation != null) {
            Location location = new Location("location");
            location.setLongitude(ParseLocation.getLongitude());
            location.setLatitude(ParseLocation.getLatitude());
            double distanceInMeters = currentLocation.distanceTo(location);
            double distanceInMiles = distanceInMeters * 0.000621371192;
            double distance = Math.round(distanceInMiles * 10) / 10;

            holder.tvDistance.setText(Double.toString(distance) + " miles away");
            user.setDistance(Double.toString(distance));
            user.setRelDistance(distance);
        }

        String categoriesText = user.getCategories().toString();
        if (categoriesText != null) {
            holder.tvCategories.setText(categoriesText);
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
        public TextView tvDescription;
        public TextView tvCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvCategories = (TextView) itemView.findViewById(R.id.tvCategories);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseUser user = mUsers.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("UserObjectId", user.getObjectId());
                view.getContext().startActivity(intent);
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


}