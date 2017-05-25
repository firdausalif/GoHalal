package id.ac.unj.gohalal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.ac.unj.gohalal.MenuActivity;
import id.ac.unj.gohalal.R;
import id.ac.unj.gohalal.SetterGetter.MenuItem;

/**
 * Created by SuperNova's on 25/05/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    List<MenuItem> menus;
    Context context;
    public MenuAdapter(String[] nama, String[] deskripsi, String[] images, int[]idresto, int[]rate, int[]price){
        super();
        menus = new ArrayList<MenuItem>();

        for(int i =0; i<nama.length; i++){
            MenuItem menu = new MenuItem();
            menu.setNama(nama[i]);
            menu.setDeskripsi(deskripsi[i]);
            menu.setIdresto(idresto[i]);
            menu.setRate(rate[i]);
            menu.setPrice(price[i]);
            menu.setImage(images[i]);
            menus.add(menu);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MenuItem list =  menus.get(position);
        holder.menuNama.setText(String.valueOf(list.getNama()));
        holder.menuHarga.setText("Rp. " + String.valueOf(list.getPrice()));
        holder.ratingMenu.setRating(list.getRate());
        Picasso.with(context).load(list.getImage()).into(holder.menuCover);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, MenuActivity.class);
                //intent.putExtra("nama", list.getNama());
                //context.startActivity(intent);

                Toast.makeText(context,"View Clicked", Toast.LENGTH_LONG).show();
            }
        });

    }

    public int getItemCount() {
        return menus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView menuNama;
        public TextView menuHarga;
        public ImageView menuCover;
        RatingBar ratingMenu;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            menuNama = (TextView) view.findViewById(R.id.menuNama);
            menuHarga = (TextView) view.findViewById(R.id.menuHarga);
            ratingMenu = (RatingBar) view.findViewById(R.id.myRatingBar);
            menuCover = (ImageView) view.findViewById(R.id.coverImageView);

        }
    }


}