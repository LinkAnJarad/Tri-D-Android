package com.example.testnavdrawer2.ui.qradmin;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ApiConfig;
import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.databinding.FragmentQradminBinding;
import com.example.testnavdrawer2.slotupdate_storage;
import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class QRAdminFragment extends Fragment {

    private FragmentQradminBinding binding;

    private MaterialButton btnScan;
    private TextView tv_user_id;

    String user_id, user_type, vehicle, reason, date, time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRAdminViewModel galleryViewModel =
                new ViewModelProvider(this).get(QRAdminViewModel.class);

        binding = FragmentQradminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnScan = root.findViewById(R.id.btnScan);
        tv_user_id = root.findViewById(R.id.tv_user_id);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the QR code scanner
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) { //Display yung Result ng License Driver
            // kukunin yung result ng qrcode
            String result = data.getStringExtra("SCAN_RESULT");

            String result_split[] = result.split("\n");

            user_id = result_split[0];
            user_type = result_split[1];
            vehicle = result_split[2];
            reason = result_split[3];
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            // Format the date and time as strings
            date = currentDate.toString();  // Format as "YYYY-MM-DD"
            time = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));  // Format as "HH:mm:ss"

            tv_user_id.setText(result); // Set scanned result to TextView
            submitQR();
        }
    }

    private void submitQR() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = ApiConfig.BASE_URL + "submitqr.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response", response);
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "Submit failed: " + status , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON Parsing", response);
                            Toast.makeText(getContext(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error + "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                params.put("user_id", user_id);
                params.put("user_type", user_type);
                params.put("vehicle", vehicle);
                params.put("reason", reason);
                params.put("date", date);
                params.put("time", time);

                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}