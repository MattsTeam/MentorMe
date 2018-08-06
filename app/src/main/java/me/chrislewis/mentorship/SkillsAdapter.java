package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    List<String> skills;

    public SkillsAdapter(List<String> skills) {
        this.skills = skills;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView = inflater.inflate(R.layout.item_skill, parent, false);
        return new ViewHolder(gridView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d("SkillsAdapter", Integer.toString(i) + ": " + skills.get(i));
        viewHolder.skillsBubble.setText(skills.get(i));
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView skillsBubble;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            skillsBubble = itemView.findViewById(R.id.skillBubble);
        }
    }
}
