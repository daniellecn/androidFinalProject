package com.example.finalproject.Fragments;


import android.Manifest;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.DessertActivity;
import com.example.finalproject.Model.AppContext;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DessertListFragment extends ListFragment {
    private static final int REQUEST_WRITE_STORAGE = 112;
    static final int NEW_DESSERT_REQUEST = 1;

    List<Dessert> dessertListData;

    ProgressBar progressBar;
    DesseertAdapter adapter;

    public DessertListFragment() {
        // Required empty public constructor
        dessertListData = Model.instance().getDessertData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dessert_list, container, false);

        /** Check permissions ***/
        boolean hasPermission = (ContextCompat.checkSelfPermission(AppContext.getAppContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        /** END Check permissions ***/

        // Get elements from screen
        progressBar = (ProgressBar) view.findViewById(R.id.listProgressBar);

        adapter = new DesseertAdapter();
        setListAdapter(adapter);

        if (Intent.ACTION_SEARCH.equals(getActivity().getIntent().getAction())) {
            String query = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            dessertListData = Model.instance().getBySearch(query);

        } else {
            loadDessertsListData();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DessertActivity.class);
        intent.putExtra("id", dessertListData.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == NEW_DESSERT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Display progress bar
                progressBar.setVisibility(View.VISIBLE);
                loadDessertsListData();
            }
        }
    }

    private void loadDessertsListData() {
        Model.instance().getAllDessertAsynch(new Model.GetAllDessertsAsynchListener() {
            @Override
            public void onComplete(List<Dessert> dessertList) {

                // Cancel progress bar
                progressBar.setVisibility(View.GONE);

                // Update list data
                dessertListData = dessertList;

                // Update the presented list
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                // Display message
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorOccure),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class DesseertAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dessertListData.size();
        }

        @Override
        public Object getItem(int i) {
            return dessertListData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.dessert_list_row, null);
            }

            final Dessert dessert = dessertListData.get(i);

            ((TextView) view.findViewById(R.id.dishRowLable)).setText(dessert.getName());
            ((TextView) view.findViewById(R.id.dishRowDesc)).setText(dessert.getDescription());

            final View finalView = view;
            Model.instance().getDessertImage(dessert, 0, new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    ((ImageView) finalView.findViewById(R.id.dishRowImage)).setImageBitmap(image);
                }

                @Override
                public void onFail() {
                    // Display message
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorOccure),
                            Toast.LENGTH_SHORT).show();
                }

            });

            return finalView;
        }
    }
}
