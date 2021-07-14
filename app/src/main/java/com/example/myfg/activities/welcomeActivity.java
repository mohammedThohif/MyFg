package com.example.myfg.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfg.R;
import com.google.firebase.auth.FirebaseAuth;

public class welcomeActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);


        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(welcomeActivity.this, MainActivity.class));
            Toast.makeText(this, "please wait you are logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void Signup(View view) {
        Intent intent = new Intent(welcomeActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void Login(View view) {
        Intent intent = new Intent(welcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
