package com.example.tubes_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.PengumumanBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PengumumanFragment extends Fragment {
    PengumumanBinding pengumumanBinding;
    AdapterPengumuman adapterPengumuman;
    FragmentActivity fragmentActivity;
    Gson gson;
    public PengumumanFragment(){
        fragmentActivity = getActivity();
        gson =new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pengumumanBinding = PengumumanBinding.inflate(inflater,container,false);
        View view = pengumumanBinding.getRoot();
        return view;
    }
//    public void callAPI(){
//        String Base_URL = "https://ifportal.labftis.net/api/v1/announcements";
//        RequestQueue queue = Volley.newRequestQueue(fragmentActivity);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,
//                Base_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    memprosesKeluaranBerhasil(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                memprosesKeluaranGagal(error);
//            }
//        });
//        queue.add(stringRequest);
//    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {

    }
    public void memprosesKeluaranGagal(VolleyError error){

    }
    public static PengumumanFragment newInstance(){
        PengumumanFragment pengumumanFragment = new PengumumanFragment();
        return pengumumanFragment;
    }
}
