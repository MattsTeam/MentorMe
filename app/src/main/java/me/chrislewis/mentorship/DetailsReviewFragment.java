package me.chrislewis.mentorship;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.chrislewis.mentorship.models.PercentFormatter;
import me.chrislewis.mentorship.models.Review;
import me.chrislewis.mentorship.models.User;

public class DetailsReviewFragment extends Fragment {

    SharedViewModel model;
    User user;
    User currentUser;
    SwipeRefreshLayout swipeDetailReview;

    ReviewAdapter adapter;
    List<Review> reviews;
    List<Review> ratingReviews;
    RecyclerView rvReviews;

    PieChart pieChart;
    int oneStarCount = 0;
    int twoStarCount = 0;
    int threeStarCount = 0;
    int fourStarCount = 0;
    int fiveStarCount = 0;
    TextView aveRating;
    CardView cardView;

    Button writeReviewButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_reviews, container, false);
    }

    public void setUpChart(double percent1, double percent2, double percent3, double percent4, double percent5) {
        Drawable star_icon = getResources().getDrawable(R.drawable.ic_chart_star);
        List<PieEntry> entries = new ArrayList<>();
        if(percent1 > 0) {
            entries.add(new PieEntry((float) percent1, "1 star"));
        }
        if(percent2 > 0) {
            entries.add(new PieEntry((float) percent2, "2 stars"));
        }
        if(percent3 > 0) {

            entries.add(new PieEntry((float) percent3, "3 stars"));
        }
        if(percent4 > 0) {
            entries.add(new PieEntry((float) percent4, "4 stars"));
        }
        if(percent5 > 0) {
            entries.add(new PieEntry((float) percent5, "5 stars"));
        }
        PieDataSet set = new PieDataSet(entries, "Ratings");
        set.setColors(new int[] {R.color.chart1, R.color.chart2, R.color.chart3, R.color.chart4, R.color.chart5}, getContext());
        set.setValueTextSize(12);
        set.setValueFormatter(new PercentFormatter());
        set.setValueTextColor(Color.WHITE);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);

        Description description = new Description();
        description.setText("");
        description.setTextColor(R.color.white);
        pieChart.setDescription(description);
        pieChart.setEntryLabelTextSize(10);

        pieChart.invalidate();
        pieChart.animateX(1500);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public void sortRatings(List<Review> ratingReviews) {
        Double total = 0.0;
        for(int i = 0; i < ratingReviews.size(); i++) {
            total = total + ratingReviews.get(i).getDouble("rating");
            Log.d("Reviews", Double.toString(ratingReviews.get(i).getDouble("rating")));
            int currRating = (int) Math.ceil(ratingReviews.get(i).getDouble("rating"));
            if(currRating == 1) { oneStarCount = oneStarCount + 1; }
            else if(currRating == 2) { twoStarCount = twoStarCount + 1; }
            else if(currRating == 3) { threeStarCount = threeStarCount + 1; }
            else if(currRating == 4) { fourStarCount = fourStarCount + 1; }
            else { fiveStarCount = fiveStarCount + 1; }
        }
        if(ratingReviews.size() == 0) {
            cardView.setVisibility(View.GONE);
        }
        else {
            double average = round(total / ratingReviews.size(), 1);
            Log.d("DetailsReview", "Rating average: " + Double.toString(average));
            aveRating.setText(Double.toString(average));
            user.setOverallRating(average);
            user.saveInBackground();
            Log.d("Reviews", "Average: " + Double.toString(average));
            double percent1 = ((double) oneStarCount / ratingReviews.size()) * 100;
            double percent2 = ((double) twoStarCount / ratingReviews.size()) * 100;
            double percent3 = ((double) threeStarCount / ratingReviews.size()) * 100;
            double percent4 = ((double) fourStarCount / ratingReviews.size()) * 100;
            double percent5 = ((double) fiveStarCount / ratingReviews.size()) * 100;
            Log.d("Reviews", "1 star: " + Double.toString(percent1));
            Log.d("Reviews", "2 star: " + Double.toString(percent2));
            Log.d("Reviews", "3 star: " + Double.toString(percent3));
            Log.d("Reviews", "4 star: " + Double.toString(percent4));
            Log.d("Reviews", "5 star: " + Double.toString(percent5));
            setUpChart(percent1, percent2, percent3, percent4, percent5);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        user = model.getUser();
        currentUser = model.getCurrentUser();

        reviews = new ArrayList<>();
        ratingReviews = new ArrayList<>();
        adapter = new ReviewAdapter(reviews);

        pieChart = view.findViewById(R.id.pieChart);
        aveRating = view.findViewById(R.id.tvAve);
        getUserReviews();
        cardView = view.findViewById(R.id.statsBox);
        sortRatings(ratingReviews);

        rvReviews = view.findViewById(R.id.rvDetailsReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvReviews.setAdapter(adapter);

        writeReviewButton = view.findViewById(R.id.writeReviewButton);
        if (!currentUser.getFavorites().contains(user)) {
            writeReviewButton.setVisibility(View.INVISIBLE);
        }
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeReviewFragment composeReviewFragment = ComposeReviewFragment.newInstance();
                composeReviewFragment.show(getFragmentManager(), null);
            }
        });

    }

    public void getUserReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.whereEqualTo("userId", user.getObjectId());
        try {
            adapter.clear();
            List<Review> chartReviews = query.find();
            Collections.reverse(chartReviews);
            adapter.addAll(chartReviews);
            adapter.notifyDataSetChanged();
            ratingReviews.addAll(chartReviews);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
