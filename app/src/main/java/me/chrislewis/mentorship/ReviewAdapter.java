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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.User;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context mContext;
    private List<ParseUser> reviewers;
    SharedViewModel model;

    public ReviewAdapter(List<ParseUser> reviewers, SharedViewModel model) {
        this.reviewers = reviewers;
        this.model = model;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_review, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ParseUser reviewer = reviewers.get(i);
        reviewer.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                try {
                    ParseUser temp = reviewer.fetchIfNeeded();
                    User mReviewer = new User(temp);
                    viewHolder.tvReviewWriter.setText(mReviewer.getName());

                    if(mReviewer.getProfileImage() != null) {
                        Glide.with(mContext)
                                .load(mReviewer.getProfileImage().getUrl())
                                .into(viewHolder.ivReviewWriter);
                    }

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if (reviewers == null) {
            return 0;
        } else {
            return reviewers.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewWriter;
        TextView tvReviewWriter;
        TextView tvReviewBody;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReviewWriter = itemView.findViewById(R.id.ivReviewWriter);
            tvReviewWriter = itemView.findViewById(R.id.tvReviewWriter);
            tvReviewBody = itemView.findViewById(R.id.tvReviewBody);
            //itemView.setOnClickListener(this);
        }
    }
}
