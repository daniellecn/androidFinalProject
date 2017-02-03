package com.example.finalproject.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.example.finalproject.Fragments.LoginFragment;
import com.example.finalproject.Fragments.SignupFragment;
import com.example.finalproject.Model.Model;
import com.example.finalproject.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fragments
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.add(R.id.loginContainer, loginFragment);
        fragTran.show(loginFragment);
        fragTran.commit();

        // Action bar
        getActionBar().setIcon(R.color.colorPrimaryDark);
    }
}
