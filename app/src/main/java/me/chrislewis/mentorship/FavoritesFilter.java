package me.chrislewis.mentorship;

import android.widget.Filter;

import com.parse.ParseUser;

import java.util.ArrayList;

import me.chrislewis.mentorship.models.User;

public class FavoritesFilter extends Filter {
    FavoritesAdapter adapter;
    ArrayList<User> filterList;

    public FavoritesFilter(ArrayList<User> filterList, FavoritesAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();
            ArrayList<User> filteredUsers = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                if (filterList.get(i) != null) {
                    User u = filterList.get(i);
                    if(u.getName().toUpperCase().contains(constraint))
                    {
                        filteredUsers.add(filterList.get(i));
                    }
                }
            }
            results.count=filteredUsers.size();
            results.values=filteredUsers;
        } else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.arrayUsers = (ArrayList<User>) filterResults.values;
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.addAll(adapter.arrayUsers);
        adapter.notifyDataSetChanged();
    }
}
