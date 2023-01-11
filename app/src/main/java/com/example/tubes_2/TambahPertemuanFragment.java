package com.example.tubes_2;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
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
import com.example.tubes_2.databinding.TambahPertemuanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TambahPertemuanFragment extends Fragment {
    TambahPertemuanBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TambahPertemuanBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("https://ifportal.labftis.net/api/v1/appointments");
            }
        });
        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                month++;
                                String date = year + "-" + versiString(month) + "-" + versiString(dayOfMonth);
                                binding.startDate.setText(date);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                month++;
                                String date = year + "-" + versiString(month) + "-" + versiString(dayOfMonth);
                                binding.endDate.setText(date);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        binding.jamMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                binding.jamMulai.setText(versiString(hourOfDay) + ":" + versiString(minute));
                            }
                        }, 7, 10, true);
                timePickerDialog.show();
            }
        });
        binding.jamSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                binding.jamSelesai.setText(versiString(hourOfDay) + ":" + versiString(minute));
                            }
                        }, 7, 10, true);
                timePickerDialog.show();
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
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
                try {
                    jsonObject.put("title",binding.title.getText().toString());
                    jsonObject.put("description",binding.deskripsi.getText().toString());
                    jsonObject.put("start_datetime",binding.startDate.getText().toString()+" "+binding.jamMulai.getText().toString()+"+0700");
                    jsonObject.put("end_datetime",binding.endDate.getText().toString()+" "+binding.jamSelesai.getText().toString()+"+0700");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        Toast.makeText(getActivity(),"Berhasil!",Toast.LENGTH_LONG).show();
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(getActivity(),"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(getActivity(),"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(),new String(error.networkResponse.data),Toast.LENGTH_LONG).show();
        }

    }
}
