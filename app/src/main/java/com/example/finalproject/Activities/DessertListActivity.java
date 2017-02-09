package com.example.finalproject.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

public class DessertListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert_list);

        // Action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(R.color.colorPrimaryDark);

        // check if need to display the add button
        if (Model.instance().getConnectedUser().isAdmin()) {
            ((ImageView) findViewById(R.id.add_new)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        ImageButton addButoon = (ImageButton) findViewById(R.id.add_new);
        addButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddDessertActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }
}
