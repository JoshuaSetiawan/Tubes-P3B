package com.example.tubes_2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
import com.example.tubes_2.databinding.ItemListPertemuanBinding;
import com.example.tubes_2.databinding.ItemListUndanganBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterUndangan extends BaseAdapter {
    ArrayList<Undangan> undangans;
    ItemListUndanganBinding binding;
    FragmentActivity fragmentActivity;
    DaftarUndanganFragment daftarUndanganFragment;
    public AdapterUndangan(FragmentActivity fragmentActivity,DaftarUndanganFragment daftarUndanganFragment){
        this.undangans = new ArrayList<>();
        this.fragmentActivity = fragmentActivity;
        this.notifyDataSetChanged();
        this.daftarUndanganFragment = daftarUndanganFragment;
    }
    public void add(String id,String title,String start_datetime,String end_datetime,String description,String organizer_name,boolean attending){
        Undangan undangan = new Undangan(id,title,start_datetime,end_datetime,description,organizer_name,attending);
        undangans.add(undangan);
        this.notifyDataSetChanged();
    }
    public void clear(){
        this.undangans.clear();
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return undangans.size();
    }

    @Override
    public Object getItem(int position) {
        return undangans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = ItemListUndanganBinding.inflate(fragmentActivity.getLayoutInflater());
        View view = binding.getRoot();
        binding.deskripsi.setText(this.undangans.get(position).description);
        binding.organizer.setText(this.undangans.get(position).organizer);
        binding.tanggalEnd.setText(this.undangans.get(position).end_datetime);
        binding.tanggalStart.setText(this.undangans.get(position).started_datetime);
        binding.title.setText(this.undangans.get(position).title);
        if(this.undangans.get(position).attending){
            binding.accept.setVisibility(View.INVISIBLE);
            binding.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callAPI("https://ifportal.labftis.net/api/v1/appointments/"+undangans.get(position).id+"/participants/d4cc8fc1-6be2-4411-92f4-5027a928136c","decline");
                }
            });
            binding.containerUndangan.setBackgroundTintList(ColorStateList.valueOf(fragmentActivity.getResources().getColor(R.color.green)));
        }
        else{
            binding.decline.setVisibility(View.INVISIBLE);
            binding.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callAPI("https://ifportal.labftis.net/api/v1/appointments/"+undangans.get(position).id+"/participants/d4cc8fc1-6be2-4411-92f4-5027a928136c","accept");
                }
            });
            binding.containerUndangan.setBackgroundTintList(ColorStateList.valueOf(fragmentActivity.getResources().getColor(R.color.red)));
        }

        return view;
    }
    public void callAPI(String Base_URL,String ngapain){
        RequestQueue queue = Volley.newRequestQueue(fragmentActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH,
                Base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    memprosesKeluaranBerhasil(response,ngapain);
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
                map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkNGNjOGZjMS02YmUyLTQ0MTEtOTJmNC01MDI3YTkyODEzNmMiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzM0MTI4NzR9.gMV_1UfVq_zRtynlNpkjrQ2SJf279b7k10ekqEkUyFs");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                if(ngapain.equals("accept")){
                    try {
                        jsonObject.put("attending",true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(ngapain.equals("decline")){
                    try {
                        jsonObject.put("attending",false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return jsonObject.toString().getBytes();
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(fragmentActivity,"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(fragmentActivity,"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(fragmentActivity,"GAGAL!",Toast.LENGTH_LONG).show();
        }
    }
    public void memprosesKeluaranBerhasil(String response,String ngapain) throws JSONException {
        Toast.makeText(fragmentActivity,"BERHASIL "+ngapain,Toast.LENGTH_LONG).show();
        daftarUndanganFragment.refresh();
    }

}
