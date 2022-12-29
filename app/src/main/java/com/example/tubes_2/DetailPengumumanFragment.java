package com.example.tubes_2;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.DetailPengumumanBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailPengumumanFragment extends DialogFragment {
    DetailPengumumanBinding detailPengumumanBinding;
    Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        detailPengumumanBinding = DetailPengumumanBinding.inflate(inflater,container,false);
        View view = detailPengumumanBinding.getRoot();
        int width = getResources().getDimensionPixelSize(R.dimen.width_dialog_fragment);
        int height = getResources().getDimensionPixelSize(R.dimen.height_dialog_fragment);
        getDialog().getWindow().setLayout(width,height);
        String id = getArguments().getString("id");
        gson = new Gson();
        callAPI("https://ifportal.labftis.net/api/v1/announcements/"+id);
        return view;
    }
    public void callAPI(String Base_URL){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    memprosesKeluaranBerhasil(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                memprosesKeluaranGagal(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiI2ZTY2ODZmMC0yOTZlLTRjNzItOGE0NS1hNmFjMWVkNDhlNDQiLCJyb2xlIjoiYWRtaW4ifSwiaWF0IjoxNjcxODY3ODQ5fQ.1i7kt7EWvw_q9EzRPFGePWiZxx4c5dRmMSm1jV93g_I");
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        Tags[] tags = gson.fromJson(jsonObject.getJSONArray("tags").toString(),Tags[].class);
        String semuaTags = "";
        for(int i=0;i<tags.length;i++){
            semuaTags+=tags[i].tag+",";
        }
        semuaTags = semuaTags.substring(0,semuaTags.length()-1);
        detailPengumumanBinding.title.setText(jsonObject.getString("title"));
        detailPengumumanBinding.content.setText(jsonObject.getString("content"));
        detailPengumumanBinding.tags.setText(semuaTags);

    }
    public void memprosesKeluaranGagal(VolleyError error){
        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
    }
}
