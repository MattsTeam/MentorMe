package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    Context context;
    List<Chat> chats;
    User user;
    SharedViewModel model;
    MessageFragment messageFragment = new MessageFragment();


    public PeopleAdapter(Context context, ArrayList<Chat> chats, SharedViewModel model) {
        this.context = context;
        this.chats = chats;
        this.model = model;
        this.user = model.getCurrentUser();
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Chat chat = chats.get(i);
//        ArrayList<String> users = chat.getUsers();
//        final ArrayList<User> recipients = new ArrayList<>();
//        for(int j = 0; j < users.size(); j++){
//            String holder = users.get(j);
//
//            User.Query query = new User.Query();
//            query.getUser(holder);
//            query.findInBackground(new FindCallback<ParseUser>() {
//                @Override
//                public void done(List<ParseUser> objects, ParseException e) {
//                    User filler = new User(objects.get(0));
//                    if (user.getObjectId().equals(filler.getObjectId()) == false) {
//                        viewHolder.tvName.setText(filler.getName());
//                        viewHolder.tvName.setVisibility(View.VISIBLE);
//                        Glide.with(context)
//                                .load(filler.getProfileImage().getUrl())
//                                .into(viewHolder.ivProfileImage);
//                        viewHolder.ivProfileImage.setVisibility(View.VISIBLE);
//                    }
//                    recipients.add(filler);
//                }
//            });
//
//        }
//        chat.setRecipients(recipients);
//        chat.setUsers(recipients);

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

        public ViewHolder(@NonNull View itemView) {
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
                model.setRecipients(chat.getRecipients());

                FragmentTransaction fragmentTransaction = model.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, messageFragment).commit();
            }
        }
    }
}
