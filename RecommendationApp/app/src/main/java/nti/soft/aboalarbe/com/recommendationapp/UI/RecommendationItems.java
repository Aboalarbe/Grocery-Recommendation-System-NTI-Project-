package nti.soft.aboalarbe.com.recommendationapp.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nti.soft.aboalarbe.com.recommendationapp.Adapters.RecommendAdapter;
import nti.soft.aboalarbe.com.recommendationapp.R;
import nti.soft.aboalarbe.com.recommendationapp.Utilities.GridSpacingItemDecoration;
import nti.soft.aboalarbe.com.recommendationapp.Utilities.Utils;

public class RecommendationItems extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> list;
    private RecommendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_items);
        setTitle("Recommended for You");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<String>();
        String selectedItem = getIntent().getExtras().getString("selectedItem");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RecommendationItems.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                GridSpacingItemDecoration.dpToPx(RecommendationItems.this, 4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        makeRecommendation(selectedItem);
    }

    private void makeRecommendation(final String selectedItem) {
        if (!(Utils.isConnected(RecommendationItems.this))) {
            Toast.makeText(RecommendationItems.this, "Check your Internet Connection,Try again", Toast.LENGTH_SHORT).show();
        } else {
            list = new ArrayList<String>();
            final String serverUrl = "http://recommendation.pythonanywhere.com/get_recommendation";
            RequestQueue queue = Volley.newRequestQueue(RecommendationItems.this);
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
                        adapter = new RecommendAdapter(RecommendationItems.this, list, recyclerView);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(RecommendationItems.this, "Unknown error in server, try again later", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("selectedItem", selectedItem);
                    map.put("noOfItems", 3 + "");
                    return map;
                }
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
