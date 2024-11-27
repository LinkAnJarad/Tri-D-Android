package com.example.testnavdrawer2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ui.LogEntry;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testnavdrawer2.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_account, R.id.nav_qr, R.id.nav_logs, R.id.nav_history)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Make a network request to fetch data

                if (!slotupdate_storage.my_slot_number.equals("")) { // Add your stopping condition here
                    handler.removeCallbacks(this); // Stop the Runnable
                    Log.d("Runnable", "Updates stopped");
                    return; // Exit the current execution
                }


                if (slotupdate_storage.first_time_call.equals("yes")) {
                    getSlotUpdate();

                } else if (slotupdate_storage.first_time_call.equals("no")) {
                    getSlotUpdate();
                }
                //Toast.makeText(MainActivity.this, "Counter: " + counter, Toast.LENGTH_SHORT).show();

                // Schedule next run
                handler.postDelayed(this, 7000); //
            }
        };
        handler.post(runnable);
    }


    public void getSlotUpdate() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiConfig.BASE_URL + "slotnotification.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("ResponseLogs", "Response: " + response);
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            if (status.equals("success")) {

                                String slot_number = json_response.getString("slot_number");
                                String history_id = json_response.getString("history_id");
                                Log.d("HistoryID_inside_2", history_id);
                                if (!history_id.equals("NA")) {
                                    Log.d("HistoryID_inside", history_id);
                                    slotupdate_storage.my_history_id = history_id;
                                }

                                Log.d("SLOTNUM", slot_number);
                                if (!slot_number.equals("NA") && !slot_number.equals("")) {
                                    Toast.makeText(MainActivity.this, "Your slot is: " + slot_number, Toast.LENGTH_SHORT).show();
                                    slotupdate_storage.my_slot_number = slot_number;
                                }

                            } else {
                                //Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(MainActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters

                Intent intent = getIntent();
                String user_id = intent.getStringExtra("USER_ID");

                Log.d("FirstCall", slotupdate_storage.first_time_call);
                Log.d("HistoryID", slotupdate_storage.my_history_id);
                params.put("first_time_call", slotupdate_storage.first_time_call);
                params.put("user_id", user_id);
                params.put("history_id", slotupdate_storage.my_history_id);
                slotupdate_storage.first_time_call = "no";
                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}