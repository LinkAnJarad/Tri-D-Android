package com.example.testnavdrawer2.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ApiConfig;
import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.ui.CardAdapter;
import com.example.testnavdrawer2.ui.CardData;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Base64;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardData> cardDataList;
    private Boolean retrieved_history = false;


    private void filter(String query) {
        if (cardAdapter != null) {
            cardAdapter.getFilter().filter(query);
        }
    }


    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);

        // Initialize RecyclerView using 'view'
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize data and adapter
        cardDataList = new ArrayList<>();
        cardAdapter = new CardAdapter(getContext(), cardDataList);
        recyclerView.setAdapter(cardAdapter);

        getUserHistory();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) {
        String cleanedBase64String = base64Str.replaceAll("\\s+", "");
        byte[] decodedBytes = Base64.decode(cleanedBase64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void getUserHistory() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = ApiConfig.BASE_URL + "gethistory.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("ResponseHistory", response);
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            if (status.equals("success")) {

                                // Get the logs array
                                JSONArray logsArray = json_response.getJSONArray("history");

                                // Parse each log entry
                                for (int i = 0; i < logsArray.length(); i++) {
                                    JSONObject historyObject = logsArray.getJSONObject(i);

                                    //Bitmap qrCodeBitmap = decodeBase64ToBitmap(historyObject.getString("qr"));
                                    // use drawable/test_qr.png as resource first for testing
                                    Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_qr);

                                    String time_out = historyObject.getString("time_out");
                                    boolean in_use = time_out.equals("");

                                    CardData cardEntry = new CardData(
                                            historyObject.getString("type"),
                                            historyObject.getString("plate_number"),
                                            historyObject.getString("slot_number"),
                                            historyObject.getString("date"),
                                            qrCodeBitmap,
                                            in_use
                                            );

                                    if (!retrieved_history) {
                                        cardAdapter.addCard(cardEntry);
                                    }

                                }
                                retrieved_history = true;


                            } else {
                                Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error + "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                Intent previous_intent = requireActivity().getIntent();
                String user_id = previous_intent.getStringExtra("USER_ID");
                String user_type = previous_intent.getStringExtra("USER_TYPE");

                params.put("user_type", user_type);
                params.put("user_id", user_id);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);



    }

}