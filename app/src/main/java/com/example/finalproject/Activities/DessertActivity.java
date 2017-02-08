package com.example.finalproject.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;

import com.example.finalproject.Fragments.DessertFragment;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

public class DessertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert);

        // Fragments
        DessertFragment fragment = new DessertFragment();
        fragment.setCurrentDessert(Model.instance().getDessertById(getIntent().getIntExtra("id", -1)));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.dishContainer, fragment);
        transaction.commit();

        // Action bar
        getActionBar().setIcon(R.color.colorPrimaryDark);
    }
}
