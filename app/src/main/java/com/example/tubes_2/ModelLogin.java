package com.example.tubes_2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelLogin implements LoginContract.model{
    Gson gson;
    FragmentActivity fragmentActivity;

    public ModelLogin(FragmentActivity fragmentActivity){
        this.fragmentActivity = fragmentActivity;
        gson = new Gson();
    }
    @Override
    public void callAPI(String email,String password,String role,LoginContract.model.OnFailedListener onFailedListener) {
        String Base_URL = "https://ifportal.labftis.net/api/v1/authenticate";
        LoginInput loginInput =new LoginInput(email,
                password,role);
        String inputJson = gson.toJson(loginInput);
        RequestQueue queue = Volley.newRequestQueue(fragmentActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               processingSuccesResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    processingFailedResponse(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onFailedListener.openInput();
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



    @Override
    public void processingSuccesResponse(String response) {
        LoginOutput loginOutput = gson.fromJson(response,LoginOutput.class);
        String hasil = "BERHASIL LOGIN\nTOKEN:";
        hasil+=loginOutput.token;
        Toast.makeText(fragmentActivity,hasil,Toast.LENGTH_LONG).show();
        Bundle result = new Bundle();
        result.putString("page","home");
        result.putString("token",loginOutput.token);
        fragmentActivity.getSupportFragmentManager().setFragmentResult("changePage",result);
    }

    @Override
    public void processingFailedResponse(VolleyError error) throws JSONException {
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
