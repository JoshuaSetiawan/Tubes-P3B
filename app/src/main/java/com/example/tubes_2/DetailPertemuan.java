package com.example.tubes_2;

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
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.BimbinganBinding;
import com.example.tubes_2.databinding.DetailPertemuanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailPertemuan extends DialogFragment {
    DetailPertemuanBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailPertemuanBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        callAPI("https://ifportal.labftis.net/api/v1/appointments/"+bundle.getString("id"),"atribut");
        callAPI("https://ifportal.labftis.net/api/v1/appointments/"+bundle.getString("id")+"/participants","partisipan");
        return view;
    }
    public synchronized void callAPI(String Base_URL,String ngapain){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(ngapain.equals("atribut")){
                        memprosesKeluaranBerhasil(response);
                    }
                    else if(ngapain.equals("partisipan")){
                        cariPartisipan(response);
                    }

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
                map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkNGNjOGZjMS02YmUyLTQ0MTEtOTJmNC01MDI3YTkyODEzNmMiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzNDQxMDh9.mbbsCLimrxtyQEa9eeeOwx43HJJ95f-IgjKmOabvHf8");
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String title = jsonObject.getString("title");
        String organizer = jsonObject.getString("organizer_name");
        String tanggal_start = jsonObject.getString("start_datetime");
        String tanggal_end = jsonObject.getString("end_datetime");
        String deskripsi = jsonObject.getString("description");
        binding.title.setText(title);
        binding.organizer.setText("Organizer: "+organizer);
        binding.tanggalStart.setText("Start: "+tanggal_start);
        binding.tanggalEnd.setText("End: "+tanggal_end);
        binding.deskripsi.setText(deskripsi);
    }
    public void memprosesKeluaranGagal(VolleyError error){
        if(error instanceof NoConnectionError){
            Toast.makeText(getActivity(),"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(getActivity(),"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(),"GAGAL",Toast.LENGTH_LONG).show();
        }
    }
    public void cariPartisipan(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        String partisipan = "";
        for(int i=0;i<jsonArray.length();i++){
            partisipan+=jsonArray.getJSONObject(i).getString("name")+",";
        }
        if(jsonArray.length()==0){
            binding.partisipan.setText("Partisipan:");
        }
        else{
            binding.partisipan.setText("Partisipan:"+partisipan.substring(0,partisipan.length()-1));
        }
    }
}
