package me.chrislewis.mentorship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.mentorship.models.ParseEvent;

public class CalendarEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static int ITEM_TYPE_EVENT = 0;
    private static int ITEM_TYPE_GOOGLE_EVENT = 1;
    private List<Object> items = new ArrayList<>();

    public CalendarEventsAdapter(List<ParseEvent> events, List<com.google.api.services.calendar.model.Event> googleEvents) {
        this.items.addAll(events);
        this.items.addAll(googleEvents);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_EVENT) {
            View eventView = layoutInflater.inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(eventView);
        }
        else {
            View eventView = layoutInflater.inflate(R.layout.item_event, parent, false);
            return new GoogleEventViewHolder(eventView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Object item = items.get(position);
        if (viewHolder instanceof EventViewHolder) {
            ((EventViewHolder) viewHolder).bind((ParseEvent) item);
        }
        else {
            ((GoogleEventViewHolder) viewHolder).bind((com.google.api.services.calendar.model.Event) item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ParseEvent) {
            return ITEM_TYPE_EVENT;
        }
        else {
            return ITEM_TYPE_GOOGLE_EVENT;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvDescription;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(ParseEvent event) {
            tvTime.setText(event.getEventTime());
            tvDescription.setText(event.getEventDescription());
        }
    }

    private static class GoogleEventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvDescription;

        public GoogleEventViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public String dateTimeTime(String dateTime) {
            return dateTime.substring(11, 16);
        }

        public void bind(com.google.api.services.calendar.model.Event GoogleEvent) {
            String eventString = GoogleEvent.getStart().getDateTime().toString();
            tvTime.setText(dateTimeTime(eventString));
            tvDescription.setText(GoogleEvent.getSummary());
        }
    }
}

