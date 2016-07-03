package rafaxplayer.comprasonline.CLASS;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rafax on 04/06/2016.
 */
public class ConectorHttpJSON {
    private String url;

    public ConectorHttpJSON(String url) {
        this.url = url;
    }

    public JSONObject execute() throws IOException, MalformedURLException, JSONException{

        URL url = new URL(this.url);
        String response="error";
        String response2;
        HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();

        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = inputStreamToString(in);
            //response = urlConnection.getResponseMessage();
            }

            finally {
                urlConnection.disconnect();
            }
            //Log.e("JSON :", response);

            JSONObject object = new JSONObject(response);

        return object;
    }

    private String inputStreamToString(InputStream is)
            throws UnsupportedEncodingException {
        String line = "";
        StringBuilder sb = new StringBuilder();
        // Guardamos la dirección en un buffer de lectura
        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                "utf-8"), 8);

        // Y la leemos toda hasta el final
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (Exception ex) {
            Log.w("Aviso", ex.toString());
        }

        // Devolvemos todo lo leido
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
