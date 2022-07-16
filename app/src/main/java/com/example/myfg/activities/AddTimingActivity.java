package com.example.myfg.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfg.MyCartsFragment;
import com.example.myfg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTimingActivity extends AppCompatActivity {

    EditText name, address, city, phoneNumber;
    Toolbar toolbar;
    Button addAddressBtn,goto_cart;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timing);

        toolbar = findViewById(R.id.add_address_toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.ad_name1);
        address = findViewById(R.id.ad_address1);
        city = findViewById(R.id.ad_city1);
        phoneNumber = findViewById(R.id.ad_phone1);
        addAddressBtn = findViewById(R.id.ad_add_address1);




        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = name.getText().toString();
                String userCity = address.getText().toString();
                String userAddress = city.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                String final_adress = " ";

                if (!userName.isEmpty()) {
                    final_adress += " " + userName;
                }
                if (!userCity.isEmpty()) {
                    final_adress += " " + userCity;
                }
                if (!userAddress.isEmpty()) {
                    final_adress += " " + userAddress;
                }
                if (!userNumber.isEmpty()) {
                    final_adress += " " + userNumber;
                }
                if (!userName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userNumber.isEmpty()) {

                    Map<String, String> map = new HashMap<>();
                    map.put("userAddress", final_adress);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("DineIn").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddTimingActivity.this, "Slot Added", Toast.LENGTH_SHORT).show();

//                                Intent intent = new Intent(AddAddressActivity.this, MyCartsFragment.class);
//                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddTimingActivity.this, "Kindly Fill All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}