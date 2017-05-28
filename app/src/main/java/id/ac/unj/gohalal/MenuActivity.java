package id.ac.unj.gohalal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import id.ac.unj.gohalal.Adapter.MenuAdapter;
import id.ac.unj.gohalal.Adapter.ReviewAdapter;
import id.ac.unj.gohalal.Helper.CustomToast;
import id.ac.unj.gohalal.Helper.JSONParser;

public class MenuActivity extends AppCompatActivity {
    String TAG_NAMA = "nama";
    String TAG_IMAGE = "image";
    String TAG_RATE = "rate";
    String TAG_PRICE = "price";
    String TAG_ID = "id";
    String TAG_DATA = "data";
    String TAG_RESTOID = "restoid";
    String TAG_MENUID = "menuid";
    String TAG_USERNAMA = "username";
    String TAG_MENUNAMA = "menuname";
    String TAG_REVIEW = "review";
    String TAG_CREATED = "created";
    public static final String MyPref = "gohalal" ;

    public static String REVIEW_URL= "http://gohalal.pe.hu/GoHalal/index.php/Review" ;

    JSONParser jParser = new JSONParser();
    ReviewAdapter adapter;
    Button addReview;
    TextView menuNama, menuHarga;
    ImageView menuCover;
    RatingBar menuRate, reviewRate;
    EditText userReview;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuNama = (TextView)findViewById(R.id.menuNama);
        menuHarga = (TextView)findViewById(R.id.menuHarga);
        menuCover = (ImageView)findViewById(R.id.coverImageView);
        menuRate = (RatingBar)findViewById(R.id.myRatingBar);
        addReview = (Button) findViewById(R.id.addReview);
        userReview = (EditText) findViewById(R.id.userReview);
        reviewRate = (RatingBar) findViewById(R.id.reviewRate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , true);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferences = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        loadMenu();

        addReview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                checkValidation();
            }
        });

    }

    public void loadMenu(){
        Intent intent = getIntent();
        String nama_menu = intent.getExtras().getString(TAG_NAMA);
        int menuid = intent.getExtras().getInt(TAG_MENUID);
        String image = intent.getExtras().getString(TAG_IMAGE);
        int price = intent.getExtras().getInt(TAG_PRICE);
        int rate = intent.getExtras().getInt(TAG_RATE);

        if(intent.getExtras() != null){
            Picasso.with(MenuActivity.this).load(image).into(menuCover);
            menuNama.setText(nama_menu);
            menuHarga.setText("Rp. " + String.valueOf(price));
            menuRate.setRating(rate);
        }

       GetReview getReview = new GetReview();
       getReview.execute(Integer.toString(menuid));
    }

    class GetReview extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String menuid= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("menuid", menuid));

            JSONObject json = jParser.makeHttpRequest(REVIEW_URL + "?menuid=" + menuid ,"GET", params);
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            try{
                if(result != null){
                    JSONArray array = result.getJSONArray(TAG_DATA);

                    String[] username = new String[array.length()];
                    String[] menuname = new String[array.length()];
                    String[] review = new String[array.length()];
                    String[] dateReview = new String[array.length()];
                    int[] idmenu = new int[array.length()];
                    int[] rate = new int[array.length()];
                    int[] idreview = new int[array.length()];

                    if(array != null || !array.equals("")){
                        for(int i = 0; i < array.length(); i++) {

                            idmenu[i] = array.getJSONObject(i).getInt(TAG_MENUID);
                            idreview[i] = array.getJSONObject(i).getInt(TAG_ID);
                            rate[i] = array.getJSONObject(i).getInt(TAG_RATE);
                            username[i] = array.getJSONObject(i).getString(TAG_USERNAMA);
                            menuname[i] = array.getJSONObject(i).getString(TAG_MENUNAMA);
                            review[i] = array.getJSONObject(i).getString(TAG_REVIEW);
                            dateReview[i] = array.getJSONObject(i).getString(TAG_CREATED);
                        }

                        pDialog.dismiss();
                        adapter = new ReviewAdapter(idreview, idmenu, rate, username,
                                menuname, review, dateReview, MenuActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }else {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"No one reviewed this menu...",
                            Toast.LENGTH_LONG)
                            .show();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    class PostReview extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String menuid= args[0];
            String rate = args[1];
            String username = args[2];
            String menuname = args [3];
            String review = args [4];
            String created = args [5];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("menuid", menuid));
            params.add(new BasicNameValuePair("rate", rate));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("menuname", menuname));
            params.add(new BasicNameValuePair("review", review));
            params.add(new BasicNameValuePair("created", created));

            JSONObject json = jParser.makeHttpRequest(REVIEW_URL, "POST", params);
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            try {
                Intent intent = getIntent();
                int menu_id = intent.getExtras().getInt(TAG_MENUID);
                if(result != null){
                    int resultjson = result.getInt("success");
                    if (resultjson == 1){
                        pDialog.dismiss();
                        GetReview getReview = new GetReview();
                        getReview.execute(Integer.toString(menu_id));
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(MenuActivity.this, "Failed to post review try again",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    pDialog.dismiss();
                    Toast.makeText(MenuActivity.this, "Error Retrieving Data",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkValidation(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getCreated = df.format(cal.getTime());

        Intent intent = getIntent();
        int getMenuId = intent.getExtras().getInt(TAG_MENUID);
        float getRate = reviewRate.getRating();
        String getUserName = sharedPreferences.getString("username", "default");
        String getMenuName = intent.getExtras().getString("nama");
        String getReview = userReview.getText().toString();

        if(getRate == 0 || getReview.equals("") || getReview.equals("0")){
            Toast.makeText(getApplicationContext(), "All input needed (Star and Review)",
                    Toast.LENGTH_LONG).show();
        }else{
            PostReview postReview = new PostReview();
            postReview.execute(Integer.toString(getMenuId),Float.toString(getRate),getUserName,
                    getMenuName,getReview,getCreated);
        }

    }

}
