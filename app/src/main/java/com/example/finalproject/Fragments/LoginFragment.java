package com.example.finalproject.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.Model;
import com.example.finalproject.Model.User;
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
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

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
                EditText name = (EditText) view.findViewById(R.id.loginName);
                EditText pass = (EditText) view.findViewById(R.id.loginPassword);
                User user = new User (name.getText().toString(), pass.getText().toString());

                Model.instance().logIn(user, new Model.LogInListener() {
                    @Override
                    public void onComplete(boolean isLogIn) {
                        if (isLogIn){
                            Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = getString(R.string.loginError);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
            }
        });
        return view;
    }

}
