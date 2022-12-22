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

public class LoginFragment extends Fragment {
    LoginBinding loginBinding;
    FragmentActivity fragmentActivity;
    Gson gson;
    public LoginFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginBinding = LoginBinding.inflate(inflater,container,false);
        View view = loginBinding.getRoot();
        fragmentActivity = getActivity();
        gson = new Gson();
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Base_URL = "https://ifportal.labftis.net/api/v1/authenticate";
                LoginInput loginInput =new LoginInput(loginBinding.etEmail.getText().toString(),
                        loginBinding.etPassword.getText().toString(),loginBinding.etRole.getText().toString());
                String inputJson = gson.toJson(loginInput);
                RequestQueue queue = Volley.newRequestQueue(fragmentActivity);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Base_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        memprosesKeluaranBerhasil(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            memprosesKeluaranGagal(error);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return inputJson.getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                queue.add(stringRequest);
            }
        });
        return view;

    }
    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
    public void memprosesKeluaranBerhasil(String response){
        LoginOutput loginOutput = gson.fromJson(response,LoginOutput.class);
        String hasil = "BERHASIL LOGIN\nTOKEN:";
        hasil+=loginOutput.token;
        Toast.makeText(fragmentActivity,hasil,Toast.LENGTH_LONG).show();
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(fragmentActivity,"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(fragmentActivity,"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            String jsonKeluaran = new String(error.networkResponse.data);
            JSONObject jsonObject = new JSONObject(jsonKeluaran);
            String keluaran = jsonObject.get("errcode").toString();
            String hasil = "Gagal Login";
            if(keluaran.equals("E_AUTH_FAILED")){
                hasil = "Email atau Password atau Role anda salah";
            }
            Toast.makeText(fragmentActivity,hasil,Toast.LENGTH_LONG).show();
        }


    }


}
