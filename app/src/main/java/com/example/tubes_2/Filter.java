package com.example.tubes_2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.FilterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Filter extends DialogFragment implements View.OnClickListener {
    FilterBinding filterBinding;
    ArrayList<String> dicheck;
    SharedPreferences sp;
    HashMap<String,String> hashMap;
    PengumumanFragment pengumumanFragment;
    public Filter(PengumumanFragment pengumumanFragment){
        this.pengumumanFragment = pengumumanFragment;
    }
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        filterBinding = FilterBinding.inflate(inflater,container,false);
        View view = filterBinding.getRoot();
        sp =getActivity().getPreferences(Context.MODE_PRIVATE);
        hashMap = new HashMap<>();
//        if(sp.contains("checkTag")){
            if(sp.getString("checkTag","").equals("")){
                dicheck = new ArrayList<>();
            }
            else{
                dicheck = new ArrayList<>(Arrays.asList(sp.getString("checkTag","").split(",")));
            }

//        }
//        else{
//            dicheck = new ArrayList<>();
//        }
        callAPI("https://ifportal.labftis.net/api/v1/tags");
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
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            hashMap.put(jsonArray.getJSONObject(i).getString("tag"),jsonArray.getJSONObject(i).getString("id"));
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setText(jsonArray.getJSONObject(i).getString("tag"));
            if(dicheck.contains(jsonArray.getJSONObject(i).getString("tag"))){
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        dicheck.add(buttonView.getText().toString());
                    }
                    else{
                        dicheck.remove(buttonView.getText().toString());
                    }
                }
            });
            filterBinding.containerFilter.addView(checkBox);
        }
        Button button = new Button(getActivity());
        button.setText("APPLY");
        button.setOnClickListener(this);
        filterBinding.containerFilter.addView(button);

    }
    public void memprosesKeluaranGagal(VolleyError error){
        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sp.edit();
        String id="";
        for(int i=0;i<dicheck.size();i++){
            id+=hashMap.get(dicheck.get(i))+",";
        }
        if(id.length()!=0){
            editor.putString("checkTagId",id.substring(0,id.length()-1));
        }
        else{
            editor.putString("checkTagId","");
        }

        String tag = "";
        for(int i=0;i<dicheck.size();i++){
            tag+=dicheck.get(i)+",";
        }
        if(tag.length()!=0){
            editor.putString("checkTag",tag.substring(0,tag.length()-1));
        }
        else{
            editor.putString("checkTag","");
        }

        editor.apply();
        this.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        pengumumanFragment.ambilTags();
        pengumumanFragment.callAPI("https://ifportal.labftis.net/api/v1/announcements",false);
    }
}
