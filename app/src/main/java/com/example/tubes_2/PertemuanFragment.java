package com.example.tubes_2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_2.databinding.PertemuanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PertemuanFragment extends Fragment {
    PertemuanBinding pertemuanBinding;
    AdapterPertemuan adapterPertemuan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pertemuanBinding = PertemuanBinding.inflate(inflater,container,false);
        View view = pertemuanBinding.getRoot();
        pertemuanBinding.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("page","tambahPertemuan");
                getParentFragmentManager().setFragmentResult("changePage",result);
            }
        });
        adapterPertemuan = new AdapterPertemuan(getActivity());
        pertemuanBinding.lstPert.setAdapter(adapterPertemuan);
        Calendar calendar = Calendar.getInstance();
        pertemuanBinding.datePickerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                String date = year + "-" + versiString(month+1) + "-" + versiString(dayOfMonth);
                                pertemuanBinding.datePickerStart.setText(date);
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.set(year,month,dayOfMonth);
                                calendar2.add(Calendar.DATE,7);
                                String semingguLagi = calendar2.get(Calendar.YEAR)+"-"+versiString(calendar2.get(Calendar.MONTH)+1)+"-"+versiString(calendar2.get(Calendar.DATE));
                                pertemuanBinding.datePickerEnd.setText(semingguLagi);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        pertemuanBinding.tombolCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pertemuanBinding.lstPert.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"Loading...."}));
                pertemuanBinding.tombolCari.setEnabled(false);
                callAPI("https://ifportal.labftis.net/api/v1/appointments/start-date/"+pertemuanBinding.datePickerStart.getText().toString()+"/end-date/"+pertemuanBinding.datePickerEnd.getText().toString());
            }
        });
        pertemuanBinding.undangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("page","daftarUndangan");
                getParentFragmentManager().setFragmentResult("changePage",result);
            }
        });
        return view;
    }
    public String versiString(int i){
        String versiString=i+"";
        if(i<10){
            versiString="0"+versiString;
        }
        return versiString;
    }
    public void callAPI(String Base_URL){
        adapterPertemuan.clear();
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
                map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkNGNjOGZjMS02YmUyLTQ0MTEtOTJmNC01MDI3YTkyODEzNmMiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMzMTkyOTF9.xkemRp96MLypBPLdgU_FmEzcBb_5jErP1kxHxlmsMjo");
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            String id = jsonArray.getJSONObject(i).getString("id");
            String title = jsonArray.getJSONObject(i).getString("title");
            String started_datetime = jsonArray.getJSONObject(i).getString("start_datetime");
            String end_datetime = jsonArray.getJSONObject(i).getString("end_datetime");
            adapterPertemuan.add(id,title,started_datetime,end_datetime);
        }
        if(jsonArray.length()!=0){
            pertemuanBinding.lstPert.setAdapter(adapterPertemuan);
        }
        else{
            pertemuanBinding.lstPert.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"Hasil tidak ditemukan!"}));
        }
        pertemuanBinding.tombolCari.setEnabled(true);
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(getActivity(),"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(getActivity(),"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(),"GAGAL",Toast.LENGTH_LONG).show();
        }
        pertemuanBinding.lstPert.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"Gagal!"}));
        pertemuanBinding.tombolCari.setEnabled(true);
    }
}
