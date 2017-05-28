package id.ac.unj.gohalal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.ac.unj.gohalal.MenuActivity;
import id.ac.unj.gohalal.R;
import id.ac.unj.gohalal.SetterGetter.Review;

/**
 * Created by SuperNova's on 26/05/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<Review> reviews;
    Context context;

    public ReviewAdapter(int[]id,  int[]idmenu, int[]rate, String[]namauser,
                         String[] namamenu, String[] review, String[] dateReview,
                                 Context context) {

        super();
        reviews = new ArrayList<Review>();
        for (int i = 0; i < id.length; i++) {
            Review rvw = new Review();
                rvw.setId(id[i]);
                rvw.setIdmenu(idmenu[i]);
                rvw.setRate(rate[i]);
                rvw.setNamauser(namauser[i]);
                rvw.setNamamenu(namamenu[i]);
                rvw.setReview(review[i]);
                rvw.setContext(context);
                rvw.setCreated(dateReview[i]);
                reviews.add(rvw);


        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Review list = reviews.get(position);
        context = list.getContext();

        String diff = getTimeDiff(list.getCreated());
        holder.userName.setText(list.getNamauser());
        holder.reviewText.setText(String.valueOf(list.getReview()));
        holder.userReviewRating.setRating(list.getRate());
        holder.userTimePost.setText(diff);


    }

    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        CardView cardView;
        ImageView userPict;
        TextView userName,userTimePost,reviewText;
        RatingBar userReviewRating;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            cardView = (CardView) view.findViewById(R.id.card_view);
            userPict = (ImageView) view.findViewById(R.id.userPict);
            userName = (TextView) view.findViewById(R.id.userName);
            userTimePost = (TextView) view.findViewById(R.id.userTimePost);
            reviewText = (TextView) view.findViewById(R.id.reviewText);
            userReviewRating = (RatingBar) view.findViewById(R.id.userReviewRating);
        }
    }

    public String getTimeDiff(String endDate){
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = df.format(cal.getTime());

            Date d1 = df.parse(now);
            Date d2 = df.parse(endDate);

            Long diff = d1.getTime() - d2.getTime();
            Long seconds = diff / 1000;
            Long minutes = seconds / 60;
            Long hours = minutes / 60;
            Long days = hours / 24;

            if (days != 0){
                return days.toString() + " days ago";
            }else if(hours != 0){
                return hours.toString() + " hours ago";
            }else if(minutes != 0){
                return minutes.toString() + " minutes ago";
            }else {
                return seconds.toString() + " seconds ago";
            }

        }catch (ParseException e){
            e.printStackTrace();
        }

        return null;
    }
}