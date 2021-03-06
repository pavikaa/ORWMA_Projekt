package com.markopavicic.orwma_projekt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<String> chosenPlayers;
    private String chosenTeamName;
    private Integer counter;
    private Boolean checkWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initViews();
        setUpPager();
        counter = getIntent().getIntExtra("counter", 0);
        chosenPlayers = getIntent().getStringArrayListExtra("chosenPlayers");
        chosenTeamName = getIntent().getStringExtra("chosenTeamName");
        checkWinner = getIntent().getBooleanExtra("checkWinner", true);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("chosenPlayers", chosenPlayers);
        AddGameDataFragment add = new AddGameDataFragment();
        add.setArguments(bundle);
    }

    private void initViews() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tab);
    }

    private void setUpPager() {
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public ArrayList<String> getChosenPlayers() {
        return chosenPlayers;
    }

    public String getChosenTeamName() {
        return chosenTeamName;
    }

    public Integer getCounter() {
        return counter;
    }

    public Boolean getCheckWinner() {
        return checkWinner;
    }
}