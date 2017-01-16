package com.example.finalproject.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Action bar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView signupText = (TextView) view.findViewById(R.id.loginSignup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment signupFragment = new SignupFragment();
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragTran.replace(R.id.loginContainer, signupFragment);
                fragTran.commit();
            }
        });

        Button loginButton = (Button) view.findViewById(R.id.loginLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
