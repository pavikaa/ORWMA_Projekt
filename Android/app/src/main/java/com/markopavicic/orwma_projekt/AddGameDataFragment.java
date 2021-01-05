package com.markopavicic.orwma_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddGameDataFragment extends Fragment implements View.OnClickListener {
    Button addWin;
    ArrayList<String> chosenPlayers;
    Spinner spinner;
    String winner, chosenTeamName;

    public static AddGameDataFragment newInstance() {
        AddGameDataFragment fragment = new AddGameDataFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewPagerActivity activity = (ViewPagerActivity) getActivity();
        chosenPlayers = activity.getChosenPlayers();
        chosenTeamName = activity.getChosenTeamName();
        return inflater.inflate(R.layout.add_game_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addWin = (Button) view.findViewById(R.id.btnAddWin);
        spinner = (Spinner) view.findViewById(R.id.dropDown);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, chosenPlayers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        addWin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        winner = spinner.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference().child("Teams")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot nameSnapshot : snapshot.getChildren()) {
                            {
                                if (nameSnapshot.child("name").getValue(String.class).equals(chosenTeamName)) {
                                    int value = nameSnapshot.child("Players").child(winner).child("Wins").getValue(Integer.class);
                                    value++;
                                    nameSnapshot.child("Players").child(winner).child("Wins").getRef().setValue(value);
                                    for (int i = 0; i < chosenPlayers.size(); i++) {
                                        int secondValue = nameSnapshot.child("Players").child(chosenPlayers.get(i)).child("Played").getValue(Integer.class);
                                        secondValue++;
                                        nameSnapshot.child("Players").child(chosenPlayers.get(i)).child("Played").getRef().setValue(secondValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent i = new Intent(getActivity(), DataAddedSplash.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                getActivity().finish();
                                            }
                                        });
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
}
