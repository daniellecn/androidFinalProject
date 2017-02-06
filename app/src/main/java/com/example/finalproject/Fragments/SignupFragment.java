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
                final User[] user = new User[1];
                final EditText userName = (EditText) view.findViewById(R.id.signName);
                final EditText userPass = (EditText) view.findViewById(R.id.signPassword);
                final EditText userMail = (EditText) view.findViewById(R.id.signMail);

                /*** Validation ***/
                Model.instance().isUserNameExist(userName.getText().toString(), new Model.SuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            userName.setError(getString(R.string.signupUsernameError));
                        } else {
                            Model.instance().isUserMailExist(userMail.getText().toString(), new Model.SuccessListener() {
                                @Override
                                public void onResult(boolean result) {
                                    if (result) {
                                        userMail.setError(getString(R.string.signupMailError));
                                    } else {
                                        if (userName.getText() == null) {
                                            userName.setError(getString(R.string.emptyField));
                                        } else if (userPass.getText() == null) {
                                            userPass.setError(getString(R.string.emptyField));
                                        } else if (userMail.getText() == null) {
                                            userMail.setError(getString(R.string.emptyField));
                                        }
                                        /*** Create account ***/
                                        else {
                                            user[0] = new User(userName.getText().toString(), userPass.getText().toString(), userMail.getText().toString());


                                            Model.instance().signUp(user[0], new Model.SuccessListener() {
                                                @Override
                                                public void onResult(boolean result) {
                                                    if (result) {
                                                        Model.instance().setConnectedUser(user[0]);
                                                        Intent intent = new Intent(getActivity().getApplicationContext(), DessertListActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Context context = getActivity().getApplicationContext();
                                                        CharSequence text = getString(R.string.signupUsernameError);
                                                        int duration = Toast.LENGTH_SHORT;

                                                        Toast toast = Toast.makeText(context, text, duration);
                                                        toast.show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        return view;
    }
}
