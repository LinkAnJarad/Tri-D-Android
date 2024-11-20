package com.example.testnavdrawer2.ui.qr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import com.example.testnavdrawer2.UserVehiclesDataStore;
import com.example.testnavdrawer2.VehicleEntry;
import com.example.testnavdrawer2.databinding.FragmentQrBinding;
import com.example.testnavdrawer2.slotupdate_storage;
import com.example.testnavdrawer2.ui.LogEntry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class QRFragment extends Fragment {

    private FragmentQrBinding binding;

    private TextInputLayout menu_qr_vehicle_layout;
    private TextInputEditText tf_reason;
    private AutoCompleteTextView menu_qr_vehicle;

    private ImageView qrCodeImageView;
    private MaterialButton btn_generate_qr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRViewModel galleryViewModel =
                new ViewModelProvider(this).get(QRViewModel.class);

        binding = FragmentQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        qrCodeImageView = (ImageView) root.findViewById(R.id.qrCodeImageView);
        btn_generate_qr = (MaterialButton) root.findViewById(R.id.btn_generate_qr);


        btn_generate_qr.setOnClickListener(v -> generateQRCode());

        menu_qr_vehicle_layout = (TextInputLayout) root.findViewById(R.id.menu_qr_vehicle);
        menu_qr_vehicle = (AutoCompleteTextView) menu_qr_vehicle_layout.getEditText();
        tf_reason = (TextInputEditText) root.findViewById(R.id.tf_reason);

        String[] vehicle_items = UserVehiclesDataStore.user_vehicles;
        Log.d("vehicle_items", Arrays.toString(vehicle_items));

        if (menu_qr_vehicle instanceof MaterialAutoCompleteTextView) {
            // Filter out null values
            String[] filteredItems = Arrays.stream(vehicle_items)
                    .filter(Objects::nonNull)
                    .toArray(String[]::new);

            // Set the filtered array
            ((MaterialAutoCompleteTextView) menu_qr_vehicle).setSimpleItems(filteredItems);
        }

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    //Generate ng QRCode
    private void generateQRCode() {

        Intent intent = getActivity().getIntent();
        String user_id = intent.getStringExtra("USER_ID");
        String user_type = intent.getStringExtra("USER_TYPE");

        String vehicle = menu_qr_vehicle.getText().toString();
        String reason = tf_reason.getText().toString();
        String qrContent = user_id + "\n" + user_type + "\n" + vehicle + "\n" + reason;

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(qrContent, BarcodeFormat.QR_CODE, 400, 400);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
            slotupdate_storage.first_time_call = "yes";
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error generating QR code", Toast.LENGTH_SHORT).show();
        }


    }
}