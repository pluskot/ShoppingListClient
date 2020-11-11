package com.example.patryk.shoppinglist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.patryk.shoppinglist.R;

public class LandingPageActivity extends AppCompatActivity {

    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        initComponents();
    }

    private void initComponents() {
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> onLoginButtonClick());
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> onRegisterButtonClick());

    }

    private void onLoginButtonClick() {
        Intent loginView = new Intent(this, LoginActivity.class);
        startActivity(loginView);
    }

    private void onRegisterButtonClick() {
        Intent registerView = new Intent(this, RegisterActivity.class);
        startActivity(registerView);
    }
}
