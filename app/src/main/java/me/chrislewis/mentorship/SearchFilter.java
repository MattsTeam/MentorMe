package me.chrislewis.mentorship;

import android.widget.Filter;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.User;

public class SearchFilter extends Filter {
    GridAdapter adapter;
    ArrayList<ParseUser> filterList;

    public SearchFilter(ArrayList<ParseUser> filterList, GridAdapter adapter)
    {
        this.adapter = adapter;
        this.filterList = filterList;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0)
        {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ParseUser> filteredUsers = new ArrayList<>();

            for (int i = 0 ;i < filterList.size(); i++)
            {
                if (filterList.get(i) != null) {
                    User u = new User(filterList.get(i));
                    if(u.getName().toUpperCase().contains(constraint))
                    {
                        filteredUsers.add(filterList.get(i));
                    }
                    List<String> skills = u.getSkillsList();
                    if (skills != null) {
                        for (String skill: skills) {
                            if (skill.toUpperCase().contains(constraint)) {
                                if (!filteredUsers.contains(filterList.get(i))) {
                                    filteredUsers.add(filterList.get(i));
                                }
                            }
                        }
                    }
                }
            }
            results.count=filteredUsers.size();
            results.values=filteredUsers;
        } else {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.users = (ArrayList<ParseUser>) filterResults.values;
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.addAll(adapter.users);
        adapter.notifyDataSetChanged();
    }
}
