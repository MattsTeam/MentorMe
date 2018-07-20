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

import java.util.List;

import me.chrislewis.mentorship.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public MessageAdapter(Context context, String userId, List<Message> messages) {
        mContext = context;
        this.mUserId = userId;
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
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if (isMe) {
            viewHolder.imageMe.setVisibility(View.VISIBLE);
            viewHolder.imageOther.setVisibility(View.GONE);
            viewHolder.body.setGravity(Gravity.END);
        }
        else {
            viewHolder.imageMe.setVisibility(View.GONE);
            viewHolder.imageOther.setVisibility(View.VISIBLE);
            viewHolder.body.setGravity(Gravity.START);
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
