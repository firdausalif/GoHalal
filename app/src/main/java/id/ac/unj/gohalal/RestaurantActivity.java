package id.ac.unj.gohalal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import id.ac.unj.gohalal.Adapter.MenuAdapter;
import id.ac.unj.gohalal.Helper.JSONParser;
import id.ac.unj.gohalal.SetterGetter.MenuItem;
import id.ac.unj.gohalal.SetterGetter.Restaurant;

/**
 * Created by SuperNova's on 25/05/2017.
 */

public class RestaurantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MenuAdapter adapter;
    JSONParser jParser = new JSONParser();
    ProgressDialog pDialog;

    String MENU_URL= "http://gohalal.pe.hu/testv2/index.php/Menu";
    TextView restoName, restoAlamat, restoPhone, restoEmail, restoDeskripsi;
    ImageView restoCover;
    String TAG_MENU = "menu";
    String TAG_ID = "id";
    String TAG_IDRESTO = "idresto";
    String TAG_NAMA = "nama";
    String TAG_DESKRIPSI = "deskripsi";
    String TAG_ALAMAT = "alamat";
    String TAG_TELP = "telp";
    String TAG_EMAIL = "email";
    String TAG_IMAGE = "image";
    String TAG_RATE = "rate";
    String TAG_PRICE = "price";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restoCover = (ImageView) findViewById(R.id.pict) ;
        restoName = (TextView)findViewById(R.id.restoName);
        restoAlamat = (TextView)findViewById(R.id.isiAlamat);
        restoEmail = (TextView)findViewById(R.id.isiEmail);
        restoPhone = (TextView)findViewById(R.id.isiTelp);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        viewInformation();

    }

    public void viewInformation(){
        Intent intent = getIntent();
        final String restoid = intent.getExtras().getString(TAG_ID);
        final String nama = intent.getExtras().getString(TAG_NAMA);
        final String alamat = intent.getExtras().getString(TAG_ALAMAT);
        final String deskripsi = intent.getExtras().getString(TAG_DESKRIPSI);
        final String image = intent.getExtras().getString(TAG_IMAGE);
        final String telp = intent.getExtras().getString(TAG_TELP);
        final String email = intent.getExtras().getString(TAG_EMAIL);
        final int rate = intent.getExtras().getInt(TAG_RATE);



        if(intent.getExtras() != null){
            restoName.setText(nama);
            restoEmail.setText(email);
            restoAlamat.setText(alamat);
            restoPhone.setText(telp);
            Picasso.with(RestaurantActivity.this).load(image).into(restoCover);
        }

        LoadData loadData= new LoadData();
        loadData.execute(restoid);
    }

    class LoadData extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RestaurantActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String idresto= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idresto", idresto));

            JSONObject json = jParser.makeHttpRequest(MENU_URL, "GET", params);
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            try{

                if(result != null){
                    JSONArray array = result.getJSONArray(TAG_MENU);

                    String[] nama = new String[array.length()];
                    String[] deskripsi = new String[array.length()];
                    String[] images = new String[array.length()];
                    int[] price = new int[array.length()];
                    int[] rate = new int[array.length()];
                    int[] idresto = new int[array.length()];
                    int[] idmenu = new int[array.length()];

                    if(array != null || !array.equals("")){
                        for(int i = 0; i < array.length(); i++) {
                            idmenu[i] = array.getJSONObject(i).getInt(TAG_ID);
                            nama[i] = array.getJSONObject(i).getString(TAG_NAMA);
                            deskripsi[i] = array.getJSONObject(i).getString(TAG_DESKRIPSI);
                            price[i] = array.getJSONObject(i).getInt(TAG_PRICE);
                            rate[i] = array.getJSONObject(i).getInt(TAG_RATE);
                            images[i] = array.getJSONObject(i).getString(TAG_IMAGE);
                            idresto[i]  = array.getJSONObject(i).getInt(TAG_IDRESTO);

                            adapter = new MenuAdapter(idmenu, nama,deskripsi,images, idresto,rate,price,
                                    RestaurantActivity.this);
                            recyclerView.setAdapter(adapter);
                            pDialog.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Restaurant doesn't have menu yet...",Toast.LENGTH_LONG)
                            .show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }




}