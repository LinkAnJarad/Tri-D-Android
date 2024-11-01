package com.example.testnavdrawer2.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.VehicleAdapter;
import com.example.testnavdrawer2.VehicleEntry;
import com.example.testnavdrawer2.databinding.FragmentHomeBinding;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private VehicleAdapter vehicleAdapter;
    private List<VehicleEntry> vehicleList;
    RecyclerView recyclerViewVehicles;

    private LogAdapter logAdapter;
    private List<LogEntry> logList;
    private RecyclerView recyclerViewLogs;

    private Boolean retrieved_data = false;

    private ImageView img_qr_1, img_qr_2, img_qr_3, imageProfile;
    private TextView lbl_qr_date_1, lbl_qr_date_2, lbl_qr_date_3;
    private TextView lbl_user_fullname, lbl_user_id_number, lbl_college, lbl_email, lbl_phone_number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        // Use binding to inflate the layout
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Use binding to find the RecyclerView
        recyclerViewVehicles = root.findViewById(R.id.recyclerViewVehicles);
        recyclerViewVehicles.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // Initialize list and adapter
        vehicleList = new ArrayList<>();
        vehicleAdapter = new VehicleAdapter(requireActivity(), vehicleList);
        recyclerViewVehicles.setAdapter(vehicleAdapter);

        // Add divider
        recyclerViewVehicles.addItemDecoration(
                new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        recyclerViewLogs = root.findViewById(R.id.recyclerViewLogs);
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(requireContext()));

        logList = new ArrayList<>();
        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);

        img_qr_1 = root.findViewById(R.id.img_qr_1);
        img_qr_2 = root.findViewById(R.id.img_qr_2);
        img_qr_3 = root.findViewById(R.id.img_qr_3);
        imageProfile = root.findViewById(R.id.imageProfile);

        lbl_qr_date_1 = root.findViewById(R.id.lbl_qr_date_1);
        lbl_qr_date_2 = root.findViewById(R.id.lbl_qr_date_2);
        lbl_qr_date_3 = root.findViewById(R.id.lbl_qr_date_3);

        lbl_user_fullname = root.findViewById(R.id.lbl_user_fullname);
        lbl_user_id_number = root.findViewById(R.id.lbl_user_id_number);
        lbl_college = root.findViewById(R.id.lbl_college);
        lbl_email = root.findViewById(R.id.lbl_email);
        lbl_phone_number = root.findViewById(R.id.lbl_phone_number);

        getUserDetails();

        return root;


    }

    private void addSampleLogs() {
        LogEntry log1 = new LogEntry("09/02/24", "12:03 PM", "1:03 PM");
        LogEntry log2 = new LogEntry("09/02/24", "9:43 AM", "10:34 PM");

        logAdapter.addLog(log1);
        logAdapter.addLog(log2);

    }

    private void addSampleVehicles() {
        vehicleAdapter.addVehicle(new VehicleEntry("ABC123", "Sedan", "Toyota", "Black", true));
        vehicleAdapter.addVehicle(new VehicleEntry("XYZ789", "SUV", "Honda", "White", false));
    }

    public void getUserDetails() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url ="https://192.168.254.118/gethomedetails2.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!retrieved_data) {
                            try {
                                Log.d("ResponseLogs", "Response: " + response);
                                JSONObject json_response = new JSONObject(response);
                                String status = json_response.getString("status");
                                if (status.equals("success")) {
                                    // Get the data object
                                    JSONObject data = json_response.getJSONObject("data");

                                    // Parse user info
                                    JSONObject userInfo = data.getJSONObject("user_info");
                                    String firstName = userInfo.getString("first_name");
                                    String middleName = userInfo.getString("middle_name");
                                    String lastName = userInfo.getString("last_name");
                                    String email = userInfo.getString("email");
                                    String telephone = userInfo.getString("telephone");
                                    String employeeNumber = userInfo.getString("employee_number");

                                    lbl_user_fullname.setText(firstName + " " + middleName + " " + lastName);
                                    lbl_user_id_number.setText("ID#: " + employeeNumber);
                                    lbl_college.setText("CEIS");
                                    lbl_email.setText(email);
                                    lbl_phone_number.setText(telephone);

                                    // Parse logs array
                                    JSONArray logsArray = data.getJSONArray("logs");
                                    ArrayList<HashMap<String, String>> logsList = new ArrayList<>();

                                    for (int i = 0; i < logsArray.length(); i++) {
                                        JSONObject log = logsArray.getJSONObject(i);
                                        LogEntry newlog = new LogEntry(log.getString("date"), log.getString("time_in"), log.getString("time_out"));
                                        logAdapter.addLog(newlog);

                                    }

                                    String base64qr_1 = logsArray.getJSONObject(0).getString("qr");
                                    Bitmap qr1 = decodeBase64ToBitmap(base64qr_1);
                                    img_qr_1.setImageBitmap(qr1);

                                    String base64qr_2 = logsArray.getJSONObject(1).getString("qr");
                                    Bitmap qr2 = decodeBase64ToBitmap(base64qr_2);
                                    img_qr_2.setImageBitmap(qr2);

                                    String base64qr_3 = logsArray.getJSONObject(2).getString("qr");
                                    Bitmap qr3 = decodeBase64ToBitmap(base64qr_3);
                                    img_qr_3.setImageBitmap(qr3);

                                    String date_1 = logsArray.getJSONObject(0).getString("date");
                                    lbl_qr_date_1.setText(date_1);
                                    String date_2 = logsArray.getJSONObject(1).getString("date");
                                    lbl_qr_date_2.setText(date_2);
                                    String date_3 = logsArray.getJSONObject(2).getString("date");
                                    lbl_qr_date_3.setText(date_3);


                                    // Parse vehicles array
                                    JSONArray vehiclesArray = data.getJSONArray("vehicles");
                                    ArrayList<HashMap<String, String>> vehiclesList = new ArrayList<>();

                                    for (int i = 0; i < vehiclesArray.length(); i++) {
                                        JSONObject vehicle = vehiclesArray.getJSONObject(i);

                                        Boolean isverified = vehicle.getString("verified").equals("Verified");

                                        vehicleAdapter.addVehicle(new VehicleEntry(
                                                vehicle.getString("plate_number"),
                                                vehicle.getString("type"),
                                                vehicle.getString("brand"),
                                                vehicle.getString("color"),
                                                isverified
                                        ));
                                    }

                                    retrieved_data = true;

                                } else {
                                    Toast.makeText(getActivity(), "Loading failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                            }
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

    public Bitmap decodeBase64ToBitmap(String base64Str) {
        String cleanedBase64String = base64Str.replaceAll("\\s+", "");
        byte[] decodedBytes = Base64.decode(cleanedBase64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}