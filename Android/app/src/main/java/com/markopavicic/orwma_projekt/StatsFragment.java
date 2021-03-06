package com.markopavicic.orwma_projekt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsFragment extends Fragment implements View.OnClickListener {
    PieChart pcWins, pcGames, pcGamesToWin, pcPoints;
    DatabaseReference reference;
    String teamName;
    List<String> playerNames;
    List<Integer> playerWins, playerGames, playerPoints;


    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupPieChart();
        return inflater.inflate(R.layout.stats_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pcWins = (PieChart) view.findViewById(R.id.pcWins);
        pcGames = (PieChart) view.findViewById(R.id.pcPlayed);
        pcGamesToWin = (PieChart) view.findViewById(R.id.pcGamesToWin);
        pcPoints = (PieChart) view.findViewById(R.id.pcPoints);
        ViewPagerActivity activity = (ViewPagerActivity) getActivity();
        teamName = activity.getChosenTeamName();
    }

    private void setupPieChart() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot nameSnapshot : snapshot.getChildren()) {
                    {
                        if (nameSnapshot.child("name").getValue(String.class).equals(teamName)) {
                            playerNames = new ArrayList<String>();
                            playerWins = new ArrayList<Integer>();
                            playerGames = new ArrayList<Integer>();
                            playerPoints = new ArrayList<Integer>();
                            for (DataSnapshot keySnapshot : nameSnapshot.child("Players").getChildren()) {
                                String playerName = keySnapshot.getKey();
                                int playerGame = keySnapshot.child("Played").getValue(Integer.class);
                                int playerWin = keySnapshot.child("Wins").getValue(Integer.class);
                                int playerPoint = keySnapshot.child("Points").getValue(Integer.class);
                                playerNames.add(playerName);
                                playerGames.add(playerGame);
                                playerWins.add(playerWin);
                                playerPoints.add(playerPoint);
                            }
                            if (playerNames.size() == playerGames.size() && playerNames.size() == playerWins.size() && playerNames.size() == playerPoints.size()) {
                                fillPieChart();
                            } else
                                Toast.makeText(getContext(), (R.string.toastErrorFetching), Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fillPieChart() {
        ArrayList<PieEntry> pieWins = new ArrayList<>();
        ArrayList<PieEntry> pieGames = new ArrayList<>();
        ArrayList<PieEntry> pieGamesToWin = new ArrayList<>();
        ArrayList<PieEntry> piePoints = new ArrayList<>();

        String label = "";

        Map<String, Integer> typeAmountMapWins = new HashMap<>();
        Map<String, Integer> typeAmountMapGames = new HashMap<>();
        Map<String, Integer> typeAmountMapGamesToWin = new HashMap<>();
        Map<String, Integer> typeAmountPoints = new HashMap<>();

        for (int i = 0; i < playerGames.size(); i++) {
            if (playerGames.get(i) != 0)
                typeAmountMapGames.put(playerNames.get(i), (Integer) playerGames.get(i));
            if (playerWins.get(i) != 0 && playerGames.get(i) != 0) {
                typeAmountMapWins.put(playerNames.get(i), (Integer) playerWins.get(i));
                typeAmountMapGamesToWin.put(playerNames.get(i), (Integer) 100 * playerWins.get(i) / playerGames.get(i));
                typeAmountPoints.put(playerNames.get(i), (Integer) playerPoints.get(i));
            }
        }
        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));
        for (String type : typeAmountMapWins.keySet()) {
            pieWins.add(new PieEntry(typeAmountMapWins.get(type).intValue(), type));
        }
        for (String type : typeAmountMapGames.keySet()) {
            pieGames.add(new PieEntry(typeAmountMapGames.get(type).intValue(), type));
        }
        for (String type : typeAmountMapGamesToWin.keySet()) {
            pieGamesToWin.add(new PieEntry(typeAmountMapGamesToWin.get(type).intValue(), type));
        }
        for (String type : typeAmountPoints.keySet()) {
            piePoints.add(new PieEntry(typeAmountPoints.get(type).intValue(), type));
        }
        PieDataSet pieDataSetWins = new PieDataSet(pieWins, label);
        PieDataSet pieDataSetGames = new PieDataSet(pieGames, label);
        PieDataSet pieDataSetGamesToWin = new PieDataSet(pieGamesToWin, label);
        PieDataSet pieDataSetPoints = new PieDataSet(piePoints, label);

        pieDataSetWins.setValueTextSize(16f);
        pieDataSetWins.setValueTextColor(Color.rgb(255, 255, 255));
        pieDataSetGames.setValueTextSize(16f);
        pieDataSetGames.setValueTextColor(Color.rgb(255, 255, 255));
        pieDataSetGamesToWin.setValueTextSize(16f);
        pieDataSetGamesToWin.setValueTextColor(Color.rgb(255, 255, 255));
        pieDataSetPoints.setValueTextSize(16f);
        pieDataSetPoints.setValueTextColor(Color.rgb(255, 255, 255));

        pieDataSetWins.setColors(colors);
        pieDataSetGames.setColors(colors);
        pieDataSetGamesToWin.setColors(colors);
        pieDataSetPoints.setColors(colors);

        PieData pieDataWins = new PieData(pieDataSetWins);
        PieData pieDataGames = new PieData(pieDataSetGames);
        PieData pieDataGamesToWin = new PieData(pieDataSetGamesToWin);
        PieData pieDataPoints = new PieData(pieDataSetPoints);

        pieDataWins.setDrawValues(true);
        pieDataGames.setDrawValues(true);
        pieDataGamesToWin.setDrawValues(true);
        pieDataPoints.setDrawValues(true);

        pcWins.setData(pieDataWins);
        pcWins.setCenterText("Wins");
        pcWins.setCenterTextSize(20f);
        pcWins.getDescription().setEnabled(false);
        pcWins.invalidate();
        Legend l = pcWins.getLegend();
        l.setEnabled(false);


        pcGames.setData(pieDataGames);
        pcGames.invalidate();
        pcGames.setCenterText("Games played");
        pcGames.setCenterTextSize(20f);
        pcGames.getDescription().setEnabled(false);
        l = pcGames.getLegend();
        l.setEnabled(false);

        pcGamesToWin.setData(pieDataGamesToWin);
        pcGamesToWin.invalidate();
        pcGamesToWin.setCenterText("Win to game ratio");
        pcGamesToWin.setCenterTextSize(20f);
        pcGamesToWin.getDescription().setEnabled(false);
        l = pcGamesToWin.getLegend();
        l.setEnabled(false);

        pcPoints.setData(pieDataPoints);
        pcPoints.setCenterText("Points");
        pcPoints.setCenterTextSize(20f);
        pcPoints.getDescription().setEnabled(false);
        pcPoints.invalidate();
        l = pcPoints.getLegend();
        l.setEnabled(false);

        pcGames.setBackgroundColor(Color.rgb(18, 18, 18));
        pcGamesToWin.setBackgroundColor(Color.rgb(18, 18, 18));
        pcWins.setBackgroundColor(Color.rgb(18, 18, 18));
        pcPoints.setBackgroundColor(Color.rgb(18, 18, 18));
        pcGames.setHoleColor(Color.rgb(18, 18, 18));
        pcGames.setCenterTextColor(Color.rgb(220, 220, 220));
        pcGamesToWin.setHoleColor(Color.rgb(18, 18, 18));
        pcGamesToWin.setCenterTextColor(Color.rgb(220, 220, 220));
        pcWins.setHoleColor(Color.rgb(18, 18, 18));
        pcWins.setCenterTextColor(Color.rgb(220, 220, 220));
        pcPoints.setHoleColor(Color.rgb(18, 18, 18));
        pcPoints.setCenterTextColor(Color.rgb(220, 220, 220));
    }

    @Override
    public void onClick(View v) {

    }
}
