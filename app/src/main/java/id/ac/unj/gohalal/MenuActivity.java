package id.ac.unj.gohalal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {
    String TAG_NAMA = "nama";
    String TAG_DESKRIPSI = "deskripsi";
    String TAG_ALAMAT = "alamat";
    String TAG_TELP = "telp";
    String TAG_EMAIL = "email";
    String TAG_IMAGE = "image";
    String TAG_RATE = "rate";
    String TAG_PRICE = "price";
    String TAG_USRID = "userid";
    String TAG_RESTOID = "restoid";
    String TAG_MENUID = "menuid";

    TextView menuNama, menuHarga;
    ImageView menuCover;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuNama = (TextView)findViewById(R.id.menuNama);
        menuHarga = (TextView)findViewById(R.id.menuHarga);
        menuCover = (ImageView)findViewById(R.id.pict);

        loadMenu();

    }

    public void loadMenu(){
        Intent intent = getIntent();
        int resto_id = intent.getExtras().getInt(TAG_RESTOID);
        String nama_menu = intent.getExtras().getString(TAG_NAMA);
        int menu_id = intent.getExtras().getInt(TAG_MENUID);
        String image = intent.getExtras().getString(TAG_IMAGE);
        int price = intent.getExtras().getInt(TAG_PRICE);
        int rate = intent.getExtras().getInt(TAG_RATE);

        if(intent.getExtras() != null){
            Picasso.with(MenuActivity.this).load(image).into(menuCover);
            menuNama.setText(nama_menu);
            menuHarga.setText("Rp. " + String.valueOf(price));
        }
    }
}
