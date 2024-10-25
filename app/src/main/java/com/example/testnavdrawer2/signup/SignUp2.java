package com.example.testnavdrawer2.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SignUp2 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_pick_employeeID, btn_next_signup2;
    private ImageView iv_employee_id;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private TextInputEditText tf_signup_email, tf_signup_password, tf_employee_id_number;
    private TextInputLayout menu_employee_id_type_layout;
    private AutoCompleteTextView menu_employee_id_type;
    private String employee_id_Uri;
    private String first_name, middle_name, last_name, telephone_number, address, gender, user_type;

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


        String[] gender_items = {"Employee ID Card", "Government ID", "Drivers License"};

        TextInputLayout employeeIDTypeMenuLayout = findViewById(R.id.menu_employee_id_type);
        AutoCompleteTextView gender_autoCompleteTextView = (AutoCompleteTextView) employeeIDTypeMenuLayout.getEditText();

        if (gender_autoCompleteTextView instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) gender_autoCompleteTextView).setSimpleItems(gender_items);
        }

        btn_pick_employeeID = (MaterialButton) findViewById(R.id.btn_pick_employeeID);
        iv_employee_id = (ImageView) findViewById(R.id.iv_employee_id);
        btn_pick_employeeID.setOnClickListener(this);

        btn_next_signup2 = (MaterialButton) findViewById(R.id.btn_next_signup2);
        btn_next_signup2.setOnClickListener(this);

        tf_signup_email = (TextInputEditText) findViewById(R.id.tf_signup_email);
        tf_signup_password = (TextInputEditText) findViewById(R.id.tf_signup_password);
        tf_employee_id_number = (TextInputEditText) findViewById(R.id.tf_employee_id_number);
        menu_employee_id_type_layout = (TextInputLayout) findViewById(R.id.menu_employee_id_type);
        menu_employee_id_type = (AutoCompleteTextView) menu_employee_id_type_layout.getEditText();
    }

    // Function to trigger image picker
    public void pickImage(ImagePickCallback callback) {
        this.imagePickCallback = callback;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pick_employeeID) {
            pickImage(uri -> {
                iv_employee_id.setImageURI(uri);
                //Toast.makeText(SignUp2.this, uri.toString(), Toast.LENGTH_SHORT).show();
                employee_id_Uri = uri.toString();
                //String uri_length = String.valueOf(base64_employee_id_Uri.length());
                //Toast.makeText(SignUp2.this, uri_length, Toast.LENGTH_SHORT).show();
            });
        } else if (view.getId() == R.id.btn_next_signup2) {

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
            }

            Intent intent = new Intent(SignUp2.this, SignUp4.class);

            String email = tf_signup_email.getText().toString();
            String password = tf_signup_password.getText().toString();
            String employee_id_number = tf_employee_id_number.getText().toString();
            String employee_id_type = menu_employee_id_type.getText().toString();

            intent.putExtra("FIRST_NAME", first_name);
            intent.putExtra("MIDDLE_NAME", middle_name);
            intent.putExtra("LAST_NAME", last_name);
            intent.putExtra("TELEPHONE_NUMBER", telephone_number);
            intent.putExtra("ADDRESS", address);
            intent.putExtra("GENDER", gender);
            intent.putExtra("USER_TYPE", user_type);

            intent.putExtra("EMAIL", email);
            intent.putExtra("PASSWORD", password);
            intent.putExtra("EMPLOYEE_ID_NUMBER", employee_id_number);
            intent.putExtra("EMPLOYEE_ID_TYPE", employee_id_type);
            intent.putExtra("EMPLOYEE_ID_URI", employee_id_Uri);

            startActivity(intent);

        }
    }
}