package com.example.testnavdrawer2.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testnavdrawer2.R;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUp2 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_pick_employeeID, btn_pick_governmentID, btn_pick_drivers_license;
    private ImageView iv_employee_id, iv_government_id, iv_drivers_license;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public interface ImagePickCallback {
        void onImagePicked(Uri imageUri);
    }

    private ImagePickCallback imagePickCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            if (imagePickCallback != null) {
                                imagePickCallback.onImagePicked(selectedImageUri);
                            }
                        }
                    }
                });




        btn_pick_employeeID = (MaterialButton) findViewById(R.id.btn_pick_employeeID);
        iv_employee_id = (ImageView) findViewById(R.id.iv_employee_id);
        btn_pick_employeeID.setOnClickListener(this);

        btn_pick_governmentID = (MaterialButton) findViewById(R.id.btn_pick_governmentID);
        iv_government_id = (ImageView) findViewById(R.id.iv_government_id);
        btn_pick_governmentID.setOnClickListener(this);

        btn_pick_drivers_license = (MaterialButton) findViewById(R.id.btn_pick_drivers_license);
        iv_drivers_license = (ImageView) findViewById(R.id.iv_drivers_license);
        btn_pick_drivers_license.setOnClickListener(this);
    }

    // Function to trigger image picker
    public void pickImage(ImagePickCallback callback) {
        this.imagePickCallback = callback;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    public String imageUriToBase64(Context context, Uri uri) {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pick_employeeID) {
            pickImage(uri -> {
                iv_employee_id.setImageURI(uri);
            });
        } else if (view.getId() == R.id.btn_pick_governmentID) {
            pickImage(uri -> {
                iv_government_id.setImageURI(uri);
            });
        } else if (view.getId() == R.id.btn_pick_drivers_license) {
            pickImage(uri -> {
                iv_drivers_license.setImageURI(uri);
            });
        }
    }
}