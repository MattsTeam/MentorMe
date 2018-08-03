package me.chrislewis.mentorship.models;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.library.calendardayview.EventView;
import com.framgia.library.calendardayview.PopupView;
import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.data.IPopup;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;
import com.parse.ParseFile;

import java.util.List;

import me.chrislewis.mentorship.SharedViewModel;

public class CustomDecoration extends CdvDecorationDefault {

    Context context;
    SharedViewModel model;

    public CustomDecoration(Context context) {
        super(context);
        this.context = context;
        FragmentActivity activity = (FragmentActivity) context;
        model = ViewModelProviders.of(activity).get(SharedViewModel.class);
    }

    @Override
    public EventView getEventView(final IEvent event, Rect eventBound, int hourHeight,
                                  int seperateHeight) {
        final EventView eventView =
                super.getEventView(event, eventBound, hourHeight, seperateHeight);

        // hide event name
        TextView textEventName = (TextView) eventView.findViewById(com.framgia.library.calendardayview.R.id.item_event_name);
        textEventName.setVisibility(View.INVISIBLE);

        // hide event header
        TextView textHeader1 = (TextView) eventView.findViewById(com.framgia.library.calendardayview.R.id.item_event_header_text1);
        TextView textHeader2 = (TextView) eventView.findViewById(com.framgia.library.calendardayview.R.id.item_event_header_text2);

        textHeader1.setVisibility(View.GONE);
        textHeader2.setVisibility(View.GONE);

        return eventView;
    }

    @Override
    public PopupView getPopupView(final IPopup popup, Rect eventBound, int hourHeight,
                                  int seperateH) {
        PopupView popupView = super.getPopupView(popup, eventBound, hourHeight, seperateH);
        // popupView.show();
        CardView cardView = (CardView) popupView.findViewById(com.framgia.library.calendardayview.R.id.cardview);
        TextView textQuote = (TextView) popupView.findViewById(com.framgia.library.calendardayview.R.id.quote);
        TextView textTitle = (TextView) popupView.findViewById(com.framgia.library.calendardayview.R.id.title);
        ImageView imvEnd = (ImageView) popupView.findViewById(com.framgia.library.calendardayview.R.id.imv_end);
        ImageView inviteeImage = (ImageView) popupView.findViewById(com.framgia.library.calendardayview.R.id.image_start);

        cardView.setCardBackgroundColor(Color.parseColor("#FFEFD5"));

        if(model.getInviteeNum() >= 0) {
            List<ParseFile> inviteeImgs = model.getInviteeImages();
            Glide.with(context).
                    load(inviteeImgs.get(model.getInviteeIndex()).getUrl()).
                    into(inviteeImage);
            model.nextInviteeIndex();
        }

        textQuote.setVisibility(View.GONE);
        imvEnd.setVisibility(View.GONE);
        return popupView;
    }
}
