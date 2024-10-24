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
import com.google.android.material.textfield.TextInputLayout;

public class SignUp1 extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btn_next_signup1;

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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next_signup1) {
            Intent intent = new Intent(SignUp1.this, SignUp2.class);
            startActivity(intent);
        }
    }
}