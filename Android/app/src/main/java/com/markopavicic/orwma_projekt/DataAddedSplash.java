package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DataAddedSplash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;
    TextView tvDataAdded;
    String chosenTeamName;
    List<String> chosenPlayers;
    Integer counter;
    Boolean checkWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_splash);
        tvDataAdded = (TextView) findViewById(R.id.tvWelcome);
        tvDataAdded.setText(R.string.toastDataAdded);
        chosenTeamName = getIntent().getStringExtra("chosenTeamName");
        chosenPlayers = getIntent().getStringArrayListExtra("chosenPlayers");
        counter = getIntent().getIntExtra("counter", 1);
        checkWinner = getIntent().getBooleanExtra("checkWinner", true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(getBaseContext(), ViewPagerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putStringArrayListExtra("chosenPlayers", (ArrayList<String>) chosenPlayers);
                i.putExtra("chosenTeamName", chosenTeamName);
                i.putExtra("counter", counter);
                i.putExtra("checkWinner", checkWinner);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}