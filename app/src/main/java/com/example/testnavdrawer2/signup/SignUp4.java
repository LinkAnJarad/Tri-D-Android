package com.example.testnavdrawer2.signup;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavdrawer2.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SignUp4 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<TableRowModel> tableRowList;

    private TextInputEditText tf_plate_number, tf_car_brand, tf_car_color;
    private TextInputLayout menu_car_type_layout;

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


}

