package rafaxplayer.comprasonline;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rafaxplayer.comprasonline.CLASS.AsyncJSON;
import rafaxplayer.comprasonline.CLASS.AsyncResponseJSON;
import rafaxplayer.comprasonline.HELP.GlobalUtilities;
import rafaxplayer.comprasonline.MODELS.Place;


public class MainActivity extends AppCompatActivity implements AsyncResponseJSON,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient=null;
    private ArrayList<Marker> listOfMarkers;

    private String NextTokenPage="";
    private FloatingActionButton fab;
    private ArrayList<Place> ListPlaces;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_activity_maps);
        }
        fab=(FloatingActionButton) findViewById(R.id.fab);
        listOfMarkers = new ArrayList<>();
        ListPlaces = new ArrayList<>();

        iniGoogleClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.details) {
            if(ListPlaces.size()>0){
                Intent in = new Intent(this,List_Activity.class);
                in.putParcelableArrayListExtra("places",this.ListPlaces);
                startActivity(in);

            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();


    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Atencion!")
                        .setMessage("Es necesario que permitas tener permisos de localización para el funcionamiento de esta app")
                        .setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setNegativeButton("No Acepto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();


            } else {
                Log.e("Peticion de permiso :", "Fine location");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {

            mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if(!marker.isInfoWindowShown()){
                        marker.showInfoWindow();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            public void run(){
                                marker.hideInfoWindow();
                            }
                        },4000);


                    }
                    return false;
                }
            });

        }
        if (!GlobalUtilities.checkGPS(this)) {
            Log.e("gps :", "Not Enabled");
            GlobalUtilities.buildAlertMessageNoGps(this);

        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        iniSites();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Api Google Error:",connectionResult.getErrorMessage());
    }

    private void iniGoogleClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }
    private void iniSites(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Atencion!")
                        .setMessage("Es necesario que permitas permisos de localización para el funcionamiento de esta app")
                        .setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);

                            }
                        }).setNegativeButton("No Acepto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {

                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                GlobalUtilities.updateLocation(latLng,mMap,15,300,30);
                String location = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);
                String urlPlaceMarkets=GlobalUtilities.urlPlaces(GlobalUtilities.URLBASE_PLACES,"",getResources().getString(R.string.goole_places_api_web),location,"2000","grocery_or_supermarket","");

                AsyncJSON getjson = new AsyncJSON(this,urlPlaceMarkets,GlobalUtilities.REQUEST_TYPE_PLACES);
                getjson.delegate = this;
                getjson.execute();

            }
        }

    }

    public void OnClick(View v){

        if(v.getId()==R.id.fab){

            if(!TextUtils.isEmpty(NextTokenPage)){
                String urlPlaceMarkets=GlobalUtilities.urlPlaces(GlobalUtilities.URLBASE_PLACES,"",getResources().getString(R.string.goole_places_api_web),"","","",NextTokenPage);

                AsyncJSON getjson=new AsyncJSON(this,urlPlaceMarkets,GlobalUtilities.REQUEST_TYPE_PLACES);
                getjson.delegate=this;
                getjson.execute();
            }else{
                Toast.makeText(this,"No hay mas establecimientos a mostrar",Toast.LENGTH_LONG).show();

            }
        }
    }

    private ArrayList<Place> parsePlaces(JSONObject json){
        ArrayList<Place> list = new ArrayList<>();
        try {
            //Log.e("token",json.getString("next_page_token"));
            this.NextTokenPage=json.has("next_page_token")?json.getString("next_page_token"):"";
            JSONArray jsonArray = json.getJSONArray("results");
                Log.e("COUNT :",String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject root = jsonArray.getJSONObject(i);
                JSONObject location= root.getJSONObject("geometry").getJSONObject("location");
                double latitude = location.getDouble("lat");
                double longitude = location.getDouble("lng");
                String name = root.has("name")?root.getString("name"):"No Name";
                String id = root.has("id")?root.getString("id"):"";
                String photo=root.has("photos") ? root.getJSONArray("photos").getJSONObject(0).getString("photo_reference") : "";
                String[] types = root.has("types")?GlobalUtilities.jsonArrayToStringArray(root.getJSONArray("types")):new String[]{};
                String iconurl = root.has("icon")?root.getString("icon"):"";
                String placeid = root.getString("place_id");
                String address = root.has("vicinity")?root.getString("vicinity"):"No Address";
                String opened = root.has("opening_hours")? root.getJSONObject("opening_hours").getString("open_now") : "N/A";
                
                Place plc = new Place();
                plc.setIcon(iconurl);
                plc.setId(id);
                plc.setLatittude(latitude);
                plc.setLongitude(longitude);
                plc.setName(name);
                plc.setPhoto_reference(photo);
                plc.setPlaceid(placeid);
                plc.setOpen(opened);
                plc.setTypes(types);
                plc.setAddress(address);
               // Log.e("ico",iconurl);
                list.add(plc);

            }
        } catch (JSONException e) {
            Log.e("Error :", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        return list;

    }

    @Override
    public void asyncFinish(JSONObject output, int type) {

        if(type == GlobalUtilities.REQUEST_TYPE_PLACES){
            ArrayList<Place> list = parsePlaces(output);
            this.ListPlaces.addAll(list);

            for(int i=0;i<ListPlaces.size();i++){

                Marker marker = GlobalUtilities.mostrarMarcador(mMap ,ListPlaces.get(i));
                if(marker != null){
                    listOfMarkers.add(marker);
                }

            }
            Toast.makeText(this, "Se Han caragado "+ String.valueOf(ListPlaces.size()), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GlobalUtilities.RESULT_GPS) {
            //restart Activity
            new Handler().post(new Runnable() {

                @Override
                public void run()
                {
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            });
        }
    }
}
