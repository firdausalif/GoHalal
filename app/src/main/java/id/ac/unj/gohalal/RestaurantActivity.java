package id.ac.unj.gohalal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.unj.gohalal.SetterGetter.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    TextView restoName, restoAlamat, restoPhone, restoEmail;
    String TAG_RESTO = "restaurant";
    String TAG_ID = "id";
    String TAG_NAMA = "nama";
    String TAG_LOC = "langlat";
    String TAG_LAT = "latitude";
    String TAG_LONG = "longitude";
    String TAG_DESKRIPSI = "deskripsi";
    String TAG_ALAMAT = "alamat";
    String TAG_TELP = "telp";
    String TAG_EMAIL = "email";
    String TAG_IMAGE = "image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restoName = (TextView)findViewById(R.id.restoName);
        restoAlamat = (TextView)findViewById(R.id.isiAlamat);
        restoEmail = (TextView)findViewById(R.id.isiEmail);
        restoPhone = (TextView)findViewById(R.id.isiTelp);

       viewInformation();


    }

    public void viewInformation(){
        Intent intent = getIntent();
        String nama = intent.getExtras().getString(TAG_NAMA);
        String alamat = intent.getExtras().getString(TAG_ALAMAT);
        String deskripsi = intent.getExtras().getString(TAG_DESKRIPSI);
        String image = intent.getExtras().getString(TAG_IMAGE);
        String telp = intent.getExtras().getString(TAG_TELP);
        String email = intent.getExtras().getString(TAG_EMAIL);

        if(intent.getExtras() != null){
            restoName.setText(nama);
            restoEmail.setText(email);
            restoAlamat.setText(alamat);
            restoPhone.setText(telp);

        }


    }
}
