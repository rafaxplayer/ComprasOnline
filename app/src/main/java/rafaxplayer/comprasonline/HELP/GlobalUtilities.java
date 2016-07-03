package rafaxplayer.comprasonline.HELP;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rafaxplayer.comprasonline.MODELS.Place;
import rafaxplayer.comprasonline.R;

/**
 * Created by rafax on 22/06/2016.
 */
public class GlobalUtilities {
    public static int RESULT_GPS = 5;
    public static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static int REQUEST_TYPE_PLACES = 524353;
    public static int REQUEST_TYPE_PLACEDETAILS = 524354;

    public static String URLBASE_PLACES = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static String URLBASE_PLACEDETAILS = "https://maps.googleapis.com/maps/api/place/details/json?";
    public static String URLBASE_IMAGEPLACE = "https://maps.googleapis.com/maps/api/place/photo?";

    public static void showSimpleAlert(String msg, Context con) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isConnectingToInternet(Context con) {
        ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void buildAlertMessageNoGps(final Context con) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        ((Activity)con).startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),RESULT_GPS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    public static boolean checkGPS(Context con) {
        LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public static void updateLocation(LatLng location, GoogleMap map, int zoom, int bearing, int tilt) {
        try {

            CameraPosition camPos = new CameraPosition.Builder().target(location)
                    .zoom(zoom).bearing(bearing).tilt(tilt).build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }

    }
    public static String urlPhotos(String url,String key,String reference){
        StringBuilder urlPhoto = new StringBuilder();
        urlPhoto.append(url).append("maxwidth=400").append("&key=").append(key).append("&photoreference=").append(reference);

        return urlPhoto.toString();


    }
    public static String urlPlaces(String urlBase, String placeid,String key, String location, String radius, String types, String token) {

        StringBuilder url = new StringBuilder();
        url.append(urlBase)
                .append("key=").append(key);
        if(!TextUtils.isEmpty(placeid)){
            url.append("&placeid=").append(placeid);
        }else if (!TextUtils.isEmpty(token)) {
            url.append("&pagetoken=" + token);
        } else {
            url.append("&location=")
                    .append(location)
                    .append("&radius=")
                    .append(radius)
                    .append("&types=")
                    .append(types);
        }


        Log.e("URL :", url.toString());
        return url.toString();
    }

    public static Marker mostrarMarcador(GoogleMap map, Place plc) {
        Marker marker = null;
        try {
            //limpiarMapa(map);
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(plc.getLatittude(), plc.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    .snippet(plc.getAddress())
                    .title(plc.getName()));
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return marker;
    }

    public static void limpiarMapa(GoogleMap map) {
        if (map != null) {
            map.clear();

        }

    }

    public static String[] jsonArrayToStringArray(JSONArray jsonArray) {
        int arraySize = jsonArray.length();
        String[] stringArray = new String[arraySize];

        for (int i = 0; i < arraySize; i++) {
            try {
                stringArray[i] = (String) jsonArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stringArray;
    }

    public static Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);


        }catch(Exception e){
            Log.d("Error downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }
}
