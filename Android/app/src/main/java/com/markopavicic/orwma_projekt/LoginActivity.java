package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private ProgressBar progressBar;
    private EditText etUsername, etPassword;
    private Button btnSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Intent i = new Intent(LoginActivity.this, WelcomeSplash.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register: {
                startActivity(new Intent(this, RegisterUser.class));
                finish();
            }
            break;
            case R.id.btnSignIn:
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    etUsername.setError(getString(R.string.errorEmptyFields));
                    etPassword.setError(getString(R.string.errorEmptyFields));
                    etUsername.requestFocus();
                    etPassword.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etUsername.getText().toString()).matches()) {
                    etUsername.setError(getString(R.string.errorEmail));
                    etUsername.requestFocus();
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError(getString(R.string.errorPasswordLength));
                    etPassword.requestFocus();
                } else
                    userLogin();
                break;
        }
    }

    private void userLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, (R.string.verified), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, WelcomeSplash.class));
                        finish();
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, (R.string.errorEmailVerification), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, (R.string.errorLogin), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}