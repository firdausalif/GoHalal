package id.ac.unj.gohalal.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

        MenuItem list =  menus.get(position);
        holder.menuNama.setText(String.valueOf(list.getNama()));
        holder.menuHarga.setText("Rp. " + String.valueOf(list.getPrice()));
        holder.ratingMenu.setRating(list.getRate());
        Picasso.with(context).load(list.getImage()).into(holder.menuCover);

    }

    public int getItemCount() {
        return menus.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView menuNama;
        public TextView menuHarga;
        public ImageView menuCover;
        RatingBar ratingMenu;

        public ViewHolder(View itemView) {
            super(itemView);

            menuNama = (TextView) itemView.findViewById(R.id.menuNama);
            menuHarga = (TextView) itemView.findViewById(R.id.menuHarga);
            ratingMenu = (RatingBar) itemView.findViewById(R.id.myRatingBar);
            menuCover = (ImageView) itemView.findViewById(R.id.coverImageView);
        }
    }


}
