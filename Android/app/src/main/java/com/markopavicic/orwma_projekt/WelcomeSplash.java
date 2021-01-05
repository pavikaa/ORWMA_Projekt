package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeSplash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2500;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_splash);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Players");
        userID = user.getUid();
        final TextView tvWelcome = findViewById(R.id.tvWelcome);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    tvWelcome.setText(getString(R.string.welcomeSplash) + fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvWelcome.setText(R.string.welcomeSplashAlternative);
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(WelcomeSplash.this, ChooseTeam.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
