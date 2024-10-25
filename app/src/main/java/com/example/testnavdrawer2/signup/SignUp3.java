package com.example.testnavdrawer2.signup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class SignUp3 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_pick_student_ID, btn_next_signup3;
    private ImageView iv_student_id;

    private TextInputEditText tf_signup_email, tf_signup_password, tf_student_ID_number;
    private TextInputLayout menu_college_layout;
    private AutoCompleteTextView menu_college;
    private String student_id_Uri;
    private String first_name, middle_name, last_name, telephone_number, address, gender, user_type;

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
        btn_next_signup3 = (MaterialButton) findViewById(R.id.btn_next_signup3);
        btn_next_signup3.setOnClickListener(this);

        iv_student_id = (ImageView) findViewById(R.id.iv_student_id);

        tf_signup_email = (TextInputEditText) findViewById(R.id.tf_signup_email);
        tf_signup_password = (TextInputEditText) findViewById(R.id.tf_signup_password);
        tf_student_ID_number = (TextInputEditText) findViewById(R.id.tf_student_ID_number);
        menu_college_layout = (TextInputLayout) findViewById(R.id.menu_college);
        menu_college = (AutoCompleteTextView) menu_college_layout.getEditText();
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
        if (view.getId() == R.id.btn_pick_student_ID) {
            pickImage(uri -> {
                iv_student_id.setImageURI(uri);
                student_id_Uri = uri.toString();
            });
        } else if (view.getId() == R.id.btn_next_signup3) {
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

            Intent intent = new Intent(SignUp3.this, SignUp4.class);

            String email = tf_signup_email.getText().toString();
            String password = tf_signup_password.getText().toString();
            String student_id_number = tf_student_ID_number.getText().toString();
            String college = menu_college.getText().toString();

            intent.putExtra("EMAIL", email);
            intent.putExtra("PASSWORD", password);
            intent.putExtra("STUDENT_ID_NUMBER", student_id_number);
            intent.putExtra("COLLEGE", college);
            intent.putExtra("STUDENT_ID_URI", student_id_Uri);

            intent.putExtra("FIRST_NAME", first_name);
            intent.putExtra("MIDDLE_NAME", middle_name);
            intent.putExtra("LAST_NAME", last_name);
            intent.putExtra("TELEPHONE_NUMBER", telephone_number);
            intent.putExtra("ADDRESS", address);
            intent.putExtra("GENDER", gender);
            intent.putExtra("USER_TYPE", user_type);

            startActivity(intent);
        }
    }
}