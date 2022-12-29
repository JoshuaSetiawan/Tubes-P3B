package com.example.tubes_2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PengumumanFragment extends Fragment implements View.OnClickListener {
    PengumumanBinding pengumumanBinding;
    Gson gson;
    ArrayList<DaftarPengumuman> daftarPengumuman;
    AdapterPengumuman adapterPengumuman;
    public PengumumanFragment(){

        gson =new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pengumumanBinding = PengumumanBinding.inflate(inflater,container,false);
        View view = pengumumanBinding.getRoot();
        daftarPengumuman = new ArrayList<>();
        adapterPengumuman = new AdapterPengumuman(daftarPengumuman,getActivity());
        pengumumanBinding.listview.setAdapter(adapterPengumuman);
        callAPI("https://ifportal.labftis.net/api/v1/announcements");
        pengumumanBinding.refresh.setOnClickListener(this);
        pengumumanBinding.searchTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callAPI("https://ifportal.labftis.net/api/v1/announcements?filter[title]="+pengumumanBinding.searchTitle.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    public void callAPI(String Base_URL){
        pengumumanBinding.listview.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.item_list_pengumuman,R.id.TampilJudul,new String[]{"loading...."}));
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
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        daftarPengumuman = gson.fromJson(jsonArray.toString(),new TypeToken<ArrayList<DaftarPengumuman>>(){}.getType());
        if(daftarPengumuman.size()==0){
            pengumumanBinding.listview.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.item_list_pengumuman,R.id.TampilJudul,new String[]{"Hasil tidak ditemukan!"}));
        }
        else{
            adapterPengumuman.setDaftarPengumuman(daftarPengumuman);
            pengumumanBinding.listview.setAdapter(adapterPengumuman);
        }

    }
    public void memprosesKeluaranGagal(VolleyError error){
        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
    }
    public static PengumumanFragment newInstance(){
        PengumumanFragment pengumumanFragment = new PengumumanFragment();
        return pengumumanFragment;
    }

    @Override
    public void onClick(View v) {
        if(v==pengumumanBinding.refresh){
            callAPI("https://ifportal.labftis.net/api/v1/announcements");
            pengumumanBinding.searchTitle.setText("");
            pengumumanBinding.filterTag.setText("");
        }
    }
}
