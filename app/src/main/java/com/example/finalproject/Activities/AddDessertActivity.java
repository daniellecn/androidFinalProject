package com.example.finalproject.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Fragments.AddDessertFragment;
import com.example.finalproject.Model.Model;
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

        getActionBar().setIcon(R.color.colorPrimaryDark);
    }
}
