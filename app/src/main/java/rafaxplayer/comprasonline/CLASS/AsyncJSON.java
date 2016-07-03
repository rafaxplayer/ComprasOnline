package rafaxplayer.comprasonline.CLASS;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;


/**
 * Created by rafax on 07/06/2016.
 */
public class AsyncJSON extends AsyncTask<Void, Void, JSONObject> {

    private String url;
    private Context context;
    public AsyncResponseJSON delegate = null;
    private int type;
    private ProgressDialog progress;


    public AsyncJSON(Context context,String url, int type) {
        this.url = url;
        this.type = type;
        this.context = context;
        this.progress= new ProgressDialog(context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progress!=null) {
            progress.setMessage("Cargando establecimientos....");
            if(!progress.isShowing()) {
                progress.show();
            }
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        ConectorHttpJSON conector = new ConectorHttpJSON(url);
        Log.e("Url :", url);
        JSONObject obj = null;
        try {

            obj = conector.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        delegate.asyncFinish(result, type);
        if(progress.isShowing()){
            progress.hide();
        }

        super.onPostExecute(result);
    }
}
