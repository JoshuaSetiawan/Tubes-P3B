package com.example.tubes_2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.ColorInt;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_2.databinding.ListFrsBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterFRS extends BaseAdapter {
    ListFrsBinding binding;
    ArrayList<Integer> tahundansemester;
    FragmentActivity fragmentActivity;
    HashMap<Integer,String> map;
    int active_year;
    public AdapterFRS(ArrayList<Integer> tahundansemester, FragmentActivity fragmentActivity,int active_year){
        this.tahundansemester = tahundansemester;
        this.fragmentActivity = fragmentActivity;
        this.active_year = active_year;
    }
    @Override
    public int getCount() {
        return tahundansemester.size();
    }

    @Override
    public Object getItem(int position) {
        return tahundansemester.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = ListFrsBinding.inflate(fragmentActivity.getLayoutInflater());
        View view = binding.getRoot();
        map = new HashMap<>();
        map.put(1,"Semester Ganjil");
        map.put(2,"Semester Genap");
        map.put(3,"Semester Pendek");
        int tahun = tahundansemester.get(position)/10;
        String semester = map.get(tahundansemester.get(position)%10);
        binding.smt.setText(semester+" "+tahun+"/"+(tahun+1));
        if(tahundansemester.get(position)==active_year){
            binding.smt.setTypeface(binding.smt.getTypeface(), Typeface.BOLD_ITALIC);
            binding.containerSemester.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle result = new Bundle();
                    result.putString("page","tambahMatkul");
                    result.putInt("tahundansem",tahundansemester.get(position));
                    result.putString("heading",semester+" "+tahun+"/"+(tahun+1));
                    fragmentActivity.getSupportFragmentManager().setFragmentResult("changePage",result);
                }
            });
        }
        else{
            binding.containerSemester.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle result = new Bundle();
                    result.putString("page","detailSemester");
                    result.putInt("tahundansem",tahundansemester.get(position));
                    result.putString("heading",semester+" "+tahun+"/"+(tahun+1));
                    fragmentActivity.getSupportFragmentManager().setFragmentResult("changePage",result);
                }
            });
        }
//        if(position==tahundansemester.size()-1) {
//            binding.containerSemester.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle result = new Bundle();
//                    result.putString("page","tambahMatkul");
//                    result.putInt("sem",position+1);
//                    result.putString("heading",binding.smt.getText().toString());
//                    fragmentActivity.getSupportFragmentManager().setFragmentResult("changePage",result);
//                }
//            });
//        }
//        else{
//            binding.containerSemester.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle result = new Bundle();
//                    result.putString("page","detailSemester");
//                    result.putInt("sem",position+1);
//                    result.putString("heading",binding.smt.getText().toString());
//                    fragmentActivity.getSupportFragmentManager().setFragmentResult("changePage",result);
//                }
//            });
//        }

        return view;
    }
}
