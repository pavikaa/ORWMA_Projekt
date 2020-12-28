package com.markopavicic.orwma_projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseTeam extends AppCompatActivity implements View.OnClickListener {
    Button btnChooseTeam;
    DatabaseReference reference;
    Spinner spinner;
    List<String> teamNames;
    TextView tvManageTeams;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);
        init();
        FirebaseDatabase.getInstance().getReference("Players")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamNames = new ArrayList<String>();
                spinner.setAdapter(null);
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    reference.child("Teams").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(check < 1){
                                check++;
                            for (DataSnapshot nameSnapshot : snapshot.getChildren()) {
                                if (nameSnapshot.child("Players").child(fullName).exists()) {
                                    String teamName = nameSnapshot.child("name").getValue(String.class);
                                    teamNames.add(teamName);
                                }
                            }
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChooseTeam.this, android.R.layout.simple_spinner_item, teamNames);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(arrayAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    private void init() {
        btnChooseTeam = findViewById(R.id.btnChoose);
        btnChooseTeam.setOnClickListener(ChooseTeam.this);
        spinner = findViewById(R.id.joinedTeamsDropDown);
        reference = FirebaseDatabase.getInstance().getReference();
        tvManageTeams = findViewById(R.id.tvManageTeams);
        tvManageTeams.setOnClickListener(ChooseTeam.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChoose:
                if(spinner!=null && spinner.getSelectedItem()!=null){
                String name = spinner.getSelectedItem().toString();
                Intent intent = new Intent(getBaseContext(), ChoosePlayers.class);
                intent.putExtra("ChosenTeamName", name);
                startActivity(intent);
                finish();}
                else {
                    Toast.makeText(ChooseTeam.this, "You have to join or create a team first", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, TeamManager.class));
                }
                    break;
            case R.id.tvManageTeams:
                startActivity(new Intent(this, TeamManager.class));
                break;
        }

    }
}