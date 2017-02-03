package com.example.finalproject.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.AddDessertActivity;
import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DessertFragment extends Fragment {
    Dessert currentDessert;

    public Dessert getCurrentDessert() {
        return currentDessert;
    }

    public DessertFragment() {
        // Required empty public constructor
    }

    public void setCurrentDessert(Dessert currentDessert) {
        this.currentDessert = currentDessert;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dessert, container, false);

        Model.instance().getDessertImage(getCurrentDessert(), 4, new Model.GetImageListener() {
            @Override
            public void onSuccess(Bitmap image) {
                ((ImageView) view.findViewById(R.id.dishImg)).setImageBitmap(image);
            }

            @Override
            public void onFail() {
                // TODO: default image
            }
        });

        ((TextView) view.findViewById(R.id.dishLable)).setText(getCurrentDessert().getName());
        ((TextView) view.findViewById(R.id.dishDesc)).setText(getCurrentDessert().getDescription());
        ((TextView) view.findViewById(R.id.dishCost)).setText(getCurrentDessert().getCost());
        ((TextView) view.findViewById(R.id.dishDates)).setText(getCurrentDessert().getDatesAvailable());

        // Action bar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);

        if (!Model.instance().getConnectedUser().isAdmin()){
            menu.removeItem(R.id.menuEdit);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuEdit:
            {
                // Create edit dessert fragment
                AddDessertFragment fragment = new AddDessertFragment();
                fragment.setNewDessert(currentDessert);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                //  Replace the fragment
                transaction.replace(R.id.dishContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
