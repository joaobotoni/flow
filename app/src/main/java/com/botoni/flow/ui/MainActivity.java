package com.botoni.flow.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botoni.flow.R;
import com.botoni.flow.ui.fragments.CattlePriceSettingsFragment;
import com.botoni.flow.ui.fragments.DealFragment;
import com.botoni.flow.ui.fragments.MainFragment;
import com.botoni.flow.ui.fragments.RouteFragment;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, RouteFragment.class, null)
                    .commit();
        }
    }
}
