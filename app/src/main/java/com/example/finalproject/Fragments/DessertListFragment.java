package com.example.finalproject.Fragments;


import android.Manifest;
import android.app.ListFragment;
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

import com.example.finalproject.Activities.DessertActivity;
import com.example.finalproject.Model.AppContext;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DessertListFragment extends ListFragment {
    private static final int REQUEST_WRITE_STORAGE = 112;

    List<Dessert> dissertListData;
    //ProgressBar progressBar;
    StudentsAdapter adapter;

    public DessertListFragment() {
        // Required empty public constructor
        dissertListData = Model.instance().getDessertData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dessert_list, container, false);

        /** Check permissions ***/
//       boolean hasPermission = (ContextCompat.checkSelfPermission(AppContext.getAppContext(),
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//
//        if (!hasPermission) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_STORAGE);
//        }
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
        //progressBar = (ProgressBar) view.findViewById(R.id.listProgressBar);

        adapter = new StudentsAdapter();
        setListAdapter(adapter);

        loadDessertsListData();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DessertActivity.class);
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        // Check which request we're responding to
//        if (requestCode == NEW_STUDENT_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                loadStudentsData();
//            }
//        }
//    }

    private void loadDessertsListData(){
        // Display progress bar
        //progressBar.setVisibility(View.VISIBLE);

        Model.instance().getAllDessertAsynch(new Model.GetAllDessertsAsynchListener() {
            @Override
            public void onComplete(List<Dessert> dessertList) {
                // Cancel progress bar
                //progressBar.setVisibility(View.GONE);

                // Update list data
                dissertListData = dessertList;

                // Update the presented list
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    class StudentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dissertListData.size();
        }

        @Override
        public Object getItem(int i) {
            return dissertListData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.dessert_list_row,null);
            }

            Dessert dessert = dissertListData.get(i);
            final ImageView dessertImage = (ImageView) view.findViewById(R.id.dishRowImage);

            // If there is a image to display
            if (dessert.getImageUrl() != null && dessert.getImageUrl() != ""){
            }

            return view;
        }
    }

}
