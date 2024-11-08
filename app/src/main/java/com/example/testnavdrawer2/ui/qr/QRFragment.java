package com.example.testnavdrawer2.ui.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.databinding.FragmentQrBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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


        String[] test_items = {"Item1", "Item2"};

        if (menu_qr_vehicle instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) menu_qr_vehicle).setSimpleItems(test_items);
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
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }
}