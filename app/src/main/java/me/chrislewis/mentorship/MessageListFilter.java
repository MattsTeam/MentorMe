package me.chrislewis.mentorship;

import android.widget.Filter;

import java.util.ArrayList;

import me.chrislewis.mentorship.models.Chat;
import me.chrislewis.mentorship.models.User;

public class MessageListFilter extends Filter {
    PeopleAdapter adapter;
    ArrayList<Chat> filterList;

    public MessageListFilter(ArrayList<Chat> filterList, PeopleAdapter adapter)
    {
        this.adapter = adapter;
        this.filterList = filterList;

    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results=new Filter.FilterResults();

        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();
            ArrayList<Chat> filteredChats = new ArrayList<>();

            for (int i=0 ; i < filterList.size();i++)
            {
                Chat chat = filterList.get(i);
                if (chat != null) {
                    ArrayList<User> users = chat.getUsers();
                    for(User user : users){
                        if(user.getName().toUpperCase().contains(constraint))
                        {
                            filteredChats.add(filterList.get(i));
                        }
                    }
                }
            }
            results.count = filteredChats.size();
            results.values = filteredChats;
        } else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
        adapter.arrayChats = (ArrayList<Chat>) filterResults.values;
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.addAll(adapter.arrayChats);
        adapter.notifyDataSetChanged();
    }
}
