package nti.soft.aboalarbe.com.recommendationapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by m_abo on 11/26/2018.
 */

public class Utils {
    /*
        this method check for internet connection
         */
    public static boolean isConnected(Activity activity) {
        ConnectivityManager conn = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info;
        info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnectedOrConnecting());
    }

    /*
   this method get the plant image from uri then load it in background
    */
    public static void loadItemImage(final Activity activity, ImageView imageView, String photoUrl) {
        Glide.with(activity)
                .load(photoUrl)

                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Toast.makeText(activity, "Failed to load the image", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    private void getItems(final Activity activity, String apiUrl) {
        if (!(Utils.isConnected(activity))) {
            Toast.makeText(activity, "Check your Internet Connection,Try again", Toast.LENGTH_SHORT).show();
        } else {
             ArrayList<String>items =new ArrayList<String>();
             final String serverUrl = apiUrl;
            RequestQueue queue = Volley.newRequestQueue(activity);
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(activity, "Unknown error in server, try again later", Toast.LENGTH_SHORT).show();
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
