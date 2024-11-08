package com.example.testnavdrawer2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testnavdrawer2.signup.SignUp1;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button login_btn;
    private Button signup_btn;
    private TextInputEditText tf_email, tf_password;

    private boolean has_logged_in = false;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_btn = (Button) findViewById(R.id.login_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        tf_email = (TextInputEditText) findViewById(R.id.tf_email);
        tf_password = (TextInputEditText) findViewById(R.id.tf_password);

        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
    }

    private void loginUser() {

        handleSSLHandshake();

        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ApiConfig.BASE_URL + "login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject json_response = new JSONObject(response);
                            String status = json_response.getString("status");
                            String user_type = json_response.getString("user_type");
                            String user_id = json_response.getString("user_id");
                            if (status.equals("success")) {

                                if (user_type.equals("student") || user_type.equals("employee")) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("USER_ID", user_id);
                                    intent.putExtra("USER_TYPE", user_type);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Login.this, AdminActivity.class);
                                    intent.putExtra("USER_ID", user_id);
                                    intent.putExtra("USER_TYPE", user_type);
                                    startActivity(intent);
                                }


                            } else {
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error + "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Send email and password as POST parameters
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        queue.add(stringRequest);

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
    public void onClick(View view) {

        if (view.getId() == R.id.login_btn) {
            loginUser();
            //Intent intent = new Intent(Login.this, MainActivity.class);
            //startActivity(intent);
        } else if (view.getId() == R.id.signup_btn) {
            Intent intent = new Intent(Login.this, SignUp1.class);
            startActivity(intent);
        }
    }
}