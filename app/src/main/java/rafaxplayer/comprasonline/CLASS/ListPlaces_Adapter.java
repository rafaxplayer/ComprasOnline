package rafaxplayer.comprasonline.CLASS;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rafaxplayer.comprasonline.DetailsActivity;
import rafaxplayer.comprasonline.HELP.GlobalUtilities;

import rafaxplayer.comprasonline.MODELS.Place;
import rafaxplayer.comprasonline.R;

/**
 * Created by rafax on 26/06/2016.
 */
public class ListPlaces_Adapter extends RecyclerView.Adapter<ListPlaces_Adapter.ViewHolder> {
    private ArrayList<Place> list;
    private Context con;
    private Activity act;

    public ListPlaces_Adapter(Context con,Activity act, ArrayList<Place> lst) {
        this.con = con;
        this.list = lst;
        this.act=act;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ListPlaces_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(((Place) list.get(position)).getName());
        holder.address.setText(((Place) list.get(position)).getAddress());
        String url = GlobalUtilities.urlPhotos(GlobalUtilities.URLBASE_IMAGEPLACE,con.getResources().getString(R.string.goole_places_api_web),((Place) list.get(position)).getPhoto_reference());
        Picasso.with(con).load(url).placeholder(R.drawable.placeholder_image).into(holder.image);

    }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView name;
            public TextView address;

            private ImageView image;


            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                v.findViewById(R.id.cardchild).setOnClickListener(this);
                name = (TextView) v.findViewById(R.id.textname);
                address = (TextView) v.findViewById(R.id.textaddress);

                image = (ImageView) v.findViewById(R.id.imagePlaceList);

            }
            @Override
            public void onClick(View view) {
                Intent in = new Intent(con, DetailsActivity.class);
                in.putExtra("place",((Place)list.get(ViewHolder.this.getLayoutPosition())));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                            act, Pair.create(view.findViewById(R.id.imagePlaceList), image.getTransitionName())
                                ,Pair.create(view.findViewById(R.id.textname),name.getTransitionName()));
                    con.startActivity(in, transitionActivityOptions.toBundle());
                } else {

                    con.startActivity(in);
                }

            }

        }


}
