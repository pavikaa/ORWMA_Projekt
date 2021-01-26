package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChoosePlayers extends AppCompatActivity implements View.OnClickListener {
    Button btnDone;
    DatabaseReference reference;
    ListView list;
    List<String> playerNames, chosenPlayers;
    String chosenTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_players);
        init();
    }

    private void init() {
        chosenTeamName = getIntent().getStringExtra("chosenTeamName");
        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(ChoosePlayers.this);
        list = findViewById(R.id.playersList);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot nameSnapshot : snapshot.getChildren()) {
                    {
                        playerNames = new ArrayList<String>();
                        if (nameSnapshot.child("name").getValue(String.class).equals(chosenTeamName)) {
                            for (DataSnapshot keySnapshot : nameSnapshot.child("Players").getChildren()) {
                                String playerName = keySnapshot.getKey();
                                playerNames.add(playerName);
                                setupListView();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setupListView() {

        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, playerNames));
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onClick(View v) {
        boolean check = false;
        SparseBooleanArray checked = list.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i))
                check = true;
        }
        chosenPlayers = new ArrayList<String>();
        if (check) {
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if (checked.valueAt(i))
                    chosenPlayers.add(list.getAdapter().getItem(position).toString());
            }
            Intent i = new Intent(getBaseContext(), ViewPagerActivity.class);
            i.putStringArrayListExtra("chosenPlayers", (ArrayList<String>) chosenPlayers);
            i.putExtra("chosenTeamName", chosenTeamName);
            startActivity(i);
            finish();
        } else
            Toast.makeText(this, (R.string.toastChoosePlayer), Toast.LENGTH_LONG).show();
    }
}