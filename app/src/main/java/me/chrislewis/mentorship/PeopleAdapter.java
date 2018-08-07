package me.chrislewis.mentorship;

import android.content.Context;
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
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> implements Filterable{

    Context context;
    private List<Chat> chats;
    User currentUser;
    SharedViewModel model;
    private MessageFragment messageFragment = new MessageFragment();
    ArrayList<Chat> arrayChats, filterList;
    MessageListFilter filter;


    PeopleAdapter(Context context, List<Chat> chats, SharedViewModel model) {
        this.context = context;
        this.chats = chats;
        this.model = model;
        this.currentUser = model.getCurrentUser();
        this.arrayChats = (ArrayList<Chat>) chats;
        this.filterList = (ArrayList<Chat>) chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_people, viewGroup, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Chat chat = chats.get(i);
        ArrayList<User> users = chat.getUsers();
        for(User user : users){
            if (!currentUser.getObjectId().equals(user.getObjectId())) {
                try {
                    user.fetchIfNeed();
                    viewHolder.tvName.setText(user.getName());
                    viewHolder.tvName.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(user.getProfileImage().getUrl())
                            .apply(new RequestOptions().circleCrop())
                            .into(viewHolder.ivProfileImage);
                    viewHolder.ivProfileImage.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (chats == null) {
            return 0;
        } else {
            return chats.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivProfileImage;
        TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Chat chat = chats.get(position);
                model.setRecipients(chat.getUsers());

                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
            }
        }
    }

    public void clear() {
        chats.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Chat> list) {
        chats.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new MessageListFilter(filterList, this);
        }
        return filter;
    }
}
