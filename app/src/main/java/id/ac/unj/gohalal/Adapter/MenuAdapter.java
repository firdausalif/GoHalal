package id.ac.unj.gohalal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
    String TAG_NAMA = "nama";
    String TAG_DESKRIPSI = "deskripsi";
    String TAG_ALAMAT = "alamat";
    String TAG_TELP = "telp";
    String TAG_EMAIL = "email";
    String TAG_IMAGE = "image";
    String TAG_RATE = "rate";
    String TAG_PRICE = "price";

    public MenuAdapter(String[] nama, String[] deskripsi, String[] images, int[]idresto, int[]rate,
                       int[]price, Context context){
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
            menu.setContext(context);
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
        context = list.getContext();

        holder.menuNama.setText(String.valueOf(list.getNama()));
        holder.menuHarga.setText("Rp. " + String.valueOf(list.getPrice()));
        holder.ratingMenu.setRating(list.getRate());
        Picasso.with(context).load(list.getImage()).into(holder.menuCover);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra(TAG_NAMA, list.getNama());
                intent.putExtra(TAG_IMAGE, list.getImage());
                intent.putExtra(TAG_PRICE, list.getPrice());
                intent.putExtra(TAG_RATE, list.getRate());
                context.startActivity(intent);
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
        public CardView cardView;
        RatingBar ratingMenu;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            cardView = (CardView) view.findViewById(R.id.card_view);
            menuNama = (TextView) view.findViewById(R.id.menuNama);
            menuHarga = (TextView) view.findViewById(R.id.menuHarga);
            ratingMenu = (RatingBar) view.findViewById(R.id.myRatingBar);
            menuCover = (ImageView) view.findViewById(R.id.coverImageView);

        }
    }


}