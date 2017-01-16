package com.example.finalproject.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;

import com.example.finalproject.Fragments.AddDessertFragment;
import com.example.finalproject.R;

public class AddDessertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dessert);

        AddDessertFragment addDessertFragment = new AddDessertFragment();
        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.add(R.id.addContainer, addDessertFragment);
        fragTran.commit();

    }

}
