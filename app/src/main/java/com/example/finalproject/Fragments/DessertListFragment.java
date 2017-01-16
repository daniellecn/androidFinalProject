package com.example.finalproject.Fragments;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.finalproject.Activities.DessertActivity;
import com.example.finalproject.Model.Dessert;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DessertListFragment extends ListFragment {
    List<Dessert> dishes;

    public DessertListFragment() {
        // Required empty public constructor
        dishes = Model.instance().getDessertData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dessert_list, container, false);

        StudentsAdapter adapter = new StudentsAdapter();
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DessertActivity.class);
        startActivity(intent);
    }

    class StudentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dishes.size();
        }

        @Override
        public Object getItem(int i) {
            return dishes.get(i);
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

            return view;
        }
    }

}
