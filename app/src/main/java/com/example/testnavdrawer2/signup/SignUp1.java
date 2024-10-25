package com.example.testnavdrawer2.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testnavdrawer2.Login;
import com.example.testnavdrawer2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp1 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_next_signup1;
    private TextInputEditText tf_first_name, tf_middle_name, tf_last_name, tf_telephone_number, tf_address;
    private TextInputLayout menu_gender_layout, menu_user_type_layout;
    private AutoCompleteTextView menu_gender, menu_user_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // Setting menu items of SignUp1

        String[] gender_items = {"Male", "Female"};

        TextInputLayout genderMenuLayout = findViewById(R.id.menu_gender);
        AutoCompleteTextView gender_autoCompleteTextView = (AutoCompleteTextView) genderMenuLayout.getEditText();

        if (gender_autoCompleteTextView instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) gender_autoCompleteTextView).setSimpleItems(gender_items);
        }

        String[] user_type_items = {"Employee", "Student"};

        TextInputLayout user_type_menuLayout = findViewById(R.id.menu_user_type);
        AutoCompleteTextView user_type_autoCompleteTextView = (AutoCompleteTextView) user_type_menuLayout.getEditText();

        if (user_type_autoCompleteTextView instanceof MaterialAutoCompleteTextView) {
            ((MaterialAutoCompleteTextView) user_type_autoCompleteTextView).setSimpleItems(user_type_items);
        }

        btn_next_signup1 = (MaterialButton) findViewById(R.id.btn_next_signup1);
        btn_next_signup1.setOnClickListener(this);

        tf_first_name = (TextInputEditText) findViewById(R.id.tf_first_name);
        tf_middle_name = (TextInputEditText) findViewById(R.id.tf_middle_name);
        tf_last_name = (TextInputEditText) findViewById(R.id.tf_last_name);
        tf_telephone_number = (TextInputEditText) findViewById(R.id.tf_telephone_number);
        tf_address = (TextInputEditText) findViewById(R.id.tf_address);
        menu_gender_layout = (TextInputLayout) findViewById(R.id.menu_gender);
        menu_gender = (AutoCompleteTextView) menu_gender_layout.getEditText();
        menu_user_type_layout = (TextInputLayout) findViewById(R.id.menu_user_type);
        menu_user_type = (AutoCompleteTextView) menu_user_type_layout.getEditText();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next_signup1) {
            String first_name = tf_first_name.getText().toString();
            String middle_name = tf_middle_name.getText().toString();
            String last_name = tf_last_name.getText().toString();
            String telephone_number = tf_telephone_number.getText().toString();
            String address = tf_address.getText().toString();
            String gender = menu_gender.getText().toString();
            String user_type = menu_user_type.getText().toString();

            if (user_type.equals("Employee")) {
                Intent intent = new Intent(SignUp1.this, SignUp2.class);

                intent.putExtra("FIRST_NAME", first_name);
                intent.putExtra("MIDDLE_NAME", middle_name);
                intent.putExtra("LAST_NAME", last_name);
                intent.putExtra("TELEPHONE_NUMBER", telephone_number);
                intent.putExtra("ADDRESS", address);
                intent.putExtra("GENDER", gender);
                intent.putExtra("USER_TYPE", user_type);

                startActivity(intent);

            } else if (user_type.equals("Student")) {
                Intent intent = new Intent(SignUp1.this, SignUp3.class);

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
}