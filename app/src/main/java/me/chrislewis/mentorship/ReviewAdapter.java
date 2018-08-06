package me.chrislewis.mentorship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context mContext;
    private List<Review> reviews;
    SharedViewModel model;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View reviewsView = inflater.inflate(R.layout.item_review, viewGroup, false);

        return new ViewHolder(reviewsView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Review review = reviews.get(i);

        ParseUser writer = review.getWriter();
        try {
            ParseUser tmp = writer.fetchIfNeeded();
            User mWriter = new User(tmp);
            viewHolder.tvReviewWriter.setText(mWriter.getName());

            viewHolder.rbReview.setRating(review.getRating());
            LayerDrawable stars = (LayerDrawable) viewHolder.rbReview.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255, 128, 0), PorterDuff.Mode.SRC_ATOP);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));
            if(mWriter.getProfileImage() != null) {
                Glide.with(mContext)
                        .load(mWriter.getProfileImage().getUrl())
                        .apply(new RequestOptions().circleCrop())
                        .into(viewHolder.ivReviewWriter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (review.getBody() != null) {
            viewHolder.tvReviewBody.setText(review.getBody());
        }
    }

    @Override
    public int getItemCount() {
        if (reviews == null) {
            return 0;
        } else {
            return reviews.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewWriter;
        TextView tvReviewWriter;
        TextView tvReviewBody;
        RatingBar rbReview;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReviewWriter = itemView.findViewById(R.id.ivReviewWriter);
            tvReviewWriter = itemView.findViewById(R.id.tvReviewWriter);
            tvReviewBody = itemView.findViewById(R.id.tvReviewBody);
            rbReview = itemView.findViewById(R.id.rbReview);
        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Review> list) {
        reviews.addAll(list);
        notifyDataSetChanged();
    }
}
