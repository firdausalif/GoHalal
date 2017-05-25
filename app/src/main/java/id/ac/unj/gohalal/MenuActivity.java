package id.ac.unj.gohalal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    String TAG_NAMA = "nama";
    String TAG_DESKRIPSI = "deskripsi";
    String TAG_ALAMAT = "alamat";
    String TAG_TELP = "telp";
    String TAG_EMAIL = "email";
    String TAG_IMAGE = "image";
    String TAG_RATE = "rate";
    String TAG_PRICE = "price";

    TextView menuNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuNama = (TextView)findViewById(R.id.menuNama);
        Intent intent = getIntent();

        String namaMenu = intent.getExtras().getString(TAG_NAMA);
        menuNama.setText(namaMenu);
    }
}
