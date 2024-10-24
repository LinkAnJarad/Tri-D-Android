package com.example.testnavdrawer2.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUp3 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_pick_student_ID, btn_next_signup3;
    private ImageView iv_student_id;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    public interface ImagePickCallback {
        void onImagePicked(Uri imageUri);
    }

    private ImagePickCallback imagePickCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up3);
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


        String[] college_items = {"CEIS", "CHTM", "SLCN", "CMT", "CBMA", "CAHS", "CASE"};

        TextInputLayout genderMenuLayout = findViewById(R.id.menu_college);
        AutoCompleteTextView gender_autoCompleteTextView = (AutoCompleteTextView) genderMenuLayout.getEditText();

        if (gender_autoCompleteTextView instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) gender_autoCompleteTextView).setSimpleItems(college_items);
        }

        btn_pick_student_ID = (MaterialButton) findViewById(R.id.btn_pick_student_ID);
        btn_pick_student_ID.setOnClickListener(this);

        iv_student_id = (ImageView) findViewById(R.id.iv_student_id);
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
        if (view.getId() == R.id.btn_pick_student_ID) {
            pickImage(uri -> {
                iv_student_id.setImageURI(uri);
            });
        }
    }
}