package com.markopavicic.orwma_projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText etUsername, etPassword, etPassword2, etFullName;
    private TextView tvSignIn;
    private ProgressBar progressBar;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        tvSignIn = findViewById(R.id.signIn);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etPassword2 = findViewById(R.id.etRegisterRetypePassword);
        etFullName = findViewById(R.id.etFullName);
        register = findViewById(R.id.btnRegister);
        tvSignIn.setOnClickListener(this);
        register.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBarRegister);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.btnRegister:
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("") || etPassword2.getText().toString().equals("") || etFullName.getText().toString().equals("")) {
                    etUsername.setError(getString(R.string.errorEmptyFields));
                    etFullName.setError(getString(R.string.errorEmptyFields));
                    etPassword.setError(getString(R.string.errorEmptyFields));
                    etPassword2.setError(getString(R.string.errorEmptyFields));
                    etFullName.requestFocus();
                    etUsername.requestFocus();
                    etPassword.requestFocus();
                    etPassword2.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etUsername.getText().toString()).matches()) {
                    etUsername.setError(getString(R.string.errorEmail));
                    etUsername.requestFocus();
                } else if (!etPassword.getText().toString().equals(etPassword2.getText().toString())) {
                    etPassword.setError(getString(R.string.errorPasswordMissmatch));
                    etPassword2.setError(getString(R.string.errorPasswordMissmatch));
                    etPassword.requestFocus();
                    etPassword2.requestFocus();
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError(getString(R.string.errorPasswordLength));
                    etPassword.requestFocus();
                } else
                    registerUser();
                break;
        }
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, password, fullName);
                            FirebaseDatabase.getInstance().getReference("Players")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, (R.string.successfulRegistration), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        etUsername.getText().clear();
                                        etFullName.getText().clear();
                                        etPassword.getText().clear();
                                        etPassword2.getText().clear();
                                    } else {
                                        Toast.makeText(RegisterUser.this, (R.string.unsuccessfulRegistration), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        etPassword.getText().clear();
                                        etPassword2.getText().clear();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, (R.string.unsuccessfulRegistration), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            etPassword.getText().clear();
                            etPassword2.getText().clear();
                        }
                    }
                });
        startActivity(new Intent(RegisterUser.this, LoginActivity.class));
        finish();
    }
}