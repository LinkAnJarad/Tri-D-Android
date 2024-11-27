package com.example.testnavdrawer2.signup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.ApiConfig;
import com.example.testnavdrawer2.Login;
import com.example.testnavdrawer2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

public class SignUp4 extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<TableRowModel> tableRowList;

    private TextInputEditText tf_plate_number, tf_car_brand, tf_car_color;
    private TextInputLayout menu_car_type_layout;
    private MaterialButton btn_submit;

    private String first_name, middle_name, last_name, telephone_number, address, gender, user_type;
    private String email, password, employee_id_number, employee_id_type, employee_id_Uri;
    private String student_id_number, college, student_id_Uri;
    String tableCarsJson;
    String employee_id_base64, student_id_base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] car_type_items = {"Sedan", "SUV", "Motorcycle"};

        TextInputLayout carTypeMenuLayout = findViewById(R.id.menu_car_type);
        AutoCompleteTextView gender_autoCompleteTextView = (AutoCompleteTextView) carTypeMenuLayout.getEditText();

        if (gender_autoCompleteTextView instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) gender_autoCompleteTextView).setSimpleItems(car_type_items);
        }

        tf_plate_number = (TextInputEditText) findViewById(R.id.tf_plate_number);
        tf_car_brand = (TextInputEditText) findViewById(R.id.tf_car_brand);
        tf_car_color = (TextInputEditText) findViewById(R.id.tf_car_color);
        menu_car_type_layout = (TextInputLayout) findViewById(R.id.menu_car_type);
        AutoCompleteTextView menu_car_type = (AutoCompleteTextView) menu_car_type_layout.getEditText();

        btn_submit = (MaterialButton) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


        recyclerView = findViewById(R.id.recyclerView);
        tableRowList = new ArrayList<>();
        tableAdapter = new TableAdapter(tableRowList, position -> {
            tableAdapter.deleteRow(position);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);

        // Example: Add a row dynamically
        findViewById(R.id.addButton).setOnClickListener(v -> {

            String plate_number = tf_plate_number.getText().toString();
            String car_brand = tf_car_brand.getText().toString();
            String car_color = tf_car_color.getText().toString();
            String car_type = menu_car_type.getText().toString();

            TableRowModel newRow = new TableRowModel(plate_number, car_type, car_brand, car_color);
            tableAdapter.addRow(newRow);
        });


    }

    // Method to convert the tableRowList to JSON
    private String getTableDataAsJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(tableRowList);  // Convert the list to JSON
    }

    private void submitUserInfo() {

        handleSSLHandshake();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiConfig.BASE_URL + "signup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response", response);
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            if (status.equals("success")) {
                                Intent intent = new Intent(SignUp4.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUp4.this, "Submit failed: " + status , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON Parsing", response);
                            Toast.makeText(SignUp4.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp4.this, error + "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                params.put("first_name", first_name);
                params.put("middle_name", middle_name);
                params.put("last_name", last_name);
                params.put("telephone_number", telephone_number);
                params.put("address", address);
                params.put("gender", gender);
                params.put("user_type", user_type);

                if (user_type.equals("Employee")) {
                    params.put("email", email);
                    params.put("password", password);
                    params.put("employee_id_number", employee_id_number);
                    params.put("employee_id_type", employee_id_type);
                    params.put("employee_id_base64", employee_id_base64);
                } else {
                    params.put("email", email);
                    params.put("password", password);
                    params.put("student_id_number", student_id_number);
                    params.put("college", college);
                    params.put("student_id_base64", student_id_base64);
                }

                Log.d("TABLECARS", tableCarsJson);
                params.put("tableCarsJson", tableCarsJson);

                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
        if (view.getId() == R.id.btn_submit) {
            Intent previous_intent = getIntent();
            Bundle bundle = previous_intent.getExtras();
            if (bundle != null) {
                first_name = bundle.getString("FIRST_NAME");
                middle_name = bundle.getString("MIDDLE_NAME");
                last_name = bundle.getString("LAST_NAME");
                telephone_number = bundle.getString("TELEPHONE_NUMBER");
                address = bundle.getString("ADDRESS");
                gender = bundle.getString("GENDER");
                user_type = bundle.getString("USER_TYPE");

                if (user_type.equals("Employee")) {
                    email = bundle.getString("EMAIL");
                    password = bundle.getString("PASSWORD");
                    employee_id_number = bundle.getString("EMPLOYEE_ID_NUMBER");
                    employee_id_type = bundle.getString("EMPLOYEE_ID_TYPE");
                    employee_id_Uri = bundle.getString("EMPLOYEE_ID_URI");
                    Uri employee_id_Uri_uri = Uri.parse(employee_id_Uri);
                    employee_id_base64 = imageUriToBase64(this, employee_id_Uri_uri);

                } else {
                    email = bundle.getString("EMAIL");
                    password = bundle.getString("PASSWORD");
                    student_id_number = bundle.getString("STUDENT_ID_NUMBER");
                    college = bundle.getString("COLLEGE");
                    student_id_Uri = bundle.getString("STUDENT_ID_URI");
                    Uri student_id_Uri_uri = Uri.parse(student_id_Uri);
                    student_id_base64 = imageUriToBase64(this, student_id_Uri_uri);
                }

            }

            tableCarsJson = getTableDataAsJson();
            Log.d("Table JSON", tableCarsJson);

            submitUserInfo();

        }



    }
}

