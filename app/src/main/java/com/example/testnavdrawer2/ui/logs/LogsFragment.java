package com.example.testnavdrawer2.ui.logs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ApiConfig;
import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.databinding.FragmentLogsBinding;
import com.example.testnavdrawer2.ui.LogAdapter;
import com.example.testnavdrawer2.ui.LogEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LogsFragment extends Fragment {

    private LogAdapter logAdapter;
    private List<LogEntry> logList;
    private RecyclerView recyclerView;



    private FragmentLogsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(LogsViewModel.class);

        binding = FragmentLogsBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        logList = new ArrayList<>();
        logAdapter = new LogAdapter(logList);
        recyclerView.setAdapter(logAdapter);

        // Example of adding a new log
        getUserLogs();

        return view;
    }

    private void addSampleLogs() {
        LogEntry log1 = new LogEntry("09/02/24", "12:03 PM", "Exit Gate");
        LogEntry log2 = new LogEntry("09/02/24", "9:43 AM", "Main Entrance");

        logAdapter.addLog(log1);
        logAdapter.addLog(log2);

    }

    public void getUserLogs() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = ApiConfig.BASE_URL + "getlogs.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ResponseLogs", response);
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            if (status.equals("success")) {

                                // Clear existing logs
                                logList.clear();

                                // Get the logs array
                                JSONArray logsArray = json_response.getJSONArray("logs");

                                // Parse each log entry
                                for (int i = 0; i < logsArray.length(); i++) {
                                    JSONObject logObject = logsArray.getJSONObject(i);

                                    LogEntry logEntry = new LogEntry(
                                            logObject.getString("date"),
                                            logObject.getString("time_in"),
                                            logObject.getString("time_out")
                                    );

                                    logAdapter.addLog(logEntry);
                                }

                                // Notify adapter of data change
                                logAdapter.notifyDataSetChanged();

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
                params.put("user_type", "Employee");
                params.put("user_id", "0");
                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}