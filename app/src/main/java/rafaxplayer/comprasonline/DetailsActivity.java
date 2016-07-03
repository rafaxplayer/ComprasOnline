package rafaxplayer.comprasonline;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import rafaxplayer.comprasonline.CLASS.AsyncJSON;
import rafaxplayer.comprasonline.CLASS.AsyncResponseJSON;
import rafaxplayer.comprasonline.HELP.GlobalUtilities;
import rafaxplayer.comprasonline.MODELS.PLaceDetails;
import rafaxplayer.comprasonline.MODELS.Place;

public class DetailsActivity extends AppCompatActivity implements AsyncResponseJSON {
    private String photoReference = "";
    private ImageView photoPlace;
    private String phonenumber = "";
    private Place currentPlace=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle");
        }
        photoPlace = (ImageView) findViewById(R.id.imagePlace);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(DetailsActivity.this.phonenumber)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + DetailsActivity.this.phonenumber));
                    if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(DetailsActivity.this, Manifest.permission.CALL_PHONE)) {
                            new AlertDialog.Builder(DetailsActivity.this).setTitle("Atencion!")
                                    .setMessage("Es necesario que permitas tener permisos de llamadas para el funcionamiento de esta app")
                                    .setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(DetailsActivity.this,
                                                    new String[]{Manifest.permission.CALL_PHONE},
                                                    GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);
                                        }
                                    }).setNegativeButton("No Acepto", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();


                        } else {
                            Log.e("Peticion de permiso :", "Fine location");
                            ActivityCompat.requestPermissions(DetailsActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    GlobalUtilities.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }else{
                        startActivity(intent);
                    }

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPlace = getIntent().getParcelableExtra("place");
        if (currentPlace != null) {
            String urldetails = GlobalUtilities.urlPlaces(GlobalUtilities.URLBASE_PLACEDETAILS, currentPlace.getPlaceid(), getResources().getString(R.string.goole_places_api_web), "", "", "", "");
            AsyncJSON getjson = new AsyncJSON(this, urldetails, GlobalUtilities.REQUEST_TYPE_PLACEDETAILS);
            getjson.delegate = this;
            getjson.execute();

        }

    }

    private void parseDetails(Place plc,JSONObject json) {

        try {
            JSONObject root = json.getJSONObject("result");
            String name = root.has("name") ? root.getString("name") : "No Name";
            String addres = root.has("formatted_address") ? root.getString("formatted_address") : "No Address";
            this.phonenumber = root.has("formatted_phone_number") ? root.getString("formatted_phone_number") : "";
            String placeid = root.has("place_id") ? root.getString("place_id") : "";
            String rating = root.has("rating") ? root.getString("rating") : "0.0";
            String website = root.has("website") ? root.getString("website") : "No Web Site URL";
            String googlesite = root.has("url") ? root.getString("url") : "No Web Site URL";
            Boolean opened = root.has("opening_hours") ? root.getJSONObject("opening_hours").getBoolean("open_now") : null;
            if (opened != null) {
                int color = opened ? R.color.ok_color : R.color.error_color;
                ((TextView) findViewById(R.id.textViewIsOpen)).setTextColor(ContextCompat.getColor(this, color));
                ((TextView) findViewById(R.id.textViewIsOpen)).setText(opened ? "Abierto" : "Cerrado");
            }
            ((TextView) findViewById(R.id.textViewName)).setText(name);
            ((TextView) findViewById(R.id.textViewAddress)).setText(addres);
            ((TextView) findViewById(R.id.textViewRating)).setText(rating);
            ((TextView) findViewById(R.id.textViewPhone)).setText(this.phonenumber);
            ((RatingBar) findViewById(R.id.ratingBar)).setRating(Float.valueOf(rating));
            ((TextView) findViewById(R.id.textViewWebsite)).setText(website);
            if(plc != null){
                String url = GlobalUtilities.urlPhotos(GlobalUtilities.URLBASE_IMAGEPLACE,getResources().getString(R.string.goole_places_api_web),plc.getPhoto_reference());
                Picasso.with(this).load(url).placeholder(R.drawable.placeholder_image).into(photoPlace);
            }


        } catch (JSONException e) {
            Log.e("Error :", e.getMessage());
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }


    }
    @Override
    public void asyncFinish(JSONObject output, int type) {

        if (type == GlobalUtilities.REQUEST_TYPE_PLACEDETAILS) {
            parseDetails(this.currentPlace,output);


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
            supportFinishAfterTransition();
    }
}
