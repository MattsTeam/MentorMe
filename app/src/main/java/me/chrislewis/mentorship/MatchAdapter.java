package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.chrislewis.mentorship.models.Match;
import me.chrislewis.mentorship.models.User;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    private Context mContext;
    private List<Match> matches;
    User currentUser;

    private boolean isMentor;

    SharedViewModel model;
    private FragmentTransaction fragmentTransaction;
    private DetailsFragment2 detailsFragment = new DetailsFragment2();
    private MessageFragment messageFragment = new MessageFragment();
    AddEventFragment addEventFragment = new AddEventFragment();

    MatchAdapter(List<Match> matches, SharedViewModel model, User thisUser) {
        this.matches = matches;
        this.model = model;
        currentUser = thisUser;
        isMentor = thisUser.getIsMentor();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup,int i){
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_match, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder ( @NonNull final ViewHolder viewHolder, int i){
        Match match = matches.get(i);
        User user = null;
        try {
            if (isMentor) {
                user = match.getMentee();
            } else {
                user = match.getMentor();
            }
            viewHolder.tvName.setText(user.getName());
            Double dist = user.getRelDistance();
            if (dist != null) {
                viewHolder.tvDistance.setText(dist.toString() + " miles away");
            }
            String description = user.getDescription();
            if (description != null) {
                viewHolder.tvDescription.setText(description);
            }

            Glide.with(mContext)
                    .load(user.getProfileImage().getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(viewHolder.ivProfile);
        } catch (Exception e) {
            currentUser.removeMatch(match);
        }

        if (user.getCategories() != null) {
            for(int j = 0; j < user.getCategories().size(); j++) {
                if(user.getCategories().get(j).equals("Art")) {
                    viewHolder.artIcon.setImageResource(R.drawable.ic_art_pressed);
                }
                else if(user.getCategories().get(j).equals("Engineering")) {
                    viewHolder.engineeringIcon.setImageResource(R.drawable.ic_engineering_pressed);
                }
                else if(user.getCategories().get(j).equals("Sports")) {
                    viewHolder.sportsIcon.setImageResource(R.drawable.ic_sport_pressed);
                }
                else if(user.getCategories().get(j).equals("Sciences")) {
                    viewHolder.scienceIcon.setImageResource(R.drawable.ic_sciences_pressed);
                }
                else if(user.getCategories().get(j).equals("Languages")) {
                    viewHolder.languagesIcon.setImageResource(R.drawable.ic_languages_pressed);
                }
                else if(user.getCategories().get(j).equals("Humanities")) {
                    viewHolder.humanitiesIcon.setImageResource(R.drawable.ic_humanities_pressed);
                }
            }
        }
    }

    @Override
    public int getItemCount () {
        if (matches == null) {
            return 0;
        } else {
            return matches.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfile;
        TextView tvName;
        TextView tvDistance;
        TextView tvDescription;
        ImageButton btMessage;
        ImageButton btEvent;

        public ImageView artIcon;
        public ImageView engineeringIcon;
        public ImageView sportsIcon;
        public ImageView scienceIcon;
        public ImageView languagesIcon;
        public ImageView humanitiesIcon;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistHome);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            artIcon = (ImageView) itemView.findViewById(R.id.artIcon);
            engineeringIcon = (ImageView) itemView.findViewById(R.id.engineeringIcon);
            sportsIcon= (ImageView) itemView.findViewById(R.id.sportsIcon);
            scienceIcon = (ImageView) itemView.findViewById(R.id.scienceIcon);
            languagesIcon = (ImageView) itemView.findViewById(R.id.languagesIcon);
            humanitiesIcon = (ImageView) itemView.findViewById(R.id.humanitiesIcon);

            btMessage = itemView.findViewById(R.id.btMessage);
            btMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = null;
                        if(isMentor) {
                            user = matches.get(position).getMentee();
                        } else {
                            user = matches.get(position).getMentor();
                        }
                        model.setRecipients(new ArrayList<>(Arrays.asList(user, model.getCurrentUser())));
                        fragmentTransaction = model.getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
                    }
                }
            });
            itemView.setOnClickListener(this);

            btEvent = itemView.findViewById(R.id.btEvent);
            btEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setCreateFromCalendar(false);
                    FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, addEventFragment).commit();
                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User user = null;
                if(isMentor) {
                    user = matches.get(position).getMentee();
                }
                else {
                    user = matches.get(position).getMentor();
                }
                model.setUser(user);
                model.setDetailsFromGrid(false);
                fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, detailsFragment).commit();
            }
        }
    }

    public void clear () {
        matches.clear();
        notifyDataSetChanged();
    }

    public void addAll (List < Match > list) {
        matches.addAll(list);
        notifyDataSetChanged();
    }
}
