package me.chrislewis.mentorship;

import android.content.Context;
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

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<ParseUser> mUsers;
    Context context;
    private ParseUser currentUser;
    Location currentLocation = new Location("currentLocation");


    public GridAdapter(List<ParseUser> users) {
        mUsers = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext(); // context is UserFragment
        currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint ParseCurrentLocation = currentUser.getParseGeoPoint("location");
        currentLocation.setLatitude(ParseCurrentLocation.getLatitude());
        currentLocation.setLongitude(ParseCurrentLocation.getLongitude());

        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(gridView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        ParseUser user = mUsers.get(position);
        holder.tvUsername.setText(user.getString("username"));
        Glide.with(context).load(user.getParseFile("profileImage").getUrl()).into(holder.ivProfileImage);

        ParseGeoPoint ParseLocation = user.getParseGeoPoint("location");
        Location location = new Location("location");
        location.setLongitude(ParseLocation.getLongitude());
        location.setLatitude(ParseLocation.getLatitude());
        double distanceInMeters = currentLocation.distanceTo(location);
        double distanceInMiles = distanceInMeters * 0.000621371192;

        holder.tvDistance.setText(String.format("%.2f", distanceInMiles) + " miles away");
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
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
