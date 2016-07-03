package rafaxplayer.comprasonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import rafaxplayer.comprasonline.CLASS.ListPlaces_Adapter;
import rafaxplayer.comprasonline.MODELS.Place;
public class List_Activity extends AppCompatActivity  {
    private Toolbar toolbar;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
    }


}
