package com.example.finalproject.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.finalproject.Fragments.DessertFragment;
import com.example.finalproject.R;

public class DessertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert);

        // Fragments
        DessertFragment fragment = new DessertFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.dishContainer, fragment);
        transaction.commit();

        // Action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(R.color.colorPrimaryDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuEdit: {
                Intent intent = new Intent(getApplicationContext(), AddDessertActivity.class);
                // TODO : add the id to the intent
                //intent.putExtra("id", Integer.parseInt(((TextView)findViewById(R.id.stdId)).getText().toString()));
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
