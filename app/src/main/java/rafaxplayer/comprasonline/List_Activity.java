package rafaxplayer.comprasonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import rafaxplayer.comprasonline.CLASS.ListPlaces_Adapter;
import rafaxplayer.comprasonline.MODELS.Place;
public class List_Activity extends AppCompatActivity  {
    private Toolbar toolbar;
    private RecyclerView list;
    public static int index = -1;
    public static int top = -1;
    private LinearLayoutManager lngManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lngManager=new LinearLayoutManager(getApplicationContext());
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(lngManager);
        list.setItemAnimator(new DefaultItemAnimator());
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_activity_list);

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Place> places = getIntent().getParcelableArrayListExtra("places");
        if (places != null) {
            list.setAdapter(new ListPlaces_Adapter(this,List_Activity.this, places));
        }
        //rstore list position
        if(index != -1)
        {
            lngManager.scrollToPositionWithOffset( index, top);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //read current recyclerview position
        index = lngManager.findFirstVisibleItemPosition();
        View v = list.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - list.getPaddingTop());
    }


}
