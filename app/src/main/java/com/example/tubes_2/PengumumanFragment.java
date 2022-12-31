package com.example.tubes_2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PengumumanFragment extends Fragment implements View.OnClickListener{
    PengumumanBinding pengumumanBinding;
    Gson gson;
    ArrayList<DaftarPengumuman> daftarPengumuman;
    AdapterPengumuman adapterPengumuman;
    String next;
    SharedPreferences sp;
    ArrayList<String> checkTag;
    ArrayList<String> checkTagId;
    public PengumumanFragment(){
        gson =new Gson();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pengumumanBinding = PengumumanBinding.inflate(inflater,container,false);
        View view = pengumumanBinding.getRoot();
        daftarPengumuman = new ArrayList<>();
        checkTag = new ArrayList<>();
        checkTagId = new ArrayList<>();
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        adapterPengumuman = new AdapterPengumuman(daftarPengumuman,getActivity());
        pengumumanBinding.listview.setAdapter(adapterPengumuman);
        ambilTags();
        callAPI("https://ifportal.labftis.net/api/v1/announcements",false);
        pengumumanBinding.refresh.setOnClickListener(this);
        pengumumanBinding.searchTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callAPI("https://ifportal.labftis.net/api/v1/announcements",false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pengumumanBinding.next.setOnClickListener(this);
        pengumumanBinding.filter.setOnClickListener(this);
        return view;
    }
    public void callAPI(String Base_URL,boolean isCursor){
        if(!isCursor){
            Base_URL+="?filter[title]="+pengumumanBinding.searchTitle.getText().toString();
            if(checkTagId.size()!=0){
                for(int i=0;i<checkTagId.size();i++){
                    Base_URL+="&filter[tags][]="+checkTagId.get(i);
                }
            }
        }
        Toast.makeText(getActivity(),Base_URL,Toast.LENGTH_LONG).show();
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
                try {
                    memprosesKeluaranGagal(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public void ambilTags(){
//        if(sp.contains("checkTag")){
            if(sp.getString("checkTag","").equals("")){
                checkTag.clear();
                pengumumanBinding.filterApply.setText("Filter : None");
            }
            else{
                checkTag = new ArrayList<>(Arrays.asList(sp.getString("checkTag","").split(",")));
                pengumumanBinding.filterApply.setText("Filter:"+sp.getString("checkTag",""));
            }
//        }
//        if(sp.contains("checkTagId")){
            if(sp.getString("checkTagId","").equals("")){
               checkTagId.clear();
            }else{
                checkTagId = new ArrayList<>(Arrays.asList(sp.getString("checkTagId","").split(",")));
            }

//        }
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        Object object = jsonObject.getJSONObject("metadata").get("next");
        if(object.equals(null)){
           pengumumanBinding.next.setVisibility(View.INVISIBLE);
        }
        else{
            pengumumanBinding.next.setVisibility(View.VISIBLE);
            next = object.toString();
        }
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
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(getActivity(),"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(getActivity(),"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            String jsonKeluaran = new String(error.networkResponse.data);
            JSONObject jsonObject = new JSONObject(jsonKeluaran);
            String keluaran = jsonObject.get("errcode").toString();

            Toast.makeText(getActivity(),keluaran,Toast.LENGTH_LONG).show();
        }

    }
    public static PengumumanFragment newInstance(){
        PengumumanFragment pengumumanFragment = new PengumumanFragment();
        return pengumumanFragment;
    }

    @Override
    public void onClick(View v) {
        if(v==pengumumanBinding.refresh){
            pengumumanBinding.searchTitle.setText("");
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            ambilTags();
            callAPI("https://ifportal.labftis.net/api/v1/announcements",false);
        }
        else if(v==pengumumanBinding.next){
            callAPI("https://ifportal.labftis.net/api/v1/announcements?cursor="+next,true);
        }
        else if(v==pengumumanBinding.filter){
            Filter filter = new Filter(this);
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            filter.show(ft,"a");
        }
    }

}
