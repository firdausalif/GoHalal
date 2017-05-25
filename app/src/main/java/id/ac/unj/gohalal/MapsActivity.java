package id.ac.unj.gohalal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.ac.unj.gohalal.SetterGetter.Restaurant;
import id.ac.unj.gohalal.Helper.JSONParser;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    String RESTAURANT_URL= "http://gohalal.pe.hu/testv2/index.php/Restaurant";

    ArrayList<HashMap<String, String>> dataMap = new ArrayList<HashMap<String, String>>();
    JSONParser jParser = new JSONParser();
    JSONArray str_json = null;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker, mShopMarker;
    TextView currLoc,targLoc;
    ImageView currPlace;
    LocationRequest mLocationRequest;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FloatingActionButton descButton;
    ProgressDialog pDialog;

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
    String TAG_RATE = "rate";
    String TAG_MENU = "menu";
    String TAG_PRICE = "price";
    String TAG_IDRESTO = "idresto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        currLoc = (TextView) findViewById(R.id.currloc);
        targLoc = (TextView)findViewById(R.id.targloc);
        descButton = (FloatingActionButton) findViewById(R.id.moreDesc);
        currPlace = (ImageView)findViewById(R.id.currentplace);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        new getMarkerInfo().execute();
        currPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildGoogleApiClient();
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trash:
                        Toast.makeText(getApplicationContext(),"Trash",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("admin@gohalal.pe.hu");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        }
        else {
            buildGoogleApiClient();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        Double getLatitude = location.getLatitude();
        Double getLongitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(getLatitude, getLongitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                }
                currLoc.setText(strReturnedAddress.toString());
            }
            else {
                currLoc.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            currLoc.setText("Canont get Address!");
            int count=0;
            if(count == 3) {
                Toast.makeText(getApplicationContext(),"Reboot ur phone if it still not appear",
                        Toast.LENGTH_LONG).show();
            }
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean onMarkerClick(Marker marker) {

        for(int i = 0; i < dataMap.size(); i++) {
            String id = dataMap.get(i).get(TAG_ID);
            String nama = dataMap.get(i).get(TAG_NAMA);
            String alamat = dataMap.get(i).get(TAG_ALAMAT);
            String deskripsi = dataMap.get(i).get(TAG_DESKRIPSI);
            String image = dataMap.get(i).get(TAG_IMAGE);
            String telp = dataMap.get(i).get(TAG_TELP);
            String email = dataMap.get(i).get(TAG_EMAIL);
            String rate = dataMap.get(i).get(TAG_RATE);

            if(marker.getTitle().equalsIgnoreCase(nama)){
                Intent in = new Intent(getApplicationContext(),RestaurantActivity.class);
                in.putExtra(TAG_ID, id);
                in.putExtra(TAG_NAMA, nama);
                in.putExtra(TAG_ALAMAT, alamat);
                in.putExtra(TAG_DESKRIPSI, deskripsi);
                in.putExtra(TAG_IMAGE, image);
                in.putExtra(TAG_TELP, telp);
                in.putExtra(TAG_EMAIL,email);
                in.putExtra(TAG_RATE, rate);
                startActivity(in);
            }

        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    class getMarkerInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            JSONObject json = jParser.getJson(RESTAURANT_URL);

            try {
                str_json = json.getJSONArray(TAG_RESTO);

                for(int i = 0; i < str_json.length(); i++)
                {
                    JSONObject ar = str_json.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    String LL = ar.getString(TAG_LOC);
                    String[] langlat = LL.split(",");
                    String latitude = langlat[0];
                    String longitude = langlat[1];

                    map.put(TAG_ID, ar.getString(TAG_ID));
                    map.put(TAG_NAMA, ar.getString(TAG_NAMA));
                    map.put(TAG_DESKRIPSI, ar.getString(TAG_DESKRIPSI));
                    map.put(TAG_LAT,  latitude);
                    map.put(TAG_LONG,  longitude);
                    map.put(TAG_ALAMAT, ar.getString(TAG_ALAMAT));
                    map.put(TAG_TELP, ar.getString(TAG_TELP));
                    map.put(TAG_IMAGE, ar.getString(TAG_IMAGE));
                    map.put(TAG_EMAIL, ar.getString(TAG_EMAIL));
                    map.put(TAG_RATE, ar.getString(TAG_RATE));

                    dataMap.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String URL) {
            runOnUiThread(new Runnable() {
                public void run() {
                    final ArrayList<Restaurant> restList = new ArrayList<Restaurant>();
                    for (int i = 0; i < dataMap.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map = dataMap.get(i);

                        LatLng POSISI = new LatLng(Double.parseDouble(map.get(TAG_LAT)),
                                Double.parseDouble(map.get(TAG_LONG)));
                        String nama = map.get(TAG_NAMA);
                        String deskripsi = map.get(TAG_DESKRIPSI);

                        mShopMarker = mMap.addMarker(new MarkerOptions()
                                .position(POSISI)
                                .title(nama)
                                .snippet(deskripsi)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                        .HUE_GREEN)));
                    }
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            if(marker.getTitle().contains("Store")) {
                                onMarkerClick(marker);
                            }
                        }
                    });
                }
            });

        }

    }




}