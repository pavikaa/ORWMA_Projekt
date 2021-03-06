package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TeamManager extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference reference;
    Button btnJoin, btnCreate;
    EditText etPassword, etNewTeamName, etNewTeamPassword, etAvailable, etNewTeamPasswordRetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manager);
        init();
    }

    private void init() {
        reference = FirebaseDatabase.getInstance().getReference();
        etPassword = findViewById(R.id.etJoinTeamPassword);
        etNewTeamName = findViewById(R.id.etCreateTeam);
        etNewTeamPassword = findViewById(R.id.etCreateTeamPassword);
        etNewTeamPasswordRetype = findViewById(R.id.etCreateTeamRetypePassword);
        etAvailable = findViewById(R.id.availableTeamsTV);
        btnJoin = findViewById(R.id.btnJoin);
        btnCreate = findViewById(R.id.btnCreate);
        btnJoin.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnJoin:
                joinTeam(etPassword.getText().toString().trim());
                break;
            case R.id.btnCreate:
                if (etNewTeamPassword.getText().toString().equals(etNewTeamPasswordRetype.getText().toString()))
                    createTeam(etNewTeamName.getText().toString().trim(), etNewTeamPassword.getText().toString().trim());
                else {
                    etNewTeamPassword.setError(getString(R.string.errorPasswordMissmatch));
                    etNewTeamPasswordRetype.setError(getString(R.string.errorPasswordMissmatch));
                    etNewTeamPassword.requestFocus();
                    etNewTeamPasswordRetype.requestFocus();
                }
                break;
        }
    }

    private void createTeam(String name, String password) {
        String pushId = reference.push().getKey();
        Team team = new Team(name, password);
        FirebaseDatabase.getInstance().getReference("Teams")
                .child(pushId)
                .setValue(team).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addPlayerToTeam(pushId);
            }
        });
    }

    private void addPlayerToTeam(String teamID) {
        FirebaseDatabase.getInstance().getReference("Players")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    FirebaseDatabase.getInstance().getReference("Teams")
                            .child(teamID).child("Players").child(fullName)
                            .child("Wins").setValue(0);
                    FirebaseDatabase.getInstance().getReference("Teams")
                            .child(teamID).child("Players").child(fullName)
                            .child("Played").setValue(0);
                    FirebaseDatabase.getInstance().getReference("Teams")
                            .child(teamID).child("Players").child(fullName)
                            .child("Points").setValue(0);

                    FirebaseDatabase.getInstance().getReference("Teams")
                            .child(teamID).child("Players").child(fullName).child("Joined")
                            .setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(TeamManager.this, ChooseTeam.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void joinTeam(String password) {
        String name = etAvailable.getText().toString().trim();
        Query teamsQuery = reference.child("Teams");
        teamsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot teams : snapshot.getChildren()) {
                    Team mTeam = teams.getValue(Team.class);
                    if (mTeam.getName().equals(name) && mTeam.getPassword().equals(password)) {
                        Toast.makeText(TeamManager.this, (R.string.toastTeamJoined), Toast.LENGTH_LONG).show();
                        addPlayerToTeam(teams.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}