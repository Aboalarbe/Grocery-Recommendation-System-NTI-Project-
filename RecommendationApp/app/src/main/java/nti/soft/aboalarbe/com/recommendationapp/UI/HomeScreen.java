package nti.soft.aboalarbe.com.recommendationapp.UI;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nti.soft.aboalarbe.com.recommendationapp.Adapters.ItemAdapter;
import nti.soft.aboalarbe.com.recommendationapp.R;
import nti.soft.aboalarbe.com.recommendationapp.Utilities.GridSpacingItemDecoration;
import nti.soft.aboalarbe.com.recommendationapp.Utilities.Utils;

public class HomeScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<String> list;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        list = new ArrayList<String>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(HomeScreen.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                GridSpacingItemDecoration.dpToPx(HomeScreen.this, 4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getItems();
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(HomeScreen.this)) {
            list.clear();
            refreshLayout.setRefreshing(true);
            getItems();
            refreshLayout.setRefreshing(false);
        } else {
            Toast.makeText(this, "Check your Internet Connection, Try again", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    private void getItems() {
        if (!(Utils.isConnected(HomeScreen.this))) {
            Toast.makeText(HomeScreen.this, "Check your Internet Connection,Try again", Toast.LENGTH_SHORT).show();
        } else {
            final String serverUrl = "http://recommendation.pythonanywhere.com/all_items";
            RequestQueue queue = Volley.newRequestQueue(HomeScreen.this);
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray itemsArray = object.getJSONArray("data");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONArray items = itemsArray.getJSONArray(i);
                            String item = items.getString(0);
                            list.add(item);
                        }
                        adapter = new ItemAdapter(HomeScreen.this, list, recyclerView);
                        recyclerView.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            refreshLayout.setRefreshing(false);
                            Toast.makeText(HomeScreen.this, "Unknown error in server, try again later", Toast.LENGTH_SHORT).show();
                        }
                    }) {
            };

            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 7000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 7000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            queue.add(request);
        }
    }

}
