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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.ac.unj.gohalal.MenuActivity;
import id.ac.unj.gohalal.R;
import id.ac.unj.gohalal.RestaurantActivity;
import id.ac.unj.gohalal.SetterGetter.MenuItem;

/**
 * Created by SuperNova's on 25/05/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    List<MenuItem> menus;
    Context context;
    public static View view;
    String[] images;
    String[] deskripsi;
    String[] nama;
    int[] idresto;
    int[] rate;
    int[] price;


    public MenuAdapter(String[] nama, String[] deskripsi, String[] images, int[]idresto, int[]rate,
                       int[]price, Context context){
        super();
        menus = new ArrayList<MenuItem>();
        this.context = context;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.images = images;
        this.idresto = idresto;
        this.rate = rate;
        this.price = price;

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

        ViewHolder viewHolder = new ViewHolder(v, context,nama,deskripsi,images,idresto,rate,price);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView menuNama;
        public TextView menuHarga;
        public ImageView menuCover;
        RatingBar ratingMenu;
        Context context;

        public static View view;
        List<MenuItem> menus;
        String[] images;
        String[] deskripsi;
        String[] nama;
        int[] idresto;
        int[] rate;
        int[] price;

        String TAG_NAMA = "nama";
        String TAG_DESKRIPSI = "deskripsi";
        String TAG_ALAMAT = "alamat";
        String TAG_TELP = "telp";
        String TAG_EMAIL = "email";
        String TAG_IMAGE = "image";
        String TAG_RATE = "rate";
        String TAG_PRICE = "price";

        public ViewHolder(View itemView, Context context, String[] nama, String[] deskripsi,
                          String[] images, int[]idresto, int[]rate, int[]price) {

            super(itemView);
            this.nama = nama;
            this.deskripsi = deskripsi;
            this.images = images;
            this.idresto = idresto;
            this.rate = rate;
            this.price = price;

            this.context = context;
            menus = new ArrayList<MenuItem>();
            view.setOnClickListener(this);

            menuNama = (TextView) itemView.findViewById(R.id.menuNama);
            menuHarga = (TextView) itemView.findViewById(R.id.menuHarga);
            ratingMenu = (RatingBar) itemView.findViewById(R.id.myRatingBar);
            menuCover = (ImageView) itemView.findViewById(R.id.coverImageView);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MenuItem list =  menus.get(position);
            Intent intent = new Intent(context, MenuActivity.class);
            intent.putExtra(TAG_NAMA, list.getNama());
            this.context.startActivity(intent);
        }
    }

}
