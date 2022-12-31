package com.example.tubes_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_2.databinding.ItemListPengumumanBinding;

import java.util.ArrayList;

public class AdapterPengumuman extends BaseAdapter {
    ItemListPengumumanBinding itemListPengumumanBinding;
    ArrayList<DaftarPengumuman> daftarPengumuman;
    FragmentActivity activity;
    public AdapterPengumuman(ArrayList<DaftarPengumuman> daftarPengumuman, FragmentActivity activity){
        this.daftarPengumuman = daftarPengumuman;
        this.activity = activity;

    }
//    public AdapterPengumuman(Activity activity){
//        this.daftarPengumuman = new ArrayList<>();
//        this.activity = activity;
//    }
    public void setDaftarPengumuman(ArrayList<DaftarPengumuman> daftarPengumuman){
        this.daftarPengumuman = daftarPengumuman;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return daftarPengumuman.size();
    }

    @Override
    public Object getItem(int position) {
        return daftarPengumuman.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       itemListPengumumanBinding = ItemListPengumumanBinding.inflate(activity.getLayoutInflater());
       View view = itemListPengumumanBinding.getRoot();
       itemListPengumumanBinding.TampilJudul.setText(this.daftarPengumuman.get(position).title);
       String tags = "";
       for(int i=0;i<daftarPengumuman.get(position).tags.size();i++){
           tags+=daftarPengumuman.get(position).tags.get(i).tag+",";
       }
       tags = tags.substring(0,tags.length()-1);
       itemListPengumumanBinding.TampilTags.setText(tags);
       itemListPengumumanBinding.pengumuman.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle bundle = new Bundle();
               bundle.putString("id",daftarPengumuman.get(position).id);
               DetailPengumumanFragment detailPengumumanFragment = new DetailPengumumanFragment();
               detailPengumumanFragment.setArguments(bundle);
               FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
               detailPengumumanFragment.show(ft,"detail");

           }
       });
       return view;
    }
}
