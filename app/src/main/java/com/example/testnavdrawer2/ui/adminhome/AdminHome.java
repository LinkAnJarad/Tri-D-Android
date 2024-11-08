package com.example.testnavdrawer2.ui.adminhome;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ApiConfig;
import com.example.testnavdrawer2.Login;
import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.VehicleEntry;
import com.example.testnavdrawer2.signup.SignUp4;
import com.example.testnavdrawer2.ui.LogAdapter;
import com.example.testnavdrawer2.ui.LogEntry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.testnavdrawer2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AdminHome extends Fragment implements View.OnClickListener {

    private AdminHomeViewModel mViewModel;

    private TextInputEditText tf_guest_name, tf_guest_platenumber, tf_guard_notes, tf_guest_email;
    private TextInputLayout menu_guest_vehicletype_layout, menu_idtype_layout, menu_reason_layout;
    private AutoCompleteTextView menu_guest_vehicletype, menu_idtype, menu_reason;
    private MaterialButton btn_pick_guestID, btn_submit_guest;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private Uri imageUri;
    private File imageFile;

    private LogAdapter logAdapter;
    private List<LogEntry> logList;
    private RecyclerView recyclerViewLogs;

    String guest_name, guest_platenumber, guard_notes, guest_email, guest_vehicletype, idtype, reason;
    String guest_ID_base64;

    Boolean retrieved_logs = false;

    public static AdminHome newInstance() {
        return new AdminHome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        btn_pick_guestID = view.findViewById(R.id.btn_pick_guestID);

        btn_pick_guestID.setOnClickListener(v -> openCamera());

        tf_guest_name = view.findViewById(R.id.tf_guest_name);
        tf_guest_platenumber = view.findViewById(R.id.tf_guest_platenumber);
        tf_guard_notes = view.findViewById(R.id.tf_guard_notes);
        tf_guest_email = view.findViewById(R.id.tf_guest_email);

        menu_reason_layout = (TextInputLayout) view.findViewById(R.id.menu_reason_layout);
        menu_reason = (AutoCompleteTextView) menu_reason_layout.getEditText();
        menu_guest_vehicletype_layout = (TextInputLayout) view.findViewById(R.id.menu_guest_vehicletype_layout);
        menu_guest_vehicletype = (AutoCompleteTextView) menu_guest_vehicletype_layout.getEditText();
        menu_idtype_layout = (TextInputLayout) view.findViewById(R.id.menu_idtype_layout);
        menu_idtype = (AutoCompleteTextView) menu_idtype_layout.getEditText();

        btn_submit_guest = view.findViewById(R.id.btn_submit_guest);
        btn_submit_guest.setOnClickListener(this);

        String[] reason_items = {"Reason1", "Reason2", "Reason3"};
        if (menu_reason instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) menu_reason).setSimpleItems(reason_items);
        }

        String[] guest_vehicletype_items = {"Sedan", "SUV", "Motorcycle"};
        if (menu_guest_vehicletype instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) menu_guest_vehicletype).setSimpleItems(guest_vehicletype_items);
        }

        String[] idtype_items = {"ID Type1", "ID Type2", "ID Type3"};
        if (menu_idtype instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) menu_idtype).setSimpleItems(idtype_items);
        }

        btn_submit_guest = view.findViewById(R.id.btn_submit_guest);

        recyclerViewLogs = view.findViewById(R.id.recyclerViewLogs);
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(requireContext()));

        logList = new ArrayList<>();
        logAdapter = new LogAdapter(logList);
        recyclerViewLogs.setAdapter(logAdapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminHomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void openCamera() {
        // Create an Intent to open the camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create a file to save the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(requireContext(),
                        "com.example.testnavdrawer2.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            if (imageUri != null) {
                // Use the imageUri (e.g., display it in an ImageView or upload it)
                guest_ID_base64 = imageUriToBase64(requireContext(), imageUri);
            }
        }
    }

    private void getGuestInfo() {
        guest_name = tf_guest_name.getText().toString();
        guest_platenumber = tf_guest_platenumber.getText().toString();
        guard_notes = tf_guard_notes.getText().toString();
        guest_email = tf_guest_email.getText().toString();
        guest_vehicletype = menu_guest_vehicletype.getText().toString();
        idtype = menu_idtype.getText().toString();
        reason = menu_reason.getText().toString();
    }

    public void getUserDetails() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = ApiConfig.BASE_URL + "gethomedetails2.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!retrieved_logs) {
                            try {
                                Log.d("ResponseLogs", "Response: " + response);
                                JSONObject json_response = new JSONObject(response);
                                String status = json_response.getString("status");
                                if (status.equals("success")) {

                                    retrieved_logs = true;

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
                params.put("user_type", "Admin");
                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void submitGuestInfo() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = ApiConfig.BASE_URL + "uploadguest.php";

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
                params.put("guest_name", guest_name);
                params.put("guest_platenumber", guest_platenumber);
                params.put("guest_email", guest_email);
                params.put("guard_notes", guard_notes);
                params.put("guest_vehicletype", guest_vehicletype);
                params.put("idtype", idtype);
                params.put("reason", reason);
                params.put("guest_ID_base64", guest_ID_base64);

                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();
                String date = currentDate.toString();  // Format as "YYYY-MM-DD"
                String time = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));  // Format as "HH:mm:ss"

                params.put("date", date);
                params.put("time", time);

                return params;
            }
        };
        queue.add(stringRequest);
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

    public static String imageUriToBase64(Context context, Uri imageUri) {
        try {
            // Step 1: Get InputStream from the Uri
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Step 2: Convert Bitmap to Byte Array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            // Step 3: Encode Byte Array to Base64
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null or handle error as appropriate
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit_guest) {
            getGuestInfo();
            submitGuestInfo();
        }
    }
}