package com.example.tubes_2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.LoginBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements LoginContract.view {
    LoginBinding loginBinding;
    LoginContract.presenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginBinding = LoginBinding.inflate(inflater,container,false);
        View view = loginBinding.getRoot();
        presenter = new PresenterLogin(this,new ModelLogin(getActivity()),getActivity());
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonClick();
            }
        });
        return view;
    }
    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void disabledInput(){
        loginBinding.btnLogin.setEnabled(false);
        loginBinding.etEmail.setEnabled(false);
        loginBinding.etPassword.setEnabled(false);
        loginBinding.etRole.setEnabled(false);
    }
    @Override
    public void enabledInput(){
        loginBinding.btnLogin.setEnabled(true);
        loginBinding.etEmail.setEnabled(true);
        loginBinding.etPassword.setEnabled(true);
        loginBinding.etRole.setEnabled(true);
    }

    @Override
    public String getEmail() {
        return loginBinding.etEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return loginBinding.etPassword.getText().toString();
    }

    @Override
    public String getRole() {
        return loginBinding.etRole.getText().toString();
    }

}
