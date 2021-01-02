package com.markopavicic.orwma_projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataAddedSplash extends AppCompatActivity {

        private static final int SPLASH_TIME_OUT = 6000;
        TextView tvDataAdded;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.welcome_splash);
            tvDataAdded=(TextView) findViewById(R.id.tvWelcome);
            tvDataAdded.setText(R.string.toastDataAdded);
         new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            Intent i = new Intent(DataAddedSplash.this, ChooseTeam.class);
            startActivity(i);
            finish();
        }
    }, SPLASH_TIME_OUT);
        }
    }