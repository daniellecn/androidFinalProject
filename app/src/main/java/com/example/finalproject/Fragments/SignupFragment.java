package com.example.finalproject.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Activities.DessertListActivity;
import com.example.finalproject.Model.Model;
import com.example.finalproject.Model.User;
import com.example.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Action bar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);

        Button signupButton = (Button) view.findViewById(R.id.signupCreate);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText) view.findViewById(R.id.signName);
                EditText userPass = (EditText) view.findViewById(R.id.signPassword);
                User user = new User(userName.getText().toString(), userPass.getText().toString());

                Model.instance().signUp(user, new Model.successListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                            startActivity(intent);
                        } else {
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = getString(R.string.signupError);
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
