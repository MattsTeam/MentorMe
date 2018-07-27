package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.List;

import me.chrislewis.mentorship.models.Message;
import me.chrislewis.mentorship.models.User;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private User mUser;

    public MessageAdapter(Context context, ParseUser user, List<Message> messages) {
        mContext = context;
        this.mUser = new User(user);
        mMessages = messages;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_message, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = mMessages.get(i);
        User user = new User(message.getUser());
        final boolean isMe =
                user.getObjectId() != null &&
                user.getObjectId().equals(mUser.getObjectId());

        if (isMe) {
            viewHolder.imageMe.setVisibility(View.VISIBLE);
            viewHolder.imageOther.setVisibility(View.GONE);
            viewHolder.body.setGravity(Gravity.END);

            if (mUser.getProfileImage() != null ) {
                Glide.with(mContext)
                        .load(mUser.getProfileImage().getUrl())
                        .apply(new RequestOptions().circleCrop())
                        .into(viewHolder.imageMe);
            }
        }
        else {
            viewHolder.imageMe.setVisibility(View.GONE);
            viewHolder.imageOther.setVisibility(View.VISIBLE);
            viewHolder.body.setGravity(Gravity.START);

            Glide.with(mContext)
                    .load(user.getProfileImage().getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(viewHolder.imageOther);
        }
        viewHolder.body.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOther = itemView.findViewById(R.id.ivProfileOther);
            imageMe = itemView.findViewById(R.id.ivProfileMe);
            body = itemView.findViewById(R.id.tvBody);
        }
    }
}
